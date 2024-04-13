package com.jhm.springbootwebservice.web;

import com.jhm.springbootwebservice.config.auth.LoginUser;
import com.jhm.springbootwebservice.config.auth.dto.SessionUser;
import com.jhm.springbootwebservice.domain.posts.PostType;
import com.jhm.springbootwebservice.service.posts.PostsService;
import com.jhm.springbootwebservice.web.dto.request.UserSearchDto;
import com.jhm.springbootwebservice.web.dto.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    private final PostsService postsService;

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "0") int myPost,
                        @RequestParam(required = false) PostType postType,
                        @LoginUser SessionUser user, // 어느 컨트롤러든지 @LoginUser만 사용하면 세션 정보를 가져올 수 있게 됨
                        @ModelAttribute("searchDto") UserSearchDto searchDto,
                        Model model) {
        String username = null;
        if (user != null) {
            username = user.getName();
        }
        Page<PostsListResponseDto> posts = postsService.findAll(postType, searchDto, page, myPost, username);

//        log.info("총 element 수 : {}, 전체 page 수 : {}, 페이지에 표시할 element 수 : {}, 현재 페이지 index : {}, 현재 페이지의 element 수 : {}",
//                posts.getTotalElements(), posts.getTotalPages(), posts.getSize(),
//                posts.getNumber(), posts.getNumberOfElements());

        model.addAttribute("postTypes", PostType.values());
        model.addAttribute("posts", posts);
        addSessionUserToModel(user, model);

        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave(Model model, @LoginUser SessionUser user) {
        addSessionUserToModel(user, model);
        model.addAttribute("postTypes", PostType.values());
        return "posts-save";
    }

    @GetMapping("/posts/read/{postId}")
    public String postsRead(@PathVariable Long postId, @LoginUser SessionUser user, Model model) {
        PostsResponseDto post = postsService.findById(postId);
        List<CommentResponseDto> comments = post.getComments();

        postsService.updateView(postId);

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

        return "posts-read";
    }

    @GetMapping("/posts/update/{postId}")
    public String postsUpdate(@PathVariable Long postId, @LoginUser SessionUser user, Model model) {

        PostsResponseDto post = postsService.findById(postId);
        List<PostsImageResponseDto> postsImages = post.getPostsImages();

        addSessionUserToModel(user, model);
        model.addAttribute("postsImages", postsImages);
        model.addAttribute("post", post);

        return "posts-update";
    }

    private void addSessionUserToModel(SessionUser user, Model model) {
        if (user != null) {
            model.addAttribute("user", user);
        }
    }

}