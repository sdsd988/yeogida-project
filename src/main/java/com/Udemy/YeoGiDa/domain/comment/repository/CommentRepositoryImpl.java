package com.Udemy.YeoGiDa.domain.comment.repository;

import com.Udemy.YeoGiDa.domain.comment.entity.Comment;
import com.Udemy.YeoGiDa.domain.comment.response.CommentListResponseDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import java.util.List;

import static com.Udemy.YeoGiDa.domain.comment.entity.QComment.*;
import static com.Udemy.YeoGiDa.domain.place.entity.QPlace.place;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findAllByPlaceByIdDesc(Long placeId) {
        return queryFactory.selectFrom(comment)
                .where(comment.place.id.eq(placeId))
                .orderBy(comment.id.desc())
                .fetch();
    }

    @Override
    public List<Comment> findAllByPlaceByIdAsc(Long placeId) {
        return queryFactory.selectFrom(comment)
                .where(comment.place.id.eq(placeId))
                .orderBy(comment.id.asc())
                .fetch();
    }

    @Override
    public Page<CommentListResponseDto> test(Long placeId, Pageable pageable,String condition) {

        List<CommentListResponseDto> ResponseList = queryFactory.select(Projections.fields(
                        CommentListResponseDto.class,
                        comment.id.as("commentId"),
                        comment.member.id.as("memberId"),
                        comment.member.memberImg.imgUrl,
                        comment.member.nickname,
                        comment.createdTime,
                        comment.content
                ))
                .from(comment)
                .where(comment.place.id.eq(placeId))
                .orderBy(conditionParam(condition))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int totalSize = queryFactory.selectFrom(comment)
                .where(comment.place.id.eq(placeId))
                .fetch().size();

        return new PageImpl<>(ResponseList, pageable, totalSize);
    }

    @Override
    public int totalSize(Long placeId) {
        return queryFactory.selectFrom(comment)
                .where(comment.place.id.eq(placeId))
                .fetch().size();
    }

    // cursor의 위치를 기준점으로
    private OrderSpecifier conditionParam(String condition) {
        if (condition.equals("desc")) {
            return place.id.desc();
        } else if (condition.equals("asc")) {
            return place.id.asc();
        } throw new IllegalArgumentException();
    }
}

