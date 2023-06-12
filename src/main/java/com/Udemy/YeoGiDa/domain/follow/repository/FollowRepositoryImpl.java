package com.Udemy.YeoGiDa.domain.follow.repository;

import com.Udemy.YeoGiDa.domain.follow.entity.Follow;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.Udemy.YeoGiDa.domain.follow.entity.QFollow.follow;
import static com.Udemy.YeoGiDa.domain.member.entity.QMember.member;

@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom{

    private final JPAQueryFactory queryFactory;

//    @Override   //세타조인
//    public List<Follow> findAllByFromMember(Long memberId) {
//        return queryFactory.select(follow)
//                .from(follow, member)
//                .where(follow.fromMemberId.eq(memberId))
//                .fetch();
//    }
//
//    @Override
//    public List<Tuple> findAllByToMember(Long memberId) {
//        return queryFactory.select(follow, member)
//                .from(follow)
//                .leftJoin(member).on(follow.toMemberId.eq(member.id)).fetchJoin()
//                .where(follow.toMemberId.eq(memberId))
//                .fetch();
//    }

    @Override
    public int findSizeFollower(Long memberId) {
        return queryFactory.selectFrom(follow)
                .where(follow.fromMemberId.eq(memberId))
                .fetch().size();
    }

    @Override
    public int findSizeFollowing(Long memberId) {
        return queryFactory.selectFrom(follow)
                .where(follow.toMemberId.eq(memberId))
                .fetch().size();
    }

    @Override
    public List<Follow> findByMemberId(Long memberId) {
        return queryFactory.selectFrom(follow)
                .where(follow.toMemberId.eq(memberId).or(follow.fromMemberId.eq(memberId)))
                .fetch();
    }
}
