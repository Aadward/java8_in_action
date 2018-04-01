package com.syh.java8inaction;

import sun.misc.Launcher;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Arrays.stream(Launcher.getBootstrapClassPath().getURLs()).forEach(
                u -> System.out.println(u.toExternalForm())
        );

        //同样得到bootstrap classloader加载的类
        System.out.println(System.getProperty("sun.boot.class.path"));

        //得到Extension classloader
        ClassLoader extCl = new Object(){}.getClass().getEnclosingClass().getClassLoader().getParent();
        //Extension的parent不是普通的类加载器，是C++编写的，所以在JVM的虚拟机中无法找到它
        System.out.println(extCl.getParent());

        //得到APP classloader
        ClassLoader appCl = ClassLoader.getSystemClassLoader();


        //得到CL的classpath
        System.out.println("==========extcl===========");
        System.out.println(extCl.getResource(""));
        System.out.println("==========appcl===========");
        System.out.println(appCl.getResource(""));
    }
}
