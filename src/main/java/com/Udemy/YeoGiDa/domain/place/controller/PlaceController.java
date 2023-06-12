package com.Udemy.YeoGiDa.domain.place.controller;

import com.Udemy.YeoGiDa.domain.common.service.S3Service;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.place.entity.Place;
import com.Udemy.YeoGiDa.domain.place.request.PlaceSaveRequestDto;
import com.Udemy.YeoGiDa.domain.place.request.PlaceUpdateRequestDto;
import com.Udemy.YeoGiDa.domain.place.response.*;
import com.Udemy.YeoGiDa.domain.place.service.PlaceService;
import com.Udemy.YeoGiDa.global.response.success.DefaultResult;
import com.Udemy.YeoGiDa.global.response.success.StatusCode;
import com.Udemy.YeoGiDa.global.security.annotation.LoginMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class PlaceController {

    private final PlaceService placeService;
    private final S3Service s3Service;

    /**
     * @param tripId
     * @param tag
     * @param condition
     * @return 장소 목록 조회
     */

    //어그리게이션?
    @GetMapping("/{tripId}/places/{tag}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getPlaceListByTagTest(@PathVariable Long tripId,
                                                @PathVariable String tag,
                                                @RequestParam("condition") String condition){
        List<PlaceListResponseDto> places = placeService.getPlaceListByTagDefault(tripId,tag,condition);

        Map<String, Object> result = new LinkedHashMap<>();

        //객체로 만들어서 사용하는 것이 좋다.
        result.put("memberId", placeService.getMemberIdFromTripId(tripId));
        result.put("placeList", places);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                String.format("장소 목록 조회 성공 " + condition), result), HttpStatus.OK);
    }

    /**
     * @param tripId
     * @return 장소 목록에서 필요한 여행지 정보
     */
    @GetMapping("/{tripId}/places/tripInfo")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getTripInfoInPlaceList(@LoginMember Member member,
                                                 @PathVariable Long tripId){
        PlaceListInTripResponseDto result = placeService.getTripDataInPlaceList(tripId, member);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "여행지 정보 조회 성공 ", result), HttpStatus.OK);
    }

    /**
     * @param member
     * @return 내가 댓글 단 장소 목록 조회
     */
    @GetMapping("/places/commented")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getPlaceListByComment(@LoginMember Member member){
        List<PlaceListResponseDto> places = placeService.getPlaceByComment(member);
        Map<String, Object> result = new HashMap<>();
        result.put("placeList", places);

        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "내가 댓글 단 장소 목록 조회 성공", result), HttpStatus.OK);
    }

    /**
     * @param tripId
     * @return 장소 지도로 보기
     */
    @GetMapping("/{tripId}/places/inMap")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getPlaceInMap(@PathVariable Long tripId) {
        List<PlaceListInMapResponseDto> places = placeService.getPlaceInMap(tripId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("meanLat",placeService.getCentralGeoCoordinate(places).get(0));
        result.put("meanLng",placeService.getCentralGeoCoordinate(places).get(1));
        result.put("placeList", places);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                String.format("지도 위 장소 목록 조회 성공" ), result), HttpStatus.OK);
    }

    /**
     * @param placeId
     * @return 장소 상세 조회
     */
    @GetMapping("/places/{placeId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getPlaceDetail(@PathVariable Long placeId) {
        PlaceDetailResponseDto result = placeService.getPlaceDetail(placeId);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "장소 상세 조회 성공", result), HttpStatus.OK);
    }


    /**
     * @param placeSaveRequestDto
     * @param tripId
     * @param multipartFiles
     * @param member
     * @return 장소 저장
     */
    @PostMapping("/{tripId}/places/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity save(PlaceSaveRequestDto placeSaveRequestDto,
                               @PathVariable Long tripId,
                               @RequestPart(name = "imgUrls", required = false) List<MultipartFile> multipartFiles,
                               @LoginMember Member member) {

        PlaceDetailResponseDto result;
        if(multipartFiles.size() == 1 && multipartFiles.get(0).isEmpty()) {
            result = placeService.saveNoPicture(placeSaveRequestDto, tripId, member);
        }
        else {
            List<String> imgPaths = s3Service.upload(multipartFiles);
            result = placeService.saveWithPictures(placeSaveRequestDto,tripId, member, imgPaths);
        }

        return new ResponseEntity(DefaultResult.res(StatusCode.CREATED,
                "장소 작성 성공", result), HttpStatus.CREATED);
    }


    /**
     * @param placeUpdateRequestDto
     * @param placeId
     * @param multipartFiles
     * @param member
     * @return 장소 수정
     */
    @PutMapping("/places/{placeId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity update(@ModelAttribute PlaceUpdateRequestDto placeUpdateRequestDto,
                                 @PathVariable Long placeId,
                                 @RequestPart(name = "imgUrls", required = false) List<MultipartFile> multipartFiles,
                                 @LoginMember Member member) {
        List<String> imgPaths = new ArrayList<>();

        if(multipartFiles.size() == 1 && multipartFiles.get(0).isEmpty()) {
            imgPaths = null;
        }
        else {
            imgPaths = s3Service.upload(multipartFiles);
        }

        placeService.update(placeUpdateRequestDto, placeId, member, imgPaths);

        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "장소 수정 성공"), HttpStatus.OK);
    }

    /**
     * @param placeId
     * @param member
     * @return 장소 삭제
     */
    @DeleteMapping("/places/{placeId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@PathVariable Long placeId,
                                 @LoginMember Member member) {
        placeService.delete(placeId, member);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "장소 삭제 성공"), HttpStatus.OK);
    }

    /**
     * @return 지도에 장소 리스트 전체 반환
     */
    @GetMapping("/places/inMap")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity aroundViewPlace() {
        List<PlaceAroundViewResponseDto> placeAroundViewResponseDtos = placeService.aroundViewPlace();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("placeList", placeAroundViewResponseDtos);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "지도에 있는 모든 장소 리스트 반환 성공", result), HttpStatus.OK);
    }

    /**
     * @param latitude
     * @param longitude
     * @return 지도에서 위도, 경도로 검색한 장소 리스트 반환
     */
    @GetMapping("/places/marker")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity aroundMarkerPlace(@RequestParam Double latitude,
                                            @RequestParam Double longitude) {
        List<PlaceAroundMarkerResponseDto> placeAroundMarkerResponseDtos = placeService.aroundMarkerPlace(latitude, longitude);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("placeList", placeAroundMarkerResponseDtos);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "지도에서 위도, 경도로 검색한 장소 리스트 반환 성공", result), HttpStatus.OK);
    }

    /**
     * @param keyword
     * @return 지도에서 위도, 경도로 검색한 장소 리스트 반환
     */
    @GetMapping("/places/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity aroundMarkerPlace(@RequestParam String keyword) {
        List<Double> doubles = placeService.aroundViewSearchGetCentralGeoCoordinate(keyword);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("meanLat", doubles.get(0));
        result.put("meanLng", doubles.get(1));
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "둘러보기에서 검색한 장소들의 중앙 좌표", result), HttpStatus.OK);
    }
}
