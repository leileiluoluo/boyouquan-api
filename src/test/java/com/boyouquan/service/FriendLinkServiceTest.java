//package com.boyouquan.service;
//
//import com.boyouquan.model.Blog;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@SpringBootTest
//public class FriendLinkServiceTest {
//
//    @Autowired
//    private BlogService blogService;
//    @Autowired
//    private FriendLinkService friendLinkService;
//
//    @Test
//    public void testDetectFriendLinks() {
//        List<Blog> blogs = blogService.listAll();
//
//        Set<String> blogAddresses = blogs.stream()
//                .map(Blog::getAddress)
//                .collect(Collectors.toSet());
//
//        Blog blog = new Blog();
//        blog.setDomainName("leileiluoluo.com");
//        blog.setAddress("https://leileiluoluo.com/");
//
//        friendLinkService.detectFriendLinks(blogAddresses, blog);
//    }
//
//}
