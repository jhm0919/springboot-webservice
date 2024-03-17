package com.jhm.springbootwebservice.web;

import com.jhm.springbootwebservice.config.auth.LoginUser;
import com.jhm.springbootwebservice.config.auth.dto.SessionUser;
import com.jhm.springbootwebservice.domain.posts.PostType;
import com.jhm.springbootwebservice.service.posts.PostsService;
import com.jhm.springbootwebservice.service.recommend.RecommendService;
import com.jhm.springbootwebservice.web.dto.response.PostsImageResponseDto;
import com.jhm.springbootwebservice.web.dto.response.PostsListResponseDto;
import com.jhm.springbootwebservice.web.dto.response.PostsResponseDto;
import com.jhm.springbootwebservice.web.dto.response.CommentResponseDto;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user, // 어느 컨트롤러든지 @LoginUser만 사용하면 세션 정보를 가져올 수 있게 됨
                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        String searchKeyword,
                        String searchType,
                        String postType) {

        Page<PostsListResponseDto> posts;

        if (StringUtils.isBlank(postType)) {
            postType = null;
        }
        posts = postsService.findAll(pageable, postType, searchType, searchKeyword);

        int nowPage = posts.getPageable().getPageNumber() + 1; //pageable에서 넘어온 현재페이지를 가지고올수있다 * 0부터시작하니까 +1
        int startPage = Math.max(nowPage - 4, 1); //매개변수로 들어온 두 값을 비교해서 큰값을 반환
        int endPage = Math.min(nowPage + 5, posts.getTotalPages());

        model.addAttribute("postTypes", PostType.values());
        model.addAttribute("posts", posts);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        getUser(user, model);

        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave(Model model, @LoginUser SessionUser user) {
        getUser(user, model);
        return "posts-save";
    }

    @GetMapping("/posts/read/{id}")
    public String postsRead(@PathVariable Long id, @LoginUser SessionUser user, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        List<CommentResponseDto> comments = dto.getComments();
        List<PostsImageResponseDto> postsImages = dto.getPostsImages();
        model.addAttribute("postsImages", postsImages);

        if (comments != null && !comments.isEmpty()) {
            model.addAttribute("comments", comments);
        }

        if (user != null) {
            model.addAttribute("user", user);

            /** 게시글 작성자 본인인지 확인 */
            if (dto.getUserId().equals(user.getId())) {
                model.addAttribute("author", true);
            }
        }

        postsService.updateView(id); // view++

        model.addAttribute("post", dto);

        return "posts-read";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, @LoginUser SessionUser user, Model model) {
        getUser(user, model);
        PostsResponseDto dto = postsService.findById(id);
        List<PostsImageResponseDto> postsImages = dto.getPostsImages();
        model.addAttribute("postsImages", postsImages);
        model.addAttribute("post", dto);
        return "posts-update";
    }

    private void getUser(SessionUser user, Model model) {
        if (user != null) {
            model.addAttribute("user", user);
        }
    }

}
