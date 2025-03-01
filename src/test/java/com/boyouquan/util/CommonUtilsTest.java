package com.boyouquan.util;

import org.junit.jupiter.api.Test;

public class CommonUtilsTest {

    @Test
    public void testMd5() {
        CommonUtils.md5("leileiluoluo@leileiluoluo.com");
    }

    @Test
    public void testGetRealWhoisDomainFromBlogDomainName() {
        System.out.println(CommonUtils.getRealWhoisDomainFromBlogDomainName("www.xiaoheiwa.com/1/2/3"));
    }

}
