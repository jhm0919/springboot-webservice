package com.jhm.springbootwebservice.web;

import com.jhm.springbootwebservice.config.auth.LoginUser;
import com.jhm.springbootwebservice.config.auth.dto.SessionUser;
import com.jhm.springbootwebservice.service.posts.PostsService;
import com.jhm.springbootwebservice.service.recommend.CommentsRecommendService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@RestController
public class PostsApiController {

    private final PostsService postsService;
    private final PostsRecommendService postsRecommendService;
    private final CommentsRecommendService commentsRecommendService;

    @PostMapping(value = "/posts", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Long save(@RequestPart("json_data") PostsSaveRequestDto postsSaveRequestDto,
                     @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles,
                     @LoginUser SessionUser user) {
        Long postId = postsService.save(user.getId(), postsSaveRequestDto, multipartFiles);

        return postId;
    }

    @GetMapping("/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id) {
        return postsService.findById(id);
    }

    @PutMapping(value = "/posts/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Long update(@PathVariable Long id,
                       @RequestPart("json_data") PostsUpdateRequestDto requestDto,
                       @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles,
                       @RequestParam(value = "checkedImageIds", required = false) String checkedImageIds) {

        List<Long> checkedIds = null;

        if (checkedImageIds != null) { // 체크된 이미지가 있을 경우에만 값이 있음
            checkedIds = Arrays.stream(checkedImageIds.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            log.info("checkedImageIds={}", checkedIds);
        }

        return postsService.update(id, requestDto, multipartFiles, checkedIds);
    }

    @DeleteMapping("/posts/{id}")
    public Long delete(@PathVariable Long id) {
        postsRecommendService.delete(id); // 게시글 추천, 비추천 삭제
        commentsRecommendService.delete(id); // 댓글에 있는 추천, 비추천 삭제
        postsService.delete(id);
        return id;
    }

    @PutMapping("/posts/{postId}/recommend")
    public RecommendResponseDto recommend(@PathVariable Long postId, @LoginUser SessionUser user) {
        RecommendRequestDto requestDto = new RecommendRequestDto(postId, user.getId());

        RecommendResponseDto recommend = postsRecommendService.recommend(requestDto);
        return recommend;
    }

    @PutMapping("/posts/{postId}/disRecommend")
    public RecommendResponseDto disRecommend(@PathVariable Long postId, @LoginUser SessionUser user) {
        RecommendRequestDto requestDto = new RecommendRequestDto(postId, user.getId());

        RecommendResponseDto recommend = postsRecommendService.disRecommend(requestDto);
        return recommend;
    }
}
