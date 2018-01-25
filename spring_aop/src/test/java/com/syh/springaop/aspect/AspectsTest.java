package com.syh.springaop.aspect;

import static org.mockito.Mockito.*;

import com.syh.springaop.test.TestInterface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AspectsTest {

    @SpyBean
    Aspects aspects;

    @SpyBean
    TestInterface obj;

    @Test
    public void test_doNothing() {
        obj.doNothing("");
        verify(aspects, only()).beforeDoNothing();
    }

    @Test
    public void test_throwException() {
        try {
            obj.throwException();
        } catch (RuntimeException e) {
            //do nothing
        }

        verify(aspects, only()).aroundGetThrowException(any());
    }


    @Test
    public void test_get() throws Throwable {
        obj.get(1);
        verify(aspects, only()).aroundGet(any());
    }

}
