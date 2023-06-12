package com.Udemy.YeoGiDa.domain.follow.controller;

import com.Udemy.YeoGiDa.domain.follow.response.FollowMemberDetailResponseDto;
import com.Udemy.YeoGiDa.domain.follow.response.FollowResponseDto;
import com.Udemy.YeoGiDa.domain.follow.service.FollowService;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.global.response.success.DefaultResult;
import com.Udemy.YeoGiDa.global.response.success.StatusCode;
import com.Udemy.YeoGiDa.global.security.annotation.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/follow")
public class FollowController {

    private final FollowService followService;

    @GetMapping("/following")
    public ResponseEntity getFollowingListOrderById(@LoginMember Member member){
        List<FollowResponseDto> followingList = followService.getFollowingList(member);
        Map<String, Object> result = new HashMap<>();
        result.put("followingList",followingList);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "팔로잉 목록 조회 성공", result), HttpStatus.OK);
    }

    @GetMapping("/follower")
    public ResponseEntity getFollowerListOrderById(@LoginMember Member member){
        List<FollowResponseDto> followerList = followService.getFollowerList(member);
        Map<String, Object> result = new HashMap<>();
        result.put("followerList",followerList);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "팔로워 목록 조회 성공", result), HttpStatus.OK);
    }

    @GetMapping("/search/following")
    public ResponseEntity getFollowingByNickname(@LoginMember Member member,
                                                 @RequestParam("nickname") String nickname){
        List<FollowResponseDto> followingList = followService.getFollowingListSearch(member.getId(),nickname);
        Map<String, Object> result = new HashMap<>();
        result.put("followingList", followingList);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "팔로잉 검색 목록 조회 성공", result), HttpStatus.OK);
    }

    @GetMapping("/search/follower")
    public ResponseEntity getFollowerByNickname(@LoginMember Member member,
                                                 @RequestParam("nickname") String nickname){
        List<FollowResponseDto> followerList = followService.getFollowerListSearch(member.getId(),nickname);
        Map<String, Object> result = new HashMap<>();
        result.put("followerList", followerList);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "팔로워 검색 목록 조회 성공", result), HttpStatus.OK);
    }


    @PostMapping("/{toMemberId}")
    public ResponseEntity addFollowing(@PathVariable Long toMemberId,
                                       @LoginMember Member member) throws IOException {

        boolean result = followService.addFollow(toMemberId, member.getId());

        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "팔로우 성공", result), HttpStatus.OK);
    }

    @DeleteMapping("/{toMemberId}/following")
    public ResponseEntity deleteFollowing(@PathVariable Long toMemberId,
                                          @LoginMember Member member){

        boolean result = followService.unFollow(toMemberId, member.getId());

        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "팔로잉 취소 성공", result), HttpStatus.OK);
    }


    @DeleteMapping("/{fromMemberId}/follower")
    public ResponseEntity deleteFollower(@PathVariable Long fromMemberId,
                                          @LoginMember Member member){

        boolean result = followService.unFollow(member.getId(), fromMemberId);

        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "팔로워 취소 성공", result), HttpStatus.OK);
    }

    @GetMapping("/{findMemberId}/detail")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getPlaceDetail(@PathVariable Long findMemberId,
                                         @LoginMember Member member) {
        FollowMemberDetailResponseDto result = followService.getFindMemberDetail(findMemberId,member.getId());
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "유저 상세 조회 성공", result), HttpStatus.OK);
    }
}
