package com.babyfs;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class LogTest {
    @Test
    public void test1() {
        String name = "zhuanxu";
        String password = "123456";
        log.debug("debug");
        log.info("info");
        log.info("name: {}, password: {}",name,password);
        log.error("error log");
//        ch.qos.logback.classic.
//        org.slf4j.LoggerFactory
    }
}
