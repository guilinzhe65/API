package com.example;

import com.yupi.yuapiclientsdk.client.YunApiClient;
import com.yupi.yuapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
class YunapiInterfaceApplicationTests {

    @Resource
    private YunApiClient yunApiClient;
    @Test
    void contextLoads() {
        String name = yunApiClient.getNameByGet("yupi");
        User user = new User();
        user.setUsername("zyq");
        System.out.println(name);
    }

}
