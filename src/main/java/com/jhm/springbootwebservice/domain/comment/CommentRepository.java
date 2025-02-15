package com.jhm.springbootwebservice.domain.comment;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT COALESCE(MAX(c.depthNo), 0) FROM Comment c WHERE c.groupNo = :groupNo AND c.level = :level")
    int findMaxDepthNoByGroupNoAndLevel(@Param("groupNo") Long groupNo, @Param("level") int level);

    // 같은레벨 이면서 parentId가 같은 depthNo의 최댓값
    @Query("SELECT COALESCE(MAX(c.depthNo), 0) FROM Comment c WHERE c.groupNo = :groupNo AND c.parent.id = :parentId AND c.level = :level")
    int findMaxDepthNoByGroupNoAndParentIdAndLevel(@Param("groupNo") Long groupNo, @Param("parentId") Long parentId, @Param("level") int level);

    @Modifying
    @Query("UPDATE Comment c SET c.depthNo = c.depthNo + 1 WHERE c.groupNo = :groupNo AND c.depthNo >= :depthNo")
    void incrementDepthNoByGroupNo(@Param("groupNo") Long groupNo, @Param("depthNo") int depthNo);

    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.post.id = :postId ORDER BY c.groupNo ASC, c.depthNo ASC")
    List<Comment> findCommentsWithUserByPostIdSorted(@Param("postId") Long postId);

    Comment findByPostIdAndId(Long postId, Long id);
}
