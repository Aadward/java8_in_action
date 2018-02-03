package com.syh.guice.module;

import com.google.inject.AbstractModule;
import com.syh.guice.annotation.Remote;
import com.syh.guice.repository.RemoteUserRepository;
import com.syh.guice.repository.UserRepository;

public class RemoteUserModule extends AbstractModule{

    /**
     * 配置映射关系，为参数为UserRepository且参数被标注为Remote的引用注入RemoteUserRepositoryImpl类
     */
    @Override
    protected void configure() {
        bind(UserRepository.class).annotatedWith(Remote.class).to(RemoteUserRepository.class);
    }
}
