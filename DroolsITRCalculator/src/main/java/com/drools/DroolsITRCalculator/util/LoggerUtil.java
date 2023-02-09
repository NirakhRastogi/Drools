package com.drools.DroolsITRCalculator.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerUtil {
  public static void logValue(Object o){
      log.info(o.toString());
  }
}