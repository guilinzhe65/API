package com.yupi.project.provider;

/**
 * @author dio哒
 * @version 1.0
 * @date 2024/4/20 23:33
 */

import java.util.concurrent.CompletableFuture;

/**
 * 示例服务
 *
 */
public interface DemoService {

    String sayHello(String name);

    String sayHello2(String name);

    default CompletableFuture<String> sayHelloAsync(String name) {
        return CompletableFuture.completedFuture(sayHello(name));
    }

}

