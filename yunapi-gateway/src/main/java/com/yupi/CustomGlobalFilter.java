package com.yupi;

import com.yupi.model.entity.InterfaceInfo;
import com.yupi.model.entity.User;
import com.yupi.service.InnerInterfaceInfoService;
import com.yupi.service.InnerUserInterfaceInfoService;
import com.yupi.service.InnerUserService;
import com.yupi.yuapiclientsdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 周韵启
 *
 * 全局过滤
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    //接口地址
    private static final String INTERFACE_HOST = "http://localhost:8090";
    //白名单，如果不在这里面的则不能访问    数组转换成集合
    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");//本机地址

    @Override
    public Mono<Void> filter(ServerWebExchange
                                     exchange, GatewayFilterChain chain) {
        //1. 用户发送请求到 API 网关——已实现
        //2. 请求日志
        ServerHttpRequest request = exchange.getRequest();
        String path = INTERFACE_HOST + request.getPath().value();
        String method = request.getMethod().toString();

        log.info("请求唯一标识:" + request.getId());
        log.info("请求路径:" + path);
        log.info("请求方法:" + method);
        log.info("请求参数:" + request.getQueryParams());
        log.info("请求来源地址:" + request.getRemoteAddress());
        String sourceAdress = request.getLocalAddress().getHostString();
        log.info("请求来源地址:" + sourceAdress);
        //获取响应对象
        ServerHttpResponse response = exchange.getResponse();
        //3. 访问控制 - 黑白名单
        if (!IP_WHITE_LIST.contains(sourceAdress)) {
            return handleNoAuth(response);
        }
        //4. 用户鉴权（判断ak、sk是否合法）
        //先拿到请求头
        HttpHeaders httpHeaders = request.getHeaders();
        String accessKey = httpHeaders.getFirst("accessKey");
        String nonce = httpHeaders.getFirst("nonce");
        String timestamp = httpHeaders.getFirst("timestamp");
        String sign = httpHeaders.getFirst("sign");
        String body = httpHeaders.getFirst("body");
        // todo 实际情况应该是去数据库中查是否已分配给用户   ----以实现
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("远程调用获取调用接口用户的信息失败");
            e.printStackTrace();
        }
        //無權限
        if (invokeUser == null) {
            return handleNoAuth(response);
        }
        if (Long.parseLong(nonce) > 10000L) {
            return handleNoAuth(response);
        }
        // 时间和当前时间不能超过 5 分钟
        //取出当前时间除以1000毫秒，就是秒
        Long currentTime = System.currentTimeMillis() / 1000;
        final Long FIVE_MINUTES = 60 * 5L;
        if ((currentTime - Long.parseLong(timestamp)) >= FIVE_MINUTES) {
            return handleNoAuth(response);
        }
        // todo 实际情况中是从数据库中查出 secretKey  ---已实现
        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtils.genSign(body, secretKey);
        if (sign == null || !sign.equals(serverSign)) {
            log.error("签名校验失败!!!!");
            return handleNoAuth(response);
        }
        //5. 请求的模拟接口是否存在？
        // todo 从数据库中查询模拟接口是否匹配，以及请求方法是否匹配（还可以校验请求参数）
        InterfaceInfo interfaceInfo = null;
        int leftInvokeCount = 0;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path, method);
            //leftInvokeCount = innerUserInterfaceInfoService.getLeftInvokeCount(invokeUser.getId(), interfaceInfo.getId());
        } catch (Exception e) {
            log.error("远程调用获取被调用接口信息失败");
            e.printStackTrace();
        }
        if (interfaceInfo == null ) {
            log.error("接口不存在！！！！");
            return handleNoAuth(response);
        }
        //todo 是否还有调用次数
//        int leftInvokeCount = innerUserInterfaceInfoService.getLeftInvokeCount(invokeUser.getId(), interfaceInfo.getId());
//        if (leftInvokeCount <= 0){
//            log.error("接口剩余次数不足");
//            return handleNoAuth(response);
//        }



        //7. 响应日志 + 请求转发
        return handleResponse(exchange, chain,interfaceInfo.getId(),invokeUser.getId());

    }


    /**
     * 处理响应
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain,long interfaceInfoId,long userId) {
        try {
            // 从交换机拿到原始response
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓冲区工厂 拿到缓存数据
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到状态码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行,在这个方法中就可以打印日志
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 把零散的数据拼接字符串
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                // 8. 调用成功，接口调用次数 + 1 invokeCount
                                try {
                                    innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                } catch (Exception e) {
                                    log.error("invokeCount error", e);
                                }
                                //组装date
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                // 构建日志
                                StringBuilder sb2 = new StringBuilder(200);
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                //拿到响应结果  String result = "POST 用户名字是" + user.getUsername();
                                String data = new String(content, StandardCharsets.UTF_8);
                                sb2.append(data);
                                // 打印日志
                                log.info("响应结果：" + data);
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            // 9. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            // 降级处理返回数据
            // 6. 请求转发，调用模拟接口
            return chain.filter(exchange);
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }

    /**
     * 无权限
     * 403
     */
    public Mono<Void> handleNoAuth(ServerHttpResponse
                                           response){
        //先设置一个状态码，再拒绝
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }
    //500
    public Mono<Void> handleInvokeError(ServerHttpResponse
                                           response){
        //先设置一个状态码，再拒绝
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }
}