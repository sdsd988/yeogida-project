package com.Udemy.YeoGiDa.domain.place.service;


import com.Udemy.YeoGiDa.domain.alarm.entity.Alarm;
import com.Udemy.YeoGiDa.domain.alarm.repository.AlarmRepository;
import com.Udemy.YeoGiDa.domain.comment.repository.CommentRepository;
import com.Udemy.YeoGiDa.domain.common.service.S3Service;
import com.Udemy.YeoGiDa.domain.heart.repository.HeartRepository;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.exception.MemberNotFoundException;
import com.Udemy.YeoGiDa.domain.place.entity.Place;
import com.Udemy.YeoGiDa.domain.place.entity.PlaceImg;
import com.Udemy.YeoGiDa.domain.place.exception.PlaceNotFoundException;
import com.Udemy.YeoGiDa.domain.place.repository.PlaceImgRepository;
import com.Udemy.YeoGiDa.domain.place.repository.PlaceRepository;
import com.Udemy.YeoGiDa.domain.place.request.PlaceSaveRequestDto;
import com.Udemy.YeoGiDa.domain.place.request.PlaceUpdateRequestDto;
import com.Udemy.YeoGiDa.domain.place.response.*;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import com.Udemy.YeoGiDa.domain.trip.exception.TripNotFoundException;
import com.Udemy.YeoGiDa.domain.trip.repository.TripRepository;
import com.Udemy.YeoGiDa.global.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceImgRepository placeImgRepository;
    private final S3Service s3Service;
    private final TripRepository tripRepository;
    private final HeartRepository heartRepository;
    private final CommentRepository commentRepository;
    private final AlarmRepository alarmRepository;

    /**
     * 장소 전체 조회 (tag(지역),condition(정렬조건))
     * @param tripId
     * @param tag
     * @param condition
     * @return
     */
    @Transactional(readOnly = true)
    public List<PlaceListResponseDto> getPlaceListByTagDefault(Long tripId,String tag,String condition){
        return placeRepository.findAllByTripIdAndTagAndCondition(tripId,tag,condition)
                .stream()
                .map(PlaceListResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 내가 댓글 쓴 장소 조회
     * @param member
     */
   @Transactional(readOnly = true)
    public List<PlaceListResponseDto> getPlaceByComment(Member member){

       List<PlaceListResponseDto> collect = placeRepository.findAllByComment(member)
               .stream()
               .map(PlaceListResponseDto::new)
               .collect(Collectors.toList());


       // 마이페이지에서 장소 별 댓글 수 재설정 - 다른 사람이 나의 글에 작성 했을 때, 댓글 전체 갯수 추가가 안되는 문제 해결을 위해
       for (PlaceListResponseDto placeListResponseDto : collect) {

           placeListResponseDto.setCommentCount(commentRepository.totalSize(placeListResponseDto.getPlaceId()));

       }

       return collect;
   }

    //지도 위의 장소
    @Transactional(readOnly = true)
    public List<PlaceListInMapResponseDto> getPlaceInMap(Long tripId){

        return placeRepository.findAllByTripId(tripId)
                .stream()
                .map(PlaceListInMapResponseDto::new)
                .collect(Collectors.toList());
    }

    //위경도 중심 구하는 로직
    public List<Double> getCentralGeoCoordinate(List<PlaceListInMapResponseDto> placeList)
    {
        List<Double> result = new ArrayList<>();
        // 사이즈 하나면 그 자체가 중심값이니까
        if (placeList.size() == 1)
        {
            PlaceListInMapResponseDto place = placeList.get(0);
//            log.info("lati: {} longi: {}", place.getLatitude(), place.getLongitude());
            result.add(place.getLatitude());
            result.add(place.getLongitude());
            return result;
        } else if(placeList.size() == 0){
            result.add(null);
            result.add(null);

            return  result;
        }

        double x = 0;
        double y = 0;
        double z = 0;

        for (PlaceListInMapResponseDto place: placeList) {
//            log.info("name: {} latitude: {} longitude: {}", place.getTitle(), place.getLatitude(), place.getLongitude());
            double latitude = place.getLatitude() * Math.PI / 180;
            double longitude = place.getLongitude() * Math.PI / 180;

            x += Math.cos(latitude) * Math.cos(longitude);
            y += Math.cos(latitude) * Math.sin(longitude);
            z += Math.sin(latitude);
        }

        int total = placeList.size();

        x = x / total;
        y = y / total;
        z = z / total;

        double centralLongitude = Math.atan2(y, x);
        double centralSquareRoot = Math.sqrt(x * x + y * y);
        double centralLatitude = Math.atan2(z, centralSquareRoot);

        log.info("centerLatitude: {} centerLongitute: {}", centralLatitude, centralLongitude);
        double finalLat = centralLatitude * 180 / Math.PI;
        double finalLog = centralLongitude * 180 / Math.PI;

        result.add(finalLat);
        result.add(finalLog);

        return result;
    }

    @Transactional(readOnly = true)
    public PlaceDetailResponseDto getPlaceDetail(Long placeId) {
        Place place = Optional.ofNullable(placeRepository.findById(placeId)
                .orElseThrow(PlaceNotFoundException::new)).get();

        return new PlaceDetailResponseDto(place);
    }

    @Transactional(readOnly = true)
    public PlaceListInTripResponseDto getTripDataInPlaceList(Long tripId, Member member) {
        Trip trip = Optional.ofNullable(tripRepository.findById(tripId)
                .orElseThrow(TripNotFoundException::new)).get();
        PlaceListInTripResponseDto result = new PlaceListInTripResponseDto(trip);
        result.setTrip_like_check(heartRepository.existsByTripIdAndMember(tripId, member));
        return result;
    }


    public PlaceDetailResponseDto saveNoPicture(PlaceSaveRequestDto placeSaveRequestDto,
                                       Long tripId, Member member) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(TripNotFoundException::new);
        if(member == null){
            throw new MemberNotFoundException();}
        if(trip.getMember().getId() != member.getId()){
            throw new ForbiddenException();}
        Place place = Place.builder()
                .title(placeSaveRequestDto.getTitle())
                .address(placeSaveRequestDto.getAddress())
                .longitude(placeSaveRequestDto.getLongitude())
                .latitude(placeSaveRequestDto.getLatitude())
                .content(placeSaveRequestDto.getContent())
                .star(placeSaveRequestDto.getStar())
                .trip(trip)
                .tag(placeSaveRequestDto.getTag())
                .build();

        Place savePlace = placeRepository.save(place);

        //장소 이미지 저장 로직
        String defaultImgPath = "https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/default_place.png";
        PlaceImg placeImg = new PlaceImg(defaultImgPath, savePlace);
        placeImgRepository.save(placeImg);
        ArrayList<PlaceImg> placeImgs = new ArrayList<>();
        placeImgs.add(placeImg);
        savePlace.setPlaceImgs(placeImgs);

        PlaceDetailResponseDto placeDetailResponseDto = new PlaceDetailResponseDto(savePlace);
        placeDetailResponseDto.setPlaceImgs(placeImgs);
        return placeDetailResponseDto;
    }

    public PlaceDetailResponseDto saveWithPictures(PlaceSaveRequestDto placeSaveRequestDto,
                                       Long tripId, Member member, List<String> imgPaths) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException());
        if(member == null){
            throw new MemberNotFoundException();
        }
        if(trip.getMember().getId() != member.getId()){
            throw new ForbiddenException();
        }
        Place place = Place.builder()
                .title(placeSaveRequestDto.getTitle())
                .address(placeSaveRequestDto.getAddress())
                .longitude(placeSaveRequestDto.getLongitude())
                .latitude(placeSaveRequestDto.getLatitude())
                .content(placeSaveRequestDto.getContent())
                .star(placeSaveRequestDto.getStar())
                .trip(trip)
                .tag(placeSaveRequestDto.getTag())
                .build();

        Place savePlace = placeRepository.save(place);

        ArrayList<PlaceImg> placeImgs = new ArrayList<>();
        //장소 이미지 저장 로직
        for (String imgPath : imgPaths) {
            PlaceImg placeImg = new PlaceImg(imgPath, savePlace);
            placeImgRepository.save(placeImg);
            placeImgs.add(placeImg);
        }
        savePlace.setPlaceImgs(placeImgs);

        PlaceDetailResponseDto placeDetailResponseDto = new PlaceDetailResponseDto(savePlace);
        placeDetailResponseDto.setPlaceImgs(placeImgs);
        return placeDetailResponseDto;
    }

    public void update(PlaceUpdateRequestDto placeUpdateRequestDto,
                       Long placeId, Member member, List<String> imgPaths) {

        Place place = placeRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new);

        if(member == null){
            throw new MemberNotFoundException();
        }

        if(!place.getTrip().getMember().getNickname().equals(member.getNickname())){
            throw new ForbiddenException();
        }

        //여행지 이미지 수정 로직
        List<PlaceImg> findPlaceImgs = placeImgRepository.findPlaceImgsByPlaceFetch(place);
        //default_image일때
        String s3FileName = findPlaceImgs.get(0).getImgUrl().split("/")[3];
        if((findPlaceImgs.size() == 1) && (s3FileName.equals("default_place.png"))) {
            placeImgRepository.delete(findPlaceImgs.get(0));
        }
        else {
            for (PlaceImg findPlaceImg : findPlaceImgs) {
                String fileName = findPlaceImg.getImgUrl().split("/")[3];
                s3Service.deleteFile(fileName);
                placeImgRepository.delete(findPlaceImg);
            }
        }

        //수정된 사진이 default일 때
        ArrayList<PlaceImg> placeImgs = new ArrayList<>();
        if(imgPaths == null) {
            String imgPath = "https://yeogida-bucket.s3.ap-northeast-2.amazonaws.com/default_place.png";
            PlaceImg placeImg = new PlaceImg(imgPath, place);
            placeImgRepository.save(placeImg);
            placeImgs.add(placeImg);
            place.setPlaceImgs(placeImgs);
        }
        else {
            for (String imgPath : imgPaths) {
                PlaceImg placeImg = new PlaceImg(imgPath, place);
                placeImgRepository.save(placeImg);
                placeImgs.add(placeImg);
            }
            place.setPlaceImgs(placeImgs);
        }

        place.update(placeUpdateRequestDto.getContent(),
                placeUpdateRequestDto.getStar(),
                placeUpdateRequestDto.getTag());
    }

    public void delete(Long placeId, Member member) {

        Place place = placeRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new);

        if(member == null){
            throw new MemberNotFoundException();
        }

        if(!Objects.equals(place.getTrip().getMember().getId(), member.getId())){
            throw new ForbiddenException();
        }

        List<Alarm> commentAlarmByPlaceId = alarmRepository.findCommentAlarmByPlaceId(placeId);
        alarmRepository.deleteAll(commentAlarmByPlaceId);

        placeRepository.delete(place);
    }

    public List<PlaceAroundViewResponseDto> aroundViewPlace() {
        return placeRepository.findAll()
                .stream()
                .map(PlaceAroundViewResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<PlaceAroundMarkerResponseDto> aroundMarkerPlace(Double latitude, Double longitude) {
        return placeRepository.findAllByLatitudeAndLongitude(latitude, longitude)
                .stream()
                .map(PlaceAroundMarkerResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<Double> aroundViewSearchGetCentralGeoCoordinate(String keyword) {
        List<PlaceListInMapResponseDto> placeListInMapResponseDtos = placeRepository.findAllByKeyword(keyword)
                .stream()
                .map(PlaceListInMapResponseDto::new)
                .collect(Collectors.toList());

        return getCentralGeoCoordinate(placeListInMapResponseDtos);
    }

    public Long getMemberIdFromTripId(Long tripId) {
        Trip findTrip = tripRepository.findById(tripId).orElseThrow(TripNotFoundException::new);
        return findTrip.getMember().getId();
    }
}