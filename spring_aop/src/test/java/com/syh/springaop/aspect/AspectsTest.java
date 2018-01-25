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
    TestInterface dao;

    @Test
    public void test_beforeDoNothing() {
        dao.doNothing("");
        verify(aspects, times(1)).beforeDoNothing();
    }

    @Test
    public void test_aroundSaveThrowException() {
        try {
            dao.doNothing("");
        } catch (RuntimeException e) {
            //do nothing
        }

        verify(aspects, times(1)).aroundDoNothingThrowException(any());
        verify(dao, never()).doNothing(anyString());
    }
}
