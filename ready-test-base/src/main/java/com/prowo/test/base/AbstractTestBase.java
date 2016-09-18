package com.prowo.test.base;

import com.prowo.test.mock.HttpServletRequestMock;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
public abstract class AbstractTestBase {

    @Resource(name = "request")
    protected HttpServletRequestMock request;
    protected String requestJsonData;

    public void beforeTest() {
    }

    public void afterTest() {
    }

    protected abstract void post();

    protected abstract void dump();

    public abstract void testNoData();

    public abstract void testOK();
}