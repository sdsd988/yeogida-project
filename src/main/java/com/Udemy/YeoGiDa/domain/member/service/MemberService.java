package com.Udemy.YeoGiDa.domain.member.service;

import com.Udemy.YeoGiDa.domain.alarm.entity.Alarm;
import com.Udemy.YeoGiDa.domain.alarm.repository.AlarmRepository;
import com.Udemy.YeoGiDa.domain.common.service.S3Service;
import com.Udemy.YeoGiDa.domain.follow.entity.Follow;
import com.Udemy.YeoGiDa.domain.follow.repository.FollowRepository;
import com.Udemy.YeoGiDa.domain.heart.entity.Heart;
import com.Udemy.YeoGiDa.domain.heart.repository.HeartRepository;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.entity.MemberImg;
import com.Udemy.YeoGiDa.domain.member.exception.AlreadyExistsNicknameException;
import com.Udemy.YeoGiDa.domain.member.exception.MemberDuplicateException;
import com.Udemy.YeoGiDa.domain.member.exception.MemberNotFoundException;
import com.Udemy.YeoGiDa.domain.member.exception.PasswordMismatchException;
import com.Udemy.YeoGiDa.domain.member.repository.MemberImgRepository;
import com.Udemy.YeoGiDa.domain.member.repository.MemberRepository;
import com.Udemy.YeoGiDa.domain.member.request.MemberJoinRequest;
import com.Udemy.YeoGiDa.domain.member.request.MemberLoginRequest;
import com.Udemy.YeoGiDa.domain.member.request.MemberUpdateRequest;
import com.Udemy.YeoGiDa.domain.member.response.*;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import com.Udemy.YeoGiDa.global.jwt.dto.TokenInfo;
import com.Udemy.YeoGiDa.global.jwt.service.JwtTokenProvider;
import com.amazonaws.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberImgRepository memberImgRepository;
    private final S3Service s3Service;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final FollowRepository followRepository;
    private final HeartRepository heartRepository;
    private final AlarmRepository alarmRepository;

    @Transactional(readOnly = true)
    public boolean isJoinMember(String email) {
        return memberRepository.existsByEmail(email);
    }

    public MemberLoginResponse login(MemberLoginRequest memberLoginRequest) {
        Member member = memberRepository.findMemberByEmail(memberLoginRequest.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(member);

        checkPassword(memberLoginRequest.getKakaoId(), member.getPassword());

        String refreshToken = tokenInfo.getRefreshToken();
        member.setRefreshToken(refreshToken);

        String deviceToken = memberLoginRequest.getDeviceToken();
        member.setDeviceToken(deviceToken);

        return new MemberLoginResponse(member.getId(), tokenInfo);
    }

    public MemberJoinResponse join(MemberJoinRequest memberJoinRequest, String imgPath) {
        String encodePw = passwordEncoder.encode(memberJoinRequest.getKakaoId());

        //회원 저장 로직
        Member member = Member.builder()
                .email(memberJoinRequest.getEmail())
                .password(encodePw)
                .nickname(memberJoinRequest.getNickname())
                .role("ROLE_USER")
                .build();

        isValidateDuplicateMember(member);
        isValidateDuplicateNickname(member);

        //회원 이미지 저장 로직
        if(imgPath == null) {
            imgPath = "https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/default_member.png";
        }
        MemberImg memberImg = new MemberImg(imgPath, member);
        member.setMemberImg(memberImg);
        memberRepository.save(member);
//        memberImgRepository.save(memberImg);

        MemberDto memberDto = new MemberDto(member);
        return new MemberJoinResponse(memberDto);
    }

    public void setDefaultNicknameAndImage(Member member) {
        if (member == null) {
            throw new MemberNotFoundException();
        }

        //기본 닉네임으로 변경
        member.update(UUID.randomUUID().toString().substring(0, 8));
        //회원 이미지 로직
        MemberImg findMemberImg = memberImgRepository.findMemberImgByMember(member);
        String fileName = findMemberImg.getImgUrl().split("/")[3];
        if (!fileName.equals("default_member.png")) {
            s3Service.deleteFile(fileName);
            memberImgRepository.delete(findMemberImg);
            MemberImg memberImg = new MemberImg("https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/default_member.png", member);
            member.setMemberImg(memberImg);
        }
        memberRepository.save(member);
    }

    public void update(Member member, MemberUpdateRequest memberUpdateRequest, String imgPath) {

        if(member == null) {
            throw new MemberNotFoundException();
        }

        if(memberRepository.existsByNickname(memberUpdateRequest.getNickname()) == true &&
            !member.getNickname().equals(memberUpdateRequest.getNickname())) {
            throw new AlreadyExistsNicknameException();
        }

        //회원 이미지 로직
        MemberImg findMemberImg = memberImgRepository.findMemberImgByMember(member);
        String fileName = findMemberImg.getImgUrl().split("/")[3];
        //원래 default_image일 때
        if(fileName.equals("default_member.png")) {
            memberImgRepository.delete(findMemberImg);
        }
        else {
            s3Service.deleteFile(fileName);
            memberImgRepository.delete(findMemberImg);
        }

        if(imgPath == null) {
            imgPath = "https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/default_member.png";
        }
        MemberImg memberImg = new MemberImg(imgPath, member);
        memberImgRepository.save(memberImg);
        member.setMemberImg(memberImg);

        member.update(memberUpdateRequest.getNickname());
        memberRepository.save(member);
    }

    public void delete(Member member) {

        if(member == null) {
            throw new MemberNotFoundException();
        }

        MemberImg findMemberImg = memberImgRepository.findMemberImgByMember(member);
        String fileName = findMemberImg.getImgUrl().split("/")[3];
        //이미지가 기본 이미지가 아닐때만
        if(!fileName.equals("default_member.png")) {
            s3Service.deleteFile(fileName);
        }
        memberImgRepository.delete(findMemberImg);

        List<Heart> hearts = heartRepository.findAllByMember(member);
        for (Heart heart : hearts) {
            heart.getTrip().getMember().minusHeartCount();
            heart.getTrip().minusChangeHeartCount();
        }

        if(followRepository.existsByToMemberIdOrFromMemberId(member.getId(),member.getId())){
            List<Follow> findFollow = followRepository.findByMemberId(member.getId());
            followRepository.deleteAll(findFollow);
        }

        List<Alarm> followAlarmByMemberId = alarmRepository.findFollowAlarmByMemberId(member.getId());
        alarmRepository.deleteAll(followAlarmByMemberId);

        memberRepository.delete(member);
    }

    @Transactional(readOnly = true)
    public List<BestTravlerListResponse> memberList() {
        return memberRepository.findAll()
                .stream().map(BestTravlerListResponse::new)
                .collect(Collectors.toList());
    }

    private void isValidateDuplicateMember(Member member){
        if(memberRepository.existsByEmail(member.getEmail()) == true) {
            throw new MemberDuplicateException();
        }
    }

    public MemberDto memberDetail(Member member) {
        if(member == null) {
            throw new MemberNotFoundException();
        }

        return new MemberDto(member);
    }

    //베스트 여행자 10명
    public List<BestTravlerListResponse> getBestTravelerBasic() {
        return memberRepository.findAllByMemberOrderByHeartCountBasicFetch()
                .stream().map(BestTravlerListResponse::new)
                .collect(Collectors.toList());
    }

    public List<BestTravlerListResponse> getBestTravelerMore() {
        return memberRepository.findAllByMemberOrderByHeartCountMoreFetch()
                .stream().map(BestTravlerListResponse::new)
                .collect(Collectors.toList());
    }

    public List<MemberResponseDto> memberSearchByNickname(String nickname) {
        return memberRepository.findMembersByNickname(nickname)
                .stream().map(MemberResponseDto::new)
                .collect(Collectors.toList());
    }


    private void isValidateDuplicateNickname(Member member) {
        if(memberRepository.existsByNickname(member.getNickname()) == true) {
            throw new AlreadyExistsNicknameException();
        }
    }

    private void checkPassword(String loginPassword, String password) {
        if(!passwordEncoder.matches(loginPassword, password) ){
            throw new PasswordMismatchException();
        }
    }

    @Transactional(readOnly = true)
    public MemberDetailResponseDto getMemberDetail(Member member) {

        if(member == null){
            throw new MemberNotFoundException();
        }

        Member memberDetail = Optional.ofNullable(memberRepository.findById(member.getId())
                .orElseThrow(MemberNotFoundException::new)).get();

        MemberDetailResponseDto memberDetailResponseDto = new MemberDetailResponseDto(memberDetail);

        memberDetailResponseDto.setFollowingCount(followRepository.findSizeFollower(member.getId()));
        memberDetailResponseDto.setFollowerCount(followRepository.findSizeFollowing(member.getId()));

        return memberDetailResponseDto;
    }

    public void synchronizingHeartCount() {
        List<Member> all = memberRepository.findAll();
        for (Member member : all) {
            Integer heartCount = 0;
            List<Trip> trips = member.getTrips();
            if(CollectionUtils.isNullOrEmpty(trips)) {
                member.setHeartCount(0);
            } else {
                for (Trip trip : trips) {
                    heartCount += trip.getHearts().size();
                }
                member.setHeartCount(heartCount);
            }
        }
    }
}
