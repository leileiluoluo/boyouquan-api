package com.boyouquan.helper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WhoisInfoHelperTest {

    @Autowired
    private WhoisInfoHelper whoisInfoHelper;

    @Test
    public void testGetDomainNameInfoByBlogDomainName() {
        whoisInfoHelper.getDomainNameInfoByRealDomainName("leileiluoluo.com");
    }

}
