package com.Udemy.YeoGiDa.domain.heart.repository;

import com.Udemy.YeoGiDa.domain.heart.entity.Heart;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.Udemy.YeoGiDa.domain.heart.entity.QHeart.heart;

@Repository
@RequiredArgsConstructor
public class HeartRepositoryImpl implements HeartRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Heart> findAllByMemberAndHeartFetch(Member m) {
        return queryFactory.selectFrom(heart)
                .where(heart.member.eq(m))
                .fetch();
    }
}
