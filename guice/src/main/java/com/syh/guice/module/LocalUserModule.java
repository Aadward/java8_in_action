package com.syh.guice.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.syh.guice.other.Printer;
import com.syh.guice.annotation.Local;
import com.syh.guice.repository.LocalUserRepositoryImpl;
import com.syh.guice.repository.UserRepository;

public class LocalUserModule extends AbstractModule{

    /**
     * 配置映射关系，为参数为UserRepository且参数被标注为Local的引用注入LocalUserRepositoryImpl类.
     *
     * 注：除了用注解来标注之外，还可以指定名字{@link com.google.inject.name.Named}或者{@link javax.inject.Named}
     */
    @Override
    protected void configure() {
        bind(UserRepository.class).annotatedWith(Local.class).to(LocalUserRepositoryImpl.class);
    }
}
