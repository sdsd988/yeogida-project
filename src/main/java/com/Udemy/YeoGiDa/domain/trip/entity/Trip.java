package com.Udemy.YeoGiDa.domain.trip.entity;

import com.Udemy.YeoGiDa.domain.common.entity.BaseEntity;
import com.Udemy.YeoGiDa.domain.heart.entity.Heart;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.place.entity.Place;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trip extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private Long id;

    private String region;

    private String title;

    private String subTitle;

    private Integer changeHeartCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @OneToOne(mappedBy = "trip", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private TripImg tripImg = new TripImg();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<Place> places = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<Heart> hearts = new ArrayList<>();

    @Builder
    public Trip(String region, String title, String subTitle, Member member) {
        this.region = region;
        this.title = title;
        this.subTitle = subTitle;
        this.member = member;
    }

    public void update(String region, String title, String subTitle) {
        this.region = region;
        this.title = title;
        this.subTitle = subTitle;
    }

    public void plusChangeHeartCount() {
        this.changeHeartCount++;
    }

    public void minusChangeHeartCount() {
        this.changeHeartCount--;
    }

    public void initChangeHeartCount() {
        this.changeHeartCount = 0;
    }

    public void setTripImg(TripImg tripImg) {
        this.tripImg = tripImg;
    }
}
