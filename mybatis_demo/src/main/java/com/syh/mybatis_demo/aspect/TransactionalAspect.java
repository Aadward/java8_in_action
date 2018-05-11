package com.syh.mybatis_demo.aspect;

import com.syh.mybatis_demo.dao.UserDao;
import com.syh.mybatis_demo.po.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 1. execution表达式中参数提取
 * 2. 在事务在Aspect上下文中的正确性
 */

@Aspect
@Component
public class TransactionalAspect {

    @Autowired
    private UserDao userDao;

    @Around("execution(* com.syh.mybatis_demo.dao.UserDao.addUser(..)) && args(user,..)")
    public Integer transactionalExecution(ProceedingJoinPoint joinPoint, User user) throws Throwable {
        System.out.println("In transactional aspect, user = " + user);
        try {
            userDao.addThenThrowException(user);
        } catch (Throwable t) {
            System.out.println(t.getMessage());
        }

        System.out.println("After run addThenThrowException, all users=" + userDao.getAllUsers());

        Integer ret = (Integer)joinPoint.proceed();

        System.out.println("After proceed by real method, all users=" + userDao.getAllUsers());

        return ret;
    }
}
