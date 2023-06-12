package com.Udemy.YeoGiDa.domain.comment.repository;

import com.Udemy.YeoGiDa.domain.comment.entity.Comment;
import com.Udemy.YeoGiDa.domain.comment.response.CommentListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> findAllByPlaceByIdDesc(Long placeId);

    List<Comment> findAllByPlaceByIdAsc(Long placeId);

    Page<CommentListResponseDto> test(Long placeId, Pageable pageable,String condition);

    int totalSize(Long placeId);
}
