package com.boyouquan.controller;

import com.boyouquan.model.Blog;
import com.boyouquan.model.PlanetShuttle;
import com.boyouquan.model.PlanetShuttleGo;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.PlanetShuttleService;
import com.boyouquan.util.CommonUtils;
import com.boyouquan.util.IPUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/planet-shuttle")
public class PlanetShuttleController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private PlanetShuttleService planetShuttleService;

    @GetMapping("")
    public ResponseEntity<PlanetShuttleGo> shuttle(HttpServletRequest request, Model model) {
        String referer = request.getHeader("From");
        Blog fromBlog = null;
        if (StringUtils.isNotBlank(referer)) {
            String fromDomainName = CommonUtils.getDomainFromURL(referer);
            fromBlog = blogService.getByShortDomainName(fromDomainName);
        }

        Blog blog = blogService.listByRandom(Collections.emptyList(), false, 1).stream().findFirst().get();

        // save planet shuttle
        long fromBlogInitiatedCount = 0;
        if (null != fromBlog) {
            String ip = IPUtil.getRealIp(request);

            PlanetShuttle planetShuttle = new PlanetShuttle();
            planetShuttle.setIp(ip);
            planetShuttle.setBlogDomainName(fromBlog.getDomainName());
            planetShuttle.setToBlogAddress(blog.getAddress());
            planetShuttleService.save(planetShuttle, referer, fromBlog, blog);

            fromBlogInitiatedCount = planetShuttleService.countInitiatedByBlogDomainName(fromBlog.getDomainName());
        }

        // return
        PlanetShuttleGo planetShuttleGo = new PlanetShuttleGo();
        planetShuttleGo.setBlogName(blog.getName());
        planetShuttleGo.setBlogAddress(blog.getAddress());
        planetShuttleGo.setBlogDescription(blog.getDescription());
        planetShuttleGo.setFromBlog(fromBlog);
        planetShuttleGo.setFromBlogInitiatedCount(fromBlogInitiatedCount);

        return ResponseEntity.ok(planetShuttleGo);
    }

}
