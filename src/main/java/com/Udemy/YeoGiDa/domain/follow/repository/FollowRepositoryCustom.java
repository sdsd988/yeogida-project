package com.Udemy.YeoGiDa.domain.follow.repository;

import com.Udemy.YeoGiDa.domain.follow.entity.Follow;
import com.querydsl.core.Tuple;

import java.util.List;

public interface FollowRepositoryCustom {

    int findSizeFollower(Long memberId);

    int findSizeFollowing(Long memberId);

    List<Follow> findByMemberId(Long memberId);
}
