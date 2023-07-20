package com.zic.springboot.demo.zicCoolApp.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

//Smaller number in order represent higher order.
@Aspect
@Component
@Order(1)
public class LoggingAspect {
    @AfterReturning(pointcut = "execution(* com.zic.springboot.demo.zicCoolApp.rest.MyRestController.test1 (..))", returning = "result")
    public void logAfterReturning(Object result) {
        System.out.println("Method execution returned: " + result);
    }
}
