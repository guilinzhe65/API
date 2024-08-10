package com.yupi.yuapiclientsdk;

import com.yupi.yuapiclientsdk.client.YunApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * YunApi 客户端配置
 *
 * @author
 * @from
 */
@Configuration
@ConfigurationProperties("yunapi.client")
@Data
@ComponentScan
public class YunApiClientConfig {

    private String accessKey;
    private String secretKey;


    @Bean
    public YunApiClient yunApiClient() {
        return new YunApiClient(accessKey, secretKey);
    }

}
