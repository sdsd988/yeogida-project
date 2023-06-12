package com.Udemy.YeoGiDa.domain.heart.repository;

import com.Udemy.YeoGiDa.domain.heart.entity.Heart;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeartRepositoryCustom {

    List<Heart> findAllByMemberAndHeartFetch(Member m);
}
