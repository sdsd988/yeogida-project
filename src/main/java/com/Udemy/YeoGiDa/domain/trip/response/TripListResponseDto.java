package com.Udemy.YeoGiDa.domain.trip.response;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@NoArgsConstructor
@Getter
@Setter
public class TripListResponseDto {

    private Long tripId;
    private Long memberId;
    private String nickname;
    private String title;
    private String subTitle;
    private String imgUrl;
    private Integer heartCount;
    private Integer placeCount;

    @Builder
    public TripListResponseDto(Long tripId, Long memberId, String nickname, String title, String subTitle, String imgUrl, Integer heartCount, Integer placeCount) {
        this.tripId = tripId;
        this.memberId = memberId;
        this.nickname = nickname;
        this.title = title;
        this.subTitle = subTitle;
        this.imgUrl = imgUrl;
        this.heartCount = heartCount;
        this.placeCount = placeCount;
    }

    public TripListResponseDto(Trip trip) {
        this.tripId = trip.getId();
        this.memberId = trip.getMember().getId();
        this.nickname = trip.getMember().getNickname();
        this.title = trip.getTitle();
        this.subTitle = trip.getSubTitle();
        if(trip.getTripImg() == null) {
            this.imgUrl = null;
        } else {
            this.imgUrl = trip.getTripImg().getImgUrl();
        }
        this.heartCount = trip.getHearts().size();
        this.placeCount = trip.getPlaces().size();
    }

    public static TripListResponseDto from(Trip trip) {
        return TripListResponseDto.builder()
                .tripId(trip.getId())
                .memberId(Optional.ofNullable(trip.getMember()).map(Member::getId).orElse(null))
                .nickname(Optional.ofNullable(trip.getMember()).map(Member::getNickname).orElse(null))
                .memberId(Optional.ofNullable(trip.getMember()).map(Member::getId).orElse(null))
                .memberId(Optional.ofNullable(trip.getMember()).map(Member::getId).orElse(null))
                .memberId(Optional.ofNullable(trip.getMember()).map(Member::getId).orElse(null))
                .memberId(Optional.ofNullable(trip.getMember()).map(Member::getId).orElse(null))
                .memberId(Optional.ofNullable(trip.getMember()).map(Member::getId).orElse(null))
                .memberId(Optional.ofNullable(trip.getMember()).map(Member::getId).orElse(null))
                .build();
    }
}
