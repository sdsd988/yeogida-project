package com.Udemy.YeoGiDa.domain.trip.service;

import com.Udemy.YeoGiDa.domain.alarm.entity.Alarm;
import com.Udemy.YeoGiDa.domain.alarm.entity.AlarmType;
import com.Udemy.YeoGiDa.domain.alarm.exception.AlarmNotFoundException;
import com.Udemy.YeoGiDa.domain.alarm.repository.AlarmRepository;
import com.Udemy.YeoGiDa.domain.common.exception.ImgNotFoundException;
import com.Udemy.YeoGiDa.domain.common.service.S3Service;
import com.Udemy.YeoGiDa.domain.follow.repository.FollowRepository;
import com.Udemy.YeoGiDa.domain.follow.response.FollowResponseDto;
import com.Udemy.YeoGiDa.domain.heart.entity.Heart;
import com.Udemy.YeoGiDa.domain.heart.exception.AlreadyHeartException;
import com.Udemy.YeoGiDa.domain.heart.exception.HeartNotFoundException;
import com.Udemy.YeoGiDa.domain.heart.repository.HeartRepository;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.exception.MemberNotFoundException;
import com.Udemy.YeoGiDa.domain.member.repository.MemberRepository;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import com.Udemy.YeoGiDa.domain.trip.entity.TripImg;
import com.Udemy.YeoGiDa.domain.trip.exception.HeartTripNotFoundException;
import com.Udemy.YeoGiDa.domain.trip.exception.TripNotFoundException;
import com.Udemy.YeoGiDa.domain.trip.repository.TripImgRepository;
import com.Udemy.YeoGiDa.domain.trip.repository.TripRepository;
import com.Udemy.YeoGiDa.domain.trip.request.TripSaveRequestDto;
import com.Udemy.YeoGiDa.domain.trip.response.*;
import com.Udemy.YeoGiDa.global.exception.ForbiddenException;
import com.Udemy.YeoGiDa.global.fcm.service.FirebaseCloudMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
public class TripService {

    //NOArgs(private)
    //롬복보다 생성자 주입 직접
    private final TripRepository tripRepository;
    private final TripImgRepository tripImgRepository;
    private final S3Service s3Service;
    private final HeartRepository heartRepository;
    private final AlarmRepository alarmRepository;
    private final FollowRepository followRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final MemberRepository memberRepository;
    private final StringRedisTemplate redisTemplate;

    public TripService(TripRepository tripRepository, TripImgRepository tripImgRepository, S3Service s3Service, HeartRepository heartRepository, AlarmRepository alarmRepository, FollowRepository followRepository, FirebaseCloudMessageService firebaseCloudMessageService, MemberRepository memberRepository,StringRedisTemplate redisTemplate) {
        this.tripRepository = tripRepository;
        this.tripImgRepository = tripImgRepository;
        this.s3Service = s3Service;
        this.heartRepository = heartRepository;
        this.alarmRepository = alarmRepository;
        this.followRepository = followRepository;
        this.firebaseCloudMessageService = firebaseCloudMessageService;
        this.memberRepository = memberRepository;
        this.redisTemplate = redisTemplate;
    }


