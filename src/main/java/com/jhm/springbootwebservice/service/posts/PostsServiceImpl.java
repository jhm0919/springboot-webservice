package com.jhm.springbootwebservice.service.posts;

import com.jhm.springbootwebservice.domain.postimage.PostsImageRepository;
import com.jhm.springbootwebservice.domain.posts.PostType;
import com.jhm.springbootwebservice.domain.posts.Posts;
import com.jhm.springbootwebservice.domain.postimage.PostsImage;
import com.jhm.springbootwebservice.domain.posts.PostsRepository;
import com.jhm.springbootwebservice.domain.user.User;
import com.jhm.springbootwebservice.domain.user.UserRepository;
import com.jhm.springbootwebservice.util.ImageUtil;
import com.jhm.springbootwebservice.web.dto.response.PostsListResponseDto;
import com.jhm.springbootwebservice.web.dto.response.PostsResponseDto;
import com.jhm.springbootwebservice.web.dto.request.PostsSaveRequestDto;
import com.jhm.springbootwebservice.web.dto.request.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostsServiceImpl implements PostsService{

    private final PostsRepository postsRepository;
    private final PostsImageRepository postsImageRepository;
    private final UserRepository userRepository;
    private final String PREV_IMAGE_URL = "C:/springboot-webservice/src/main/resources/static/files/";

    @Override
    @Transactional
    public Long save(Long userId, PostsSaveRequestDto requestDto, List<MultipartFile> multipartFiles) {
        User user = userRepository.findById(userId).orElseThrow(() ->
            new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        requestDto.setUser(user);

        Posts posts = requestDto.toEntity();

        saveImageFiles(multipartFiles, posts); // 이미지 저장

        return postsRepository.save(posts).getId();
    }

    @Override
    @Transactional
    public Long update(Long postId, PostsUpdateRequestDto requestDto, List<MultipartFile> multipartFiles, List<Long> checkedIds) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. postId=" + postId));

        posts.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getPostType()); // 게시글 수정

        saveImageFiles(multipartFiles, posts); // 이미지 저장

        deleteImageFiles(checkedIds, posts); // 이미지 삭제

        return postId;
    }

    private void saveImageFiles(List<MultipartFile> multipartFiles, Posts posts) {
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            for (MultipartFile file : multipartFiles) {
                UUID uuid = UUID.randomUUID();
                String originalName = file.getOriginalFilename();
                String imageFileName = uuid + "_" + originalName;

                File fileName = new File(PREV_IMAGE_URL, imageFileName);

                try {
                    file.transferTo(fileName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                PostsImage image = PostsImage.builder()
                    .url(imageFileName)
                    .name(originalName)
                    .posts(posts)
                    .build();

                postsImageRepository.save(image);
            }
        }
    }

    private void deleteImageFiles(List<Long> checkedIds, Posts posts) {
        if (checkedIds != null) { // 체크한 이미지(삭제하고싶은 이미지)가 있는 경우
            for (Long checkedId : checkedIds) {
                PostsImage postsImage = postsImageRepository.findByPostsIdAndId(posts.getId(), checkedId);
                String imageFileName = PREV_IMAGE_URL + postsImage.getUrl();
                File imageFileUrl = new File(imageFileName);

                if (imageFileUrl.delete()) {
                    postsImageRepository.delete(postsImage);
                }
            }
        }
    }

    @Override
    @Transactional
    public void updateView(Long id) {
        Posts posts = postsRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        posts.increaseView(); // 조회수 증가
        postsRepository.save(posts);
    }

    @Override
    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        return new PostsResponseDto(posts);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostsListResponseDto> findAll(Pageable pageable, String postType, String searchType, String searchKeyword) {
        Page<PostsListResponseDto> postsListResponseDto;

        if (postType != null) { // postType 선택 했을 때
            PostType type = PostType.valueOf(postType.toUpperCase()); // PostType으로 변환
            if (searchKeyword != null) { // postType + 검색조건
                postsListResponseDto = getPostsListPostTypeResponseDtos(pageable, searchType, searchKeyword, type);
            } else { // postType + 전체조회
                postsListResponseDto = entityToDto(postsRepository.findAllByPostType(type, pageable));
            }
        } else { // 카테고리 선택 안한 경우
            if (searchKeyword != null) { // 검색조건
                postsListResponseDto = getPostsListResponseDtos(pageable, searchType, searchKeyword);
            } else { // 전체조회
                postsListResponseDto = entityToDto(postsRepository.findAll(pageable));
            }
        }
        return postsListResponseDto;
    }

    private Page<PostsListResponseDto> getPostsListResponseDtos(Pageable pageable, String searchType, String searchKeyword) {
        Page<PostsListResponseDto> postsListResponseDto;
        switch (searchType) {
            default: // 검색조건 : 제목+내용
                postsListResponseDto = entityToDto(postsRepository.findByTitleContainingOrContentContaining(searchKeyword, searchKeyword, pageable));
                break;
            case "title": // 검색조건 : 제목
                postsListResponseDto = entityToDto(postsRepository.findByTitleContaining(searchKeyword, pageable));
                break;
            case "content": // 검색조건 : 내용
                postsListResponseDto = entityToDto(postsRepository.findByContentContaining(searchKeyword, pageable));
                break;
            case "author": // 검색조건 : 작성자
                postsListResponseDto = entityToDto(postsRepository.findByAuthorContaining(searchKeyword, pageable));
                break;
        }
        return postsListResponseDto;
    }

    private Page<PostsListResponseDto> getPostsListPostTypeResponseDtos(Pageable pageable, String searchType, String searchKeyword, PostType type) {
        Page<PostsListResponseDto> postsListResponseDto;
        switch (searchType) {
            default:  // 검색조건 : 전체
                postsListResponseDto = entityToDto(
                    postsRepository.findByPostTypeAndTitleContainingOrPostTypeAndContentContaining(type, searchKeyword, type, searchKeyword, pageable));
                break;
            case "title": // 검색조건 : 제목
                postsListResponseDto = entityToDto(
                    postsRepository.findByPostTypeAndTitleContaining(type, searchKeyword, pageable));
                break;
            case "content": // 검색조건 : 내용
                postsListResponseDto = entityToDto(
                    postsRepository.findByPostTypeAndContentContaining(type, searchKeyword, pageable));
                break;
            case "author": // 검색조건 : 작성자
                postsListResponseDto = entityToDto(
                    postsRepository.findByPostTypeAndAuthorContaining(type, searchKeyword, pageable));
                break;
        }
        return postsListResponseDto;
    }

    private Page<PostsListResponseDto> entityToDto(Page<Posts> postsPage) {
        return postsPage.map(PostsListResponseDto::new);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        postsRepository.delete(posts);
    }
}
