package com.syh.guice;

import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.syh.guice.model.User;
import com.syh.guice.module.LocalUserModule;
import com.syh.guice.module.RemoteUserModule;
import com.syh.guice.repository.LocalUserRepositoryImpl;
import com.syh.guice.repository.RemoteUserRepository;
import com.syh.guice.repository.UserRepository;
import com.syh.guice.service.LocalUserServiceImpl;
import com.syh.guice.service.RemoteUserService;
import com.syh.guice.service.UserService;
import org.junit.Assert;
import org.junit.Test;

public class GuiceTest {

    @Test
    public void localUserTest() {
        UserRepository userRepository = new LocalUserRepositoryImpl();
        User expect = userRepository.getUserById(1L);

        Injector injector = Guice.createInjector(new LocalUserModule());
        UserService service = injector.getInstance(LocalUserServiceImpl.class);
        User actual = service.getUser(1L);

        Assert.assertEquals(expect, actual);
    }

    @Test
    public void remoteUserTest() {
        UserRepository userRepository = new RemoteUserRepository();
        User expect = userRepository.getUserById(2L);

        Injector injector = Guice.createInjector(new RemoteUserModule());
        UserService service = injector.getInstance(RemoteUserService.class);
        User actual = service.getUser(2L);

        Assert.assertEquals(expect, actual);
    }

    @Test(expected = ConfigurationException.class)
    public void inject_localRepository_to_remoteService() {
        Injector injector = Guice.createInjector(new LocalUserModule());
        UserService service = injector.getInstance(RemoteUserService.class);
    }

}
