package com.Udemy.YeoGiDa.domain.member.repository;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    boolean existsByEmail(String email);
    @Query(value ="select m from Member m where m.nickname like %:nickname% order by m.id desc")
    List<Member> findMembersByNickname(@Param(value = "nickname") String nickname);
    boolean existsByNickname(String nickname);
    Optional<Member> findMemberByEmail(String email);
}
