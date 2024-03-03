package com.jhm.springbootwebservice.service.posts;

import com.jhm.springbootwebservice.domain.postimage.PostsImageRepository;
import com.jhm.springbootwebservice.domain.posts.Posts;
import com.jhm.springbootwebservice.domain.postimage.PostsImage;
import com.jhm.springbootwebservice.domain.posts.PostsRepository;
import com.jhm.springbootwebservice.domain.user.User;
import com.jhm.springbootwebservice.domain.user.UserRepository;
import com.jhm.springbootwebservice.web.dto.request.PostsImageRequestDto;
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
    public Long save(Long id, PostsSaveRequestDto requestDto, List<MultipartFile> multipartFiles) {
        User user = userRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        log.info("multipartFiles = {}", multipartFiles);

        requestDto.setUser(user);

        Posts result = requestDto.toEntity();

        saveImages(multipartFiles, result);

        return postsRepository.save(result).getId();
    }

    private void saveImages(List<MultipartFile> multipartFiles, Posts result) {
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            for (MultipartFile file : multipartFiles) {
                UUID uuid = UUID.randomUUID();
                String originalName = file.getOriginalFilename();
                String imageFileName = uuid + "_" + originalName;

                File uploadFolder = new File(PREV_IMAGE_URL);
                File destinationFile = new File(uploadFolder, imageFileName);

                try {
                    file.transferTo(destinationFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                PostsImage image = PostsImage.builder()
                        .url(imageFileName)
                        .name(originalName)
                        .posts(result)
                        .build();

                postsImageRepository.save(image);
            }
        }
    }


    @Override
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto, List<MultipartFile> multipartFiles) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        saveImages(multipartFiles, posts);
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    @Override
    @Transactional
    public int updateView(Long id) {
        return postsRepository.updateView(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostsListResponseDto> findAll(Pageable pageable) {
        Page<Posts> postsPages = postsRepository.findAll(pageable);
        Page<PostsListResponseDto> postsListResponseDto = postsPages.map(PostsListResponseDto::new);
        return postsListResponseDto;
    }


    @Override
    @Transactional
    public Page<PostsListResponseDto> postsSearch(String searchKeyword, Pageable pageable) {
        Page<Posts> posts = postsRepository.findByTitleContaining(searchKeyword, pageable);
        Page<PostsListResponseDto> postsListResponseDto = posts.map(PostsListResponseDto::new);
        return postsListResponseDto;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        postsRepository.delete(posts);
    }

    @Override
    public Long deleteImage(Long postsId, Long id) {
        PostsImage postsImage = postsImageRepository.findByPostsIdAndId(postsId, id);
        String imageFileName = PREV_IMAGE_URL + postsImage.getUrl();
        File imageFileUrl = new File(imageFileName);

        if (imageFileUrl.delete()) {
            postsImageRepository.delete(postsImage);
        }
        return id;
    }
}
