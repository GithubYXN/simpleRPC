package com.yangx.springboot_consumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 单元测试
 */
@SpringBootTest
public class ServiceImplTest {

    @Resource
    private ServiceImpl service;

    @Test
    public void test() {service.test();}
}
