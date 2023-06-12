package com.Udemy.YeoGiDa.domain.member.repository;

import com.Udemy.YeoGiDa.domain.member.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    Member findByEmailFetch(String email);

    //베스트 여행자 목록 기본 10개
    List<Member> findAllByMemberOrderByHeartCountBasicFetch();

    //베스트 여행자 목록 더 보기 - 페이징 필요
    List<Member> findAllByMemberOrderByHeartCountMoreFetch();
}
