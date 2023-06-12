

package com.Udemy.YeoGiDa.domain.comment.repository;

import com.Udemy.YeoGiDa.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long>,CommentRepositoryCustom {

    @Override
    Optional<Comment> findById(Long commentId);


}

