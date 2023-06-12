package com.Udemy.YeoGiDa.domain.follow.repository;

import com.Udemy.YeoGiDa.domain.follow.entity.Follow;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Follow.PK>, FollowRepositoryCustom {
    Optional<Follow> findByToMemberIdAndFromMemberId(Long toMemberId, Long fromMemberId);

    List<Follow> findAll();

    @Query(value ="select m from Follow f INNER JOIN Member m ON f.toMemberId = m.id where f.fromMemberId = :memberId")
    List<Member> findAllByFromMemberId(@Param(value = "memberId") Long memberId);
    @Query(value ="select m from Follow f INNER JOIN Member m ON f.fromMemberId = m.id where f.toMemberId = :memberId")
    List<Member> findAllByToMemberId(@Param(value = "memberId") Long memberId);

    @Query(value ="select m.id from Follow f INNER JOIN Member m ON f.toMemberId = m.id where f.fromMemberId = :memberId")
    List<Long> findMemberIdsByFromMemberId(@Param(value = "memberId") Long memberId);

    @Query(value ="select m from Follow f INNER JOIN Member m ON f.toMemberId = m.id " +
            " where f.fromMemberId = :memberId and m.nickname like %:nickname%")
    List<Member> SearchFollowingMemberByNickname(@Param(value = "memberId") Long memberId,
                                                 @Param(value = "nickname") String nickname);

    @Query(value ="select m from Follow f INNER JOIN Member m ON f.fromMemberId = m.id " +
            " where f.toMemberId = :memberId and m.nickname like %:nickname%")
    List<Member> SearchFollowerMemberByNickname(@Param(value = "memberId") Long memberId,
                                                 @Param(value = "nickname") String nickname);

    boolean existsByToMemberIdOrFromMemberId(Long toMemberId,Long fromMemberId);


    boolean existsByToMemberIdAndFromMemberId(Long toMemberId, Long fromMemberId);
}
