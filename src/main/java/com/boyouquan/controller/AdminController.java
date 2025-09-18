package com.boyouquan.controller;

import com.boyouquan.enumration.ErrorCode;
import com.boyouquan.helper.BlogRequestFormHelper;
import com.boyouquan.model.*;
import com.boyouquan.service.*;
import com.boyouquan.util.LoginUtil;
import com.boyouquan.util.PermissionUtil;
import com.boyouquan.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private BlogRequestService blogRequestService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PinHistoryService pinHistoryService;
    @Autowired
    private BlogRequestFormHelper blogRequestFormHelper;
    @Autowired
    private ImageDownloadService imageDownloadService;
    @Autowired
    private PostImageService postImageService;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminLoginForm adminLoginForm) {
        // validation
        if (StringUtils.isBlank(adminLoginForm.getUsername())) {
            return ResponseUtil.errorResponse(ErrorCode.LOGIN_USERNAME_INVALID);
        }
        if (StringUtils.isBlank(adminLoginForm.getPassword())) {
            return ResponseUtil.errorResponse(ErrorCode.LOGIN_PASSWORD_INVALID);
        }

        boolean isUserValid = userService.isUsernamePasswordValid(adminLoginForm.getUsername(), adminLoginForm.getPassword());
        if (!isUserValid) {
            return ResponseUtil.errorResponse(ErrorCode.LOGIN_USERNAME_PASSWORD_INVALID);
        }

        // set session
        String sessionId = UUID.randomUUID().toString();
        LoginUtil.setSessionId(adminLoginForm.getUsername(), sessionId);

        // return
        LoginSuccess loginSuccess = new LoginSuccess(adminLoginForm.getUsername(), sessionId);
        return ResponseEntity.ok(loginSuccess);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String username = request.getHeader("username");

        // permission check
        if (!PermissionUtil.hasAdminPermission(request)) {
            return ResponseUtil.errorResponse(ErrorCode.UNAUTHORIZED);
        }

        // remove session
        LoginUtil.removeSessionId(username);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/permission-check")
    public ResponseEntity<?> permissionCheck(HttpServletRequest request) {
        // permission check
        if (!PermissionUtil.hasAdminPermission(request)) {
            return ResponseUtil.errorResponse(ErrorCode.UNAUTHORIZED);
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/blog-requests")
    public ResponseEntity<?> addBlogRequest(@RequestBody BlogRequestForm blogRequestForm, HttpServletRequest request) {
        // permission check
        if (!PermissionUtil.hasAdminPermission(request)) {
            return ResponseUtil.errorResponse(ErrorCode.UNAUTHORIZED);
        }

        ErrorCode error = blogRequestFormHelper.paramsValidation(blogRequestForm);
        if (null != error) {
            return ResponseUtil.errorResponse(error);
        }

        // submit
        BlogRequest blogRequest = new BlogRequest();
        blogRequest.setName(blogRequestForm.getName().trim());
        blogRequest.setDescription(blogRequestForm.getDescription().trim());
        blogRequest.setRssAddress(blogRequestForm.getRssAddress().trim());
        blogRequest.setAdminEmail(blogRequestForm.getAdminEmail().trim());
        blogRequest.setSelfSubmitted(false);
        blogRequestService.submit(blogRequest);

        executorService.execute(() -> blogRequestService.processNewRequest(blogRequest.getRssAddress()));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/blog-requests/{id}/uncollected")
    public ResponseEntity<?> uncollectedBlogById(@PathVariable("id") Long id, @RequestBody BlogUncollectedForm blogDeletedForm, HttpServletRequest request) {
        // permission check
        if (!PermissionUtil.hasAdminPermission(request)) {
            return ResponseUtil.errorResponse(ErrorCode.UNAUTHORIZED);
        }

        // get
        BlogRequest blogRequest = blogRequestService.getById(id);
        if (null != blogRequest) {
            blogRequestService.uncollectedByRssAddress(blogRequest.getRssAddress(), blogDeletedForm.getReason());
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/blog-requests/{id}")
    public ResponseEntity<?> deleteBlogRequestById(@PathVariable("id") Long id, HttpServletRequest request) {
        // permission check
        if (!PermissionUtil.hasAdminPermission(request)) {
            return ResponseUtil.errorResponse(ErrorCode.UNAUTHORIZED);
        }

        // get
        BlogRequest blogRequest = blogRequestService.getById(id);
        if (null != blogRequest) {
            blogRequestService.deleteByRssAddress(blogRequest.getRssAddress());
        }

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/blog-requests/{id}/approve")
    public ResponseEntity<?> approveBlogRequestById(@PathVariable("id") Long id, HttpServletRequest request) {
        // permission check
        if (!PermissionUtil.hasAdminPermission(request)) {
            return ResponseUtil.errorResponse(ErrorCode.UNAUTHORIZED);
        }

        // approve
        blogRequestService.approveById(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/blog-requests/{id}/reject")
    public ResponseEntity<?> rejectBlogRequestById(@PathVariable("id") Long id, @RequestBody BlogRequestRejectForm blogRequestRejectForm, HttpServletRequest request) {
        // permission check
        if (!PermissionUtil.hasAdminPermission(request)) {
            return ResponseUtil.errorResponse(ErrorCode.UNAUTHORIZED);
        }

        // reject
        if (StringUtils.isNotBlank(blogRequestRejectForm.getReason())) {
            blogRequestService.rejectById(id, blogRequestRejectForm.getReason());
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/recommended-posts")
    public ResponseEntity<?> recommendPostRequest(@RequestBody RecommendPostForm recommendPostForm, HttpServletRequest request) {
        // permission check
        if (!PermissionUtil.hasAdminPermission(request)) {
            return ResponseUtil.errorResponse(ErrorCode.UNAUTHORIZED);
        }

        Post post = postService.getByLink(recommendPostForm.getLink());
        if (null == post) {
            return ResponseUtil.errorResponse(ErrorCode.POST_NOT_EXISTS);
        }

        postService.recommendByLink(recommendPostForm.getLink());

        // after
        Blog blog = blogService.getByDomainName(post.getBlogDomainName());
        emailService.sendPostRecommendedNotice(blog, post);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/recommended-posts/unpin")
    public ResponseEntity<?> unpinPost(@RequestBody RecommendPostForm recommendPostForm, HttpServletRequest request) {
        // permission check
        if (!PermissionUtil.hasAdminPermission(request)) {
            return ResponseUtil.errorResponse(ErrorCode.UNAUTHORIZED);
        }

        String link = recommendPostForm.getLink();

        if (!postService.existsByLink(link)) {
            return ResponseUtil.errorResponse(ErrorCode.POST_NOT_EXISTS);
        }

        // unpin
        postService.unpinByLink(link);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/recommended-posts/pin")
    public ResponseEntity<?> pinPost(@RequestBody RecommendPostForm recommendPostForm, HttpServletRequest request) {
        // permission check
        if (!PermissionUtil.hasAdminPermission(request)) {
            return ResponseUtil.errorResponse(ErrorCode.UNAUTHORIZED);
        }

        String link = recommendPostForm.getLink();

        if (!postService.existsByLink(link)) {
            return ResponseUtil.errorResponse(ErrorCode.POST_NOT_EXISTS);
        }

        // pin
        postService.pinByLink(link);

        // after
        Post post = postService.getByLink(link);
        Blog blog = blogService.getByDomainName(post.getBlogDomainName());
        emailService.sendPostPinnedNotice(blog, post);

        // save pin history
        PinHistory pinHistory = new PinHistory();
        pinHistory.setBlogDomainName(post.getBlogDomainName());
        pinHistory.setLink(link);
        pinHistoryService.save(pinHistory);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/post-images")
    public ResponseEntity<?> addImage(@RequestBody PostImageForm form, HttpServletRequest request) {
        // permission check
        if (!PermissionUtil.hasAdminPermission(request)) {
            return ResponseUtil.errorResponse(ErrorCode.UNAUTHORIZED);
        }

        ImageDownloadResult result = imageDownloadService.downloadImage(form.getImageURL());
        if (!result.isSuccess()) {
            return ResponseUtil.errorResponse(ErrorCode.POST_IMAGE_ADD_FAILED, result.getMessage());
        }

        // add
        postImageService.saveOrUpdate(form.getLink(), form.getImageURL(), result);

        return ResponseEntity.noContent().build();
    }

}