    public List<TripListResponseDto> getTripList(String condition) {
        return tripRepository.findAllByConditionFetch(condition)
                .stream()
                .map(TripListResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<TripListResponseDto> getTripListByRegion(String region, String condition) {
        return tripRepository.findAllByRegionAndConditionFetch(region, condition)
                .stream()
                .map(TripListResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<TripListResponseDto> getTripListSearch(String keyword) {

        String key = "rank";
        ZSetOperations<String,String> stringStringZSetOperations = redisTemplate.opsForZSet();


        Double score = 0.0;
        try {
            // 검색을하면 해당검색어를 value에 저장하고, score를 1 준다
            redisTemplate.opsForZSet().incrementScore(key, keyword,1);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        //score를 1씩 올려준다.
        redisTemplate.opsForZSet().incrementScore(key, keyword, score);


        return tripRepository.findAllSearch(keyword)
                .stream()
                .map(TripListResponseDto::new)
                .collect(Collectors.toList());
    }

    public TripDetailResponseDto getTripDetail(Long tripId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(TripNotFoundException::new);

        //from 변경
        return new TripDetailResponseDto(trip);
    }

    @Transactional
    public TripDetailResponseDto save(TripSaveRequestDto tripSaveRequestDto, Member member, String imgPath) {
        if (member == null) {
            throw new MemberNotFoundException();
        }

        //여행지 저장 로직
        //변경
        Trip trip = Trip.builder()
                .region(tripSaveRequestDto.getRegion())
                .title(tripSaveRequestDto.getTitle())
                .subTitle(tripSaveRequestDto.getSubTitle())
                .member(member)
                .build();

        //여행지 이미지 저장 로직
        TripImg tripImg = new TripImg(imgPath, trip);
        if (imgPath == null) {
            throw new ImgNotFoundException();
        }
        trip.setTripImg(tripImg);
        tripRepository.save(trip);
//        tripImgRepository.save(tripImg);
        return new TripDetailResponseDto(trip);
    }

    @Transactional
    public void update(Long tripId, TripSaveRequestDto tripSaveRequestDto, Member member, String imgPath) {
        if (member == null) {
            throw new MemberNotFoundException();
        }

        Trip trip = Optional.ofNullable(tripRepository.findById(tripId)
                .orElseThrow(TripNotFoundException::new)).get();

        if (trip.getMember().getId() != member.getId()) {
            throw new ForbiddenException();
        }

        //여행지 이미지 수정 로직
        TripImg findTripImg = tripImgRepository.findTripImgByTrip(trip);
        String fileName = findTripImg.getImgUrl().split("/")[3];
        s3Service.deleteFile(fileName);
        tripImgRepository.delete(findTripImg);
        TripImg tripImg = new TripImg(imgPath, trip);
        if (imgPath == null) {
            throw new ImgNotFoundException();
        }
        tripImgRepository.save(tripImg);
        trip.setTripImg(tripImg);

        trip.update(tripSaveRequestDto.getRegion(), tripSaveRequestDto.getTitle(),
                tripSaveRequestDto.getSubTitle());
    }

    @Transactional
    public void delete(Long tripId, Member member) {
        if (member == null) {
            throw new ForbiddenException();
        }

        Trip trip = Optional.ofNullable(tripRepository.findById(tripId)
                .orElseThrow(TripNotFoundException::new)).get();

        if (trip.getMember().getId() != member.getId()) {
            throw new ForbiddenException();
        }

        TripImg findTripImg = tripImgRepository.findTripImgByTrip(trip);
        String fileName = findTripImg.getImgUrl().split("/")[3];
        s3Service.deleteFile(fileName);
        tripImgRepository.delete(findTripImg);

        int size = heartRepository.findAllByTripId(tripId).size();
        for (int i=0; i<size; i++){
            trip.getMember().minusHeartCount();
        }

        List<Alarm> heartAlarmByTripId = alarmRepository.findHeartAlarmByTripId(tripId);
        alarmRepository.deleteAll(heartAlarmByTripId);

        List<Alarm> commentAlarmByTripId = alarmRepository.findCommentAlarmByTripId(tripId);
        alarmRepository.deleteAll(commentAlarmByTripId);

        tripRepository.delete(trip);
    }

    public List<TripBestListResponseDto> getMonthBestTripBasic() {
        return tripRepository.findAllOrderByChangeHeartCountBasicFetch()
                .stream()
                .map(TripBestListResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<TripBestListResponseDto> getMonthBestTripMore() {
        return tripRepository.findAllOrderByChangeHeartCountMoreFetch()
                .stream()
                .map(TripBestListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void heart(Long tripId, Member member) throws IOException {
        if (member == null) {
            throw new MemberNotFoundException();
        }

        Trip trip = Optional.ofNullable(tripRepository.findById(tripId)
                .orElseThrow(TripNotFoundException::new)).get();

        trip.plusChangeHeartCount();
        trip.getMember().plusHeartCount();

        heartRepository.findByMemberAndTrip(member, trip).ifPresent(it -> {
            throw new AlreadyHeartException();
        });

        heartRepository.save(Heart.builder()
                .member(member)
                .trip(trip)
                .build());

        //알람 추가
        if(!trip.getMember().getNickname().equals(member.getNickname())) {
            alarmRepository.save(new Alarm(
                    trip.getMember(),
                    AlarmType.NEW_HEART,
                    member.getId(),
                    null,
                    tripId,
                    null,
                    null
            ));
        }

        //푸쉬 알림 보내기
        if(!trip.getMember().getNickname().equals(member.getNickname())) {
            firebaseCloudMessageService.sendMessageTo(trip.getMember().getDeviceToken(),
                    "여기다", member.getNickname() + AlarmType.NEW_HEART.getAlarmText(),
                    "NEW_HEART",
                    tripId.toString());
        }
    }

    @Transactional
    public void deleteHeart(Long tripId, Member member) {
        if (member == null) {
            throw new MemberNotFoundException();
        }

        Trip trip = Optional.ofNullable(tripRepository.findById(tripId)
                .orElseThrow(TripNotFoundException::new)).get();

        trip.minusChangeHeartCount();
        trip.getMember().minusHeartCount();

        Heart heart = heartRepository.findByMemberAndTrip(member, trip)
                .orElseThrow(HeartNotFoundException::new);

        heartRepository.delete(heart);

        //알람 삭제
        if(!trip.getMember().getNickname().equals(member.getNickname())) {
            Alarm findAlarm = alarmRepository.findAlarmByTripIdAndMakeMemberId(trip.getId(), member.getId())
                    .orElseThrow(AlarmNotFoundException::new);
            alarmRepository.delete(findAlarm);
        }
    }

    //내가 작성한 여행지
    public List<TripListResponseDto> getMyTripList(Member member) {
        return tripRepository.findAllByMemberFetch(member)
                .stream()
                .map(TripListResponseDto::new)
                .collect(Collectors.toList());
    }

    //내가 작성한 여행지 검색
    public List<TripListResponseDto> getMyTripListSearch(Member member, String keyword) {
        List<Trip> trips = tripRepository.findAllByMemberFetch(member);
        List<TripListResponseDto> tripListResponseDtos = new ArrayList<>();
        for (Trip trip : trips) {
            if (trip.getTitle().contains(keyword) || trip.getSubTitle().contains(keyword)) {
                tripListResponseDtos.add(new TripListResponseDto(trip));
            }
        }
        Collections.reverse(tripListResponseDtos);
        return tripListResponseDtos;
    }

    //전체 여행지 검색
    public List<TripListResponseDto> tripAllSearchAndSort(String keyword, String condition) {
        List<Trip> trips = tripRepository.findAllTripSearchAndSort(keyword, condition);

        String key = "rank";
        ZSetOperations<String,String> stringStringZSetOperations = redisTemplate.opsForZSet();


        Double score = 0.0;
        try {
            // 검색을하면 해당검색어를 value에 저장하고, score를 1 준다
            redisTemplate.opsForZSet().incrementScore(key, keyword,1);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        //score를 1씩 올려준다.
        redisTemplate.opsForZSet().incrementScore(key, keyword, score);
        return trips.stream()
                .map(TripListResponseDto::new)
                .collect(Collectors.toList());
    }

    //멤버 여행지 지역 조건 o, 정렬
    public List<TripListWithRegionResponseDto> getMemberTripList(Long memberId, String region, String condition) {

        memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException());

        return tripRepository.findAllByMemberIdFetch(memberId,region,condition)
                .stream()
                .map(TripListWithRegionResponseDto::new)
                .collect(Collectors.toList());
    }

    //멤버 여행지 지역 조건 x, 정렬
    public List<TripListWithRegionResponseDto> getMemberTripList2(Long memberId, String condition) {

        memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException());

        return tripRepository.findAllByMemberIdFetch2(memberId,condition)
                .stream()
                .map(TripListWithRegionResponseDto::new)
                .collect(Collectors.toList());
    }

    //내가 좋아요한 여행지
    public List<TripListWithRegionResponseDto> getMyHeartTripList(Member member) {
        List<Heart> findHeart = heartRepository.findAllByMemberAndHeartFetch(member);
        if (findHeart.isEmpty()) {
            throw new HeartTripNotFoundException();
        }
        List<TripListWithRegionResponseDto> tripListWithRegionResponseDtos = new ArrayList<>();
        for (Heart heart : findHeart) {
            Long tripId = heart.getTrip().getId();
            Trip trip = tripRepository.findById(tripId).orElseThrow(TripNotFoundException::new);
            tripListWithRegionResponseDtos.add(new TripListWithRegionResponseDto(trip));
        }
        Collections.reverse(tripListWithRegionResponseDtos);
        return tripListWithRegionResponseDtos;
    }

    //좋아요한 여행지 지역별 필터
    public List<TripListWithRegionResponseDto> getMyHeartTripFilterList(Member member, String region) {
        List<Heart> findHeart = heartRepository.findAllByMemberAndHeartFetch(member);
        //CollectionUtils.isNullOrEmpty()
        if (findHeart.isEmpty()) {
            throw new HeartTripNotFoundException();
        }
        //멘토님 리팩토링 코드
//        List<Long> tripIds = findHeart.stream().map(h -> h.getTrip().getId()).collect(Collectors.toList());
//        List<TripListWithRegionResponseDto> collect = tripRepository.findByIdIn(tripIds).stream().map(TripListWithRegionResponseDto::new).collect(Collectors.toList());

        //내 코드
        List<TripListWithRegionResponseDto> tripListWithRegionResponseDto = new ArrayList<>();
        for (Heart heart : findHeart) {
            Long tripId = heart.getTrip().getId();
            Trip trip = tripRepository.findById(tripId).orElseThrow(TripNotFoundException::new);
            if (trip.getRegion().equals(region)) {
                tripListWithRegionResponseDto.add(new TripListWithRegionResponseDto(trip));
            }
        }
        Collections.reverse(tripListWithRegionResponseDto);
        return tripListWithRegionResponseDto;
    }

    //좋아요한 여행지 검색
    public List<TripListWithRegionResponseDto> getMyHeartTripSearchList(Member member, String keyword) {
        List<Heart> findHeart = heartRepository.findAllByMemberAndHeartFetch(member);
        if (findHeart.isEmpty()) {
            throw new HeartTripNotFoundException();
        }
        List<TripListWithRegionResponseDto> tripListWithRegionResponseDto = new ArrayList<>();
        for (Heart heart : findHeart) {
            Long tripId = heart.getTrip().getId();
            Trip trip = tripRepository.findById(tripId).orElseThrow(TripNotFoundException::new);
            if (trip.getTitle().contains(keyword) || trip.getSubTitle().contains(keyword)) {
                tripListWithRegionResponseDto.add(new TripListWithRegionResponseDto(trip));
            }
        }
        Collections.reverse(tripListWithRegionResponseDto);
        return tripListWithRegionResponseDto;
    }

    //팔로잉의 최근 여행지 10개
    public List<TripBestListResponseDto> getFollowingsTripListBasic(Member member) {
        List<TripBestListResponseDto> collect = tripRepository.findAllByFollowingOrderByIdBasicFetch(member)
                .stream()
                .map(TripBestListResponseDto::new)
                .collect(Collectors.toList());

        if(CollectionUtils.isEmpty(collect)) {
            throw new TripNotFoundException();
        }
        return collect;
    }

    //팔로잉의 최근 여행지 모두
    public List<TripBestListResponseDto> getFollwingsTripListMore(Member member) {
        List<TripBestListResponseDto> collect = tripRepository.findAllByFollowingOrderByIdMoreFetch(member)
                .stream()
                .map(TripBestListResponseDto::new)
                .collect(Collectors.toList());

        if(CollectionUtils.isEmpty(collect)) {
            throw new TripNotFoundException();
        }
        return collect;
    }

    //여행지에 좋아요한 유저들 목록 반환
    public List<FollowResponseDto> getHeartMemberList(Long tripId) {
        List<Heart> allByTripId = heartRepository.findAllByTripId(tripId);
        List<Member> memberList = new ArrayList<>();
        for (Heart heart : allByTripId) {
            memberList.add(heart.getMember());
        }
        List<FollowResponseDto> followResponseDtos = new ArrayList<>();
        for (Member member : memberList) {
            followResponseDtos.add(new FollowResponseDto(member));
        }
        return followResponseDtos;
    }

    public List<TripBestListResponseDto> findRecentTrip() {
        return tripRepository.findRecentTrip()
                .stream()
                .map(TripBestListResponseDto::new)
                .collect(Collectors.toList());
    }

    //@Scheduler에서 매달 1일마다 하트 변화량을 0으로 수정
    //추가로 월간 베스트 여행지에 빈 리스트를 보내지 않도록 샘플데이터 3개 추가
    @Transactional
    public void initTripChangeHeartCount() {
        List<Trip> tripList = tripRepository.findAll();
        for (Trip trip : tripList) {
            //reset
            trip.initChangeHeartCount();
        }

        //샘플 데이터 3개 하트 추가해놓기
        Optional<Trip> tripOptional1 = tripRepository.findById(25L);
        tripOptional1.ifPresent(Trip::plusChangeHeartCount);
        Optional<Trip> tripOptional2 = tripRepository.findById(32L);
        tripOptional2.ifPresent(Trip::plusChangeHeartCount);
        Optional<Trip> tripOptional3 = tripRepository.findById(37L);
        tripOptional3.ifPresent(Trip::plusChangeHeartCount);
    }

    @Transactional
    public List<TripRankResponseDto> getRankingList() {
        String key = "rank";
        ZSetOperations<String, String> stringStringZSetOperations = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringStringZSetOperations.reverseRangeWithScores(key, 0, 9);
        List<TripRankResponseDto> rankList = new ArrayList<>();

        for (ZSetOperations.TypedTuple<String> typedTuple : typedTuples) {
            rankList.add(new TripRankResponseDto(typedTuple.getValue(),typedTuple.getScore()));
        }

        return rankList;
    }

//    @Transactional
//    public void resetRank(){
//        redisTemplate.delete("rank");
//    }

}
