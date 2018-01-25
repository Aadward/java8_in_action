package com.syh.springaop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Aspects {

    /**
     * 在执行实现类的doNothing()方法之前会调用这个方法。@After同理
     */
    @Before("execution(* com.syh.springaop.test.TestInterface.doNothing(String))")
    public void beforeDoNothing() {
        System.out.println("this is message test before doNothing");
    }

    /**
     * 更加灵活地控制在何时调用，这个调用和doNothing()达到一样的效果，并且抛出
     * 一个RuntimeException阻止真正的doNothing()方法调用.
     */
    @Around("execution(* com.syh.springaop.test.TestInterface.doNothing(String))")
    public void aroundDoNothingThrowException(ProceedingJoinPoint joinPoint) {
        aroundBefore();
        throw new RuntimeException("doNothing() will not execute");
    }

    @Around("execution(* com.syh.springaop.test.TestInterface.doNothing(String))")
    public void aroundSaveNormal(ProceedingJoinPoint joinPoint) throws Throwable {
        aroundBefore();
        joinPoint.proceed(joinPoint.getArgs());
        aroundAfter();
    }


    /**
     * 这个方法用于测试Around在调用proceed()之前运行情况
     */

    public void aroundBefore() {
        //do nothing
    }

    /**
     * 这个方法用于测试Around在调用proceed()之后运行情况
     */
    public void aroundAfter() {

    }


}
