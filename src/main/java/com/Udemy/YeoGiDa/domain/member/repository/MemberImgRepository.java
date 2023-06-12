package com.Udemy.YeoGiDa.domain.member.repository;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.entity.MemberImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberImgRepository extends JpaRepository<MemberImg, Long> {
    MemberImg findMemberImgByMember(Member member);
}
