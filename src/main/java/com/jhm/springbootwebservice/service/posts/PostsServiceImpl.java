package com.jhm.springbootwebservice.service.posts;

import com.jhm.springbootwebservice.domain.postimage.PostsImageRepository;
import com.jhm.springbootwebservice.domain.posts.PostType;
import com.jhm.springbootwebservice.domain.posts.Posts;
import com.jhm.springbootwebservice.domain.postimage.PostsImage;
import com.jhm.springbootwebservice.domain.posts.PostsRepository;
import com.jhm.springbootwebservice.domain.user.User;
import com.jhm.springbootwebservice.domain.user.UserRepository;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostsServiceImpl implements PostsService{

    private final PostsRepository postsRepository;
    private final UserRepository userRepository;
    private String tempLocation = "C:\\springboot-webservice\\src\\main\\resources\\static\\temp\\";
    private String localLocation = "C:\\springboot-webservice\\src\\main\\resources\\static\\files\\";

    @Override
    @Transactional
    public Long save(Long userId, PostsSaveRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
            new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        String content = moveImg(requestDto.getContent());// content를 넘겨서 이미지 경로 이동과 내용물 변경
        
        requestDto.setUser(user);
        requestDto.setContent(content);

        Posts posts = requestDto.toEntity();

        return postsRepository.save(posts).getId();
    }

    private String moveImg(String content) {

        Pattern nonValidPattern = Pattern
                .compile("(?i)< *[IMG][^\\>]*[src] *= *[\"\']{0,1}([^\"\'\\ >]*)");
        Matcher matcher = nonValidPattern.matcher(content);
        String img;
        while (matcher.find()) {
            img = matcher.group(1);
            log.info("asdf={}", img);
            img = img.replace("/temp", ""); // 이미지 이름만 추출
            content = content.replace("/temp", "/files"); // content에 있는 temp를 files로 교체
            File file = new File(tempLocation + img);
            file.renameTo(new File(localLocation + img));// 실제 저장 경로로 이동
        }
        return content;
    }

    @Override
    @Transactional
    public Long update(Long postId, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. postId=" + postId));

        String content = moveImg(requestDto.getContent());

        posts.update(requestDto.getTitle(), content, requestDto.getPostType()); // 게시글 수정

        // todo: 수정시 넘어온 이미지 제외 지우기?

        return postId;
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
        
        String content = posts.getContent();

        deleteImg(content); // 이미지 삭제
        
        postsRepository.delete(posts);
    }

    private void deleteImg(String content) {
        Pattern nonValidPattern = Pattern
                .compile("(?i)< *[IMG][^\\>]*[src] *= *[\"\']{0,1}([^\"\'\\ >]*)");
        Matcher matcher = nonValidPattern.matcher(content);
        String img;
        while (matcher.find()) {
            img = matcher.group(1);
            img = img.replace("/files", ""); // 이미지 이름만 추출
            File file = new File(localLocation + img);
            file.delete();
        }
    }
}
