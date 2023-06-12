package com.Udemy.YeoGiDa.domain.trip.repository;

import com.Udemy.YeoGiDa.domain.follow.exception.NoOneFollowException;
import com.Udemy.YeoGiDa.domain.follow.repository.FollowRepository;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.entity.QMemberImg;
import com.Udemy.YeoGiDa.domain.trip.entity.QTripImg;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.Udemy.YeoGiDa.domain.member.entity.QMember.member;
import static com.Udemy.YeoGiDa.domain.trip.entity.QTrip.trip;

@RequiredArgsConstructor
public class TripRepositoryImpl implements TripRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final FollowRepository followRepository;

    @Override
    public List<Trip> findAllByConditionFetch(String condition) {
        return queryFactory.selectFrom(trip)
                .leftJoin(trip.tripImg, QTripImg.tripImg).fetchJoin()
                .leftJoin(trip.member, member).fetchJoin()
                .leftJoin(member.memberImg, QMemberImg.memberImg).fetchJoin()
                .orderBy(conditionParam(condition))
                .fetch();
    }

    /**
     * id로 맵을 만들어서 사용
     */
    @Override
    public List<Trip> findAllByRegionAndConditionFetch(String region, String condition) {
        return queryFactory.selectFrom(trip)
                .leftJoin(trip.tripImg, QTripImg.tripImg).fetchJoin()
                .leftJoin(trip.member, member).fetchJoin()
                .leftJoin(member.memberImg, QMemberImg.memberImg).fetchJoin()
                .where(trip.region.eq(region))
                .orderBy(conditionParam(condition))
                .fetch();
    }

    private OrderSpecifier conditionParam(String condition) {
        if (StringUtils.isNullOrEmpty(condition)) {
            return trip.id.desc();
        } else if (condition.equals("id")) {
            return trip.id.desc();
        } else if (condition.equals("heart")) {
            return trip.hearts.size().desc();
        } throw new IllegalArgumentException();
    }

    @Override
    public List<Trip> findAllOrderByChangeHeartCountBasicFetch() {
        return queryFactory.selectFrom(trip)
                .leftJoin(trip.tripImg, QTripImg.tripImg).fetchJoin()
                .leftJoin(trip.member, member).fetchJoin()
                .leftJoin(member.memberImg, QMemberImg.memberImg).fetchJoin()
                .where(trip.changeHeartCount.gt(0))
                .orderBy(trip.changeHeartCount.desc(), trip.id.desc())
                .limit(10)
                .fetch();
    }

    @Override
    public List<Trip> findAllOrderByChangeHeartCountMoreFetch() {
        return queryFactory.selectFrom(trip)
                .leftJoin(trip.tripImg, QTripImg.tripImg).fetchJoin()
                .leftJoin(trip.member, member).fetchJoin()
                .leftJoin(member.memberImg, QMemberImg.memberImg).fetchJoin()
                .where(trip.changeHeartCount.gt(0))
                .orderBy(trip.changeHeartCount.desc(), trip.id.desc())
                .fetch();
    }

    @Override
    public List<Trip> findAllByMemberFetch(Member m) {
        return queryFactory.selectFrom(trip)
                .leftJoin(trip.tripImg, QTripImg.tripImg).fetchJoin()
                .leftJoin(trip.member, member).fetchJoin()
                .leftJoin(member.memberImg, QMemberImg.memberImg).fetchJoin()
                .where(trip.member.id.eq(m.getId()))
                .orderBy(trip.id.desc())
                .fetch();
    }

    @Override
    public List<Trip> findAllByMemberIdFetch(Long memberId,String tag, String condition) {
        return queryFactory.selectFrom(trip)
                .leftJoin(trip.tripImg, QTripImg.tripImg).fetchJoin()
                .leftJoin(trip.member, member).fetchJoin()
                .leftJoin(member.memberImg, QMemberImg.memberImg).fetchJoin()
                .where(trip.member.id.eq(memberId),whereCondition(tag))
                .orderBy(conditionParam(condition))
                .fetch();
    }

    @Override
    public List<Trip> findAllByMemberIdFetch2(Long memberId, String condition) {
        return queryFactory.selectFrom(trip)
                .leftJoin(trip.tripImg, QTripImg.tripImg).fetchJoin()
                .leftJoin(trip.member, member).fetchJoin()
                .leftJoin(member.memberImg, QMemberImg.memberImg).fetchJoin()
                .where(trip.member.id.eq(memberId))
                .orderBy(conditionParam(condition))
                .fetch();
    }

    @Override
    public List<Trip> findAllTripSearchAndSort(String keyword, String condition) {
        return queryFactory.selectFrom(trip)
                .leftJoin(trip.tripImg, QTripImg.tripImg).fetchJoin()
                .leftJoin(trip.member, member).fetchJoin()
                .leftJoin(member.memberImg, QMemberImg.memberImg).fetchJoin()
                .where(trip.title.contains(keyword).or(trip.subTitle.contains(keyword)))
                .orderBy(conditionParam(condition))
                .fetch();
    }

    @Override
    public List<Trip> findAllByFollowingOrderByIdBasicFetch(Member m) {
        List<Long> memberIdsByFromMemberId = followRepository.findMemberIdsByFromMemberId(m.getId());
        if(CollectionUtils.isEmpty(memberIdsByFromMemberId)) {
            throw new NoOneFollowException();
        }

        return queryFactory.selectFrom(trip)
                .where(trip.member.id.in(memberIdsByFromMemberId))
                .orderBy(trip.id.desc())
                .limit(10)
                .fetch();
    }

    @Override
    public List<Trip> findAllByFollowingOrderByIdMoreFetch(Member m) {
        List<Long> memberIdsByFromMemberId = followRepository.findMemberIdsByFromMemberId(m.getId());
        if(CollectionUtils.isEmpty(memberIdsByFromMemberId)) {
            throw new NoOneFollowException();
        }

        return queryFactory.selectFrom(trip)
                .where(trip.member.id.in(memberIdsByFromMemberId))
                .orderBy(trip.id.desc())
                .fetch();
    }

    @Override
    public List<Trip> findRecentTrip() {
        return queryFactory.selectFrom(trip)
                .leftJoin(trip.tripImg, QTripImg.tripImg).fetchJoin()
                .leftJoin(trip.member, member).fetchJoin()
                .leftJoin(member.memberImg, QMemberImg.memberImg).fetchJoin()
                .orderBy(trip.id.desc())
                .limit(20)
                .fetch();
    }

    private BooleanExpression whereCondition(String tag) {
        if(tag.equals("nothing")){
            return null;
        } return trip.region.eq(tag);
    }

}
