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
     * 在执行实现类的doNothing()方法之前会调用这个方法，@After同理。
     */
    @Before("execution(* com.syh.springaop.test.TestInterface.doNothing(String))")
    public void beforeDoNothing() {
        System.out.println("this is message test before doNothing");
    }

    /**
     * 抛出一个RuntimeException阻止目标方法throwException()的调用。
     */
    @Around("execution(* com.syh.springaop.test.TestInterface.throwException())")
    public void aroundGetThrowException(ProceedingJoinPoint joinPoint) {
        //因为在调用joinPoint.proceed()之前抛出了异常，所以该方法不会执行
        throw new RuntimeException("throwException() will not execute");
    }


    /**
     * joinPoint.proceed()显式调用目标方法，可以灵活地在调用方法之前、之后做操作。
     */
    @Around("execution(* com.syh.springaop.test.TestInterface.get(..))")
    public void aroundGet(ProceedingJoinPoint joinPoint) throws Throwable {
        //在get方法的之前做一些操作
        Object[] args = joinPoint.getArgs();
        System.out.println("Before calling joinPoint.proceed(), args=" + args[0]);
        String result = (String) joinPoint.proceed(joinPoint.getArgs());
        //在get方法调用后
        System.out.println("After calling joinPoint.proceed(), result=" + result);
    }

}
