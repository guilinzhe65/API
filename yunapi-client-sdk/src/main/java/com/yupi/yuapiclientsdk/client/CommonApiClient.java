package com.yupi.yuapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import com.yupi.yuapiclientsdk.utils.SignUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * @author dio哒
 * @version 1.0
 * @date 2024/5/6 18:56
 */
public class CommonApiClient {
    //由调用处携带accessKey和secretKey
    protected final String accessKey;
    protected final String secretKey;
    //网关地址
    protected static final String GATEWAY_HOST = "http://localhost:8100";

    public CommonApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * 负责将数字签名的相关参数填入请求头中
     * @param body
     * @param
     * @param
     * @return
     */
    protected Map<String, String> getHeaderMap(String body,String accessKey, String secretKey) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        // 一定不能直接发送
        // hashMap.put("secretKey", secretKey);
        //加随机数
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        //body就是用户参数，用来和secretKey生成sign
        hashMap.put("body", body);
        //加时间戳
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", SignUtils.genSign(body, secretKey));
        return hashMap;
    }
}
