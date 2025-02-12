package com.jhm.springbootwebservice.web;

import com.jhm.springbootwebservice.config.auth.LoginUser;
import com.jhm.springbootwebservice.config.auth.dto.SessionUser;
import com.jhm.springbootwebservice.domain.post.PostType;
import com.jhm.springbootwebservice.service.comment.CommentService;
import com.jhm.springbootwebservice.service.post.PostService;
import com.jhm.springbootwebservice.web.dto.request.UserSearchDto;
import com.jhm.springbootwebservice.web.dto.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostService postService;

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "0") int myPost,
                        @RequestParam(required = false) PostType postType,
                        @LoginUser SessionUser user, // 어느 컨트롤러든지 @LoginUser만 사용하면 세션 정보를 가져올 수 있게 됨
                        @ModelAttribute("searchDto") UserSearchDto searchDto,
                        Model model) {
        Long userId = null;
        if (user != null) {
            model.addAttribute("user", user);
            userId = user.getId();
        }
        Page<PostListResponseDto> posts = postService.findAll(postType, searchDto, page, myPost, userId);

        model.addAttribute("postTypes", PostType.values());
        model.addAttribute("posts", posts);

        return "index";
    }

    @GetMapping("/post/save")
    public String postSave(Model model, @LoginUser SessionUser user) {
        addSessionUserToModel(user, model);
        model.addAttribute("postTypes", PostType.values());
        return "post-save";
    }

    @GetMapping("/post/read/{postId}")
    public String postRead(@PathVariable Long postId, @LoginUser SessionUser user, Model model) {
        PostResponseDto post = postService.findById(postId);
        List<CommentResponseDto> comments = post.getComments();

        postService.updateView(postId);

        if (user != null) {
            model.addAttribute("user", user);

            /** 게시글 작성자 본인인지 확인 */
            if (post.getUserId().equals(user.getId())) {
                model.addAttribute("author", true);
            }
        }

        if (!comments.isEmpty()) {
            model.addAttribute("comments", comments);
        }

        model.addAttribute("postTypes", PostType.values());
        model.addAttribute("post", post);


        return "post-read";
    }

    @GetMapping("/post/update/{postId}")
    public String postUpdate(@PathVariable Long postId, @LoginUser SessionUser user, Model model) {

        PostResponseDto post = postService.findById(postId);
        List<PostImageResponseDto> postImages = post.getPostImages();

        addSessionUserToModel(user, model);
        model.addAttribute("postImages", postImages);
        model.addAttribute("post", post);

        return "post-update";
    }

    private void addSessionUserToModel(SessionUser user, Model model) {
        if (user != null) {
            model.addAttribute("user", user);
        }
    }

}