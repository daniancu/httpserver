package com.diancu.webserver.httpapi;

import org.junit.Assert;
import org.junit.Test;


public class HttpProtocolTest {

    @Test
    public void testHttpProtocol() {
        HttpProtocol protocol = new HttpProtocol("http/1.1");
        Assert.assertTrue(protocol.isVer1_1());
    }

}