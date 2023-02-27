package com.drools.DroolsDemo.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;

@Slf4j
public class LoggerUtil {

    public static void logValue(Object o) {
        System.out.println(o.toString());
    }

    @SneakyThrows
    public static boolean wait(int amount) {
        Thread.sleep(amount);
        return true;
    }


}