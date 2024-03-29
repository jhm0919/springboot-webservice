package com.jhm.springbootwebservice.web;

import com.jhm.springbootwebservice.config.auth.LoginUser;
import com.jhm.springbootwebservice.config.auth.dto.SessionUser;
import com.jhm.springbootwebservice.service.posts.PostsService;
import com.jhm.springbootwebservice.service.recommend.PostsRecommendService;
import com.jhm.springbootwebservice.service.recommend.RecommendService;
import com.jhm.springbootwebservice.web.dto.request.RecommendRequestDto;
import com.jhm.springbootwebservice.web.dto.response.PostsResponseDto;
import com.jhm.springbootwebservice.web.dto.request.PostsSaveRequestDto;
import com.jhm.springbootwebservice.web.dto.request.PostsUpdateRequestDto;
import com.jhm.springbootwebservice.web.dto.response.RecommendResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@RestController
public class PostsApiController {

    private final PostsService postsService;
    private final PostsRecommendService postsRecommendService;

    @PostMapping(value = "/posts", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Long save(@RequestPart("json_data") PostsSaveRequestDto postsSaveRequestDto,
                     @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles,
                     @LoginUser SessionUser user) {
        return postsService.save(user.getId(), postsSaveRequestDto, multipartFiles);
    }

    @GetMapping("/posts/{id}")
    public PostsResponseDto findById (@PathVariable Long id) {
        return postsService.findById(id);
    }

    @PutMapping(value = "/posts/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Long update(@PathVariable Long id,
                       @RequestPart("json_data") PostsUpdateRequestDto requestDto,
                       @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles) {
        return postsService.update(id, requestDto, multipartFiles);
    }

    @DeleteMapping("/posts/{id}")
    public Long delete(@PathVariable Long id) {
        postsRecommendService.delete(id);
        postsService.delete(id);
        return id;
    }

    @DeleteMapping("/posts/{postsId}/images/{id}")
    public Long deleteImage(@PathVariable Long postsId,
                            @PathVariable Long id) {
        postsService.deleteImage(postsId, id);
        return id;
    }

    @PutMapping("/posts/{postId}/recommend")
    public RecommendResponseDto recommend(@PathVariable Long postId, @LoginUser SessionUser user) {
        RecommendRequestDto requestDto = new RecommendRequestDto(postId, user.getId());

        RecommendResponseDto recommend = postsRecommendService.recommend(requestDto);
        int recommendUpCount = recommend.getRecommendUpCount();
        int recommendDownCount = recommend.getRecommendDownCount();
        boolean isRecommend = postsRecommendService.findById(requestDto).isRecommend();

        RecommendResponseDto recommendResponseDto =
            new RecommendResponseDto(isRecommend, recommendUpCount, recommendDownCount);
        return recommendResponseDto;
    }

    @PutMapping("/posts/{postId}/disRecommend")
    public RecommendResponseDto disRecommend(@PathVariable Long postId, @LoginUser SessionUser user) {
        RecommendRequestDto requestDto = new RecommendRequestDto(postId, user.getId());

        RecommendResponseDto recommend = postsRecommendService.disRecommend(requestDto);
        int recommendUpCount = recommend.getRecommendUpCount();
        int recommendDownCount = recommend.getRecommendDownCount();
        boolean isRecommend = postsRecommendService.findById(requestDto).isRecommend();

        RecommendResponseDto recommendResponseDto =
            new RecommendResponseDto(isRecommend, recommendUpCount, recommendDownCount);
        return recommendResponseDto;
    }
}
