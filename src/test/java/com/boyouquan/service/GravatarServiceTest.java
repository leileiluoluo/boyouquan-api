package com.boyouquan.service;

import com.boyouquan.util.CommonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GravatarServiceTest {

    @Autowired
    private GravatarService gravatarService;

    @Test
    public void testGetImage() {
        gravatarService.getImage("21f1e4d5eb03c7ced50a8270e48f1665", 80);
    }

    @Test
    public void testRefreshLocalImage() {
        gravatarService.refreshLocalImage("21f1e4d5eb03c7ced50a8270e48f1665", 80);
    }

}
