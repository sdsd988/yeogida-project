package com.Udemy.YeoGiDa.domain.place.entity;


import com.Udemy.YeoGiDa.domain.comment.entity.Comment;
import com.Udemy.YeoGiDa.domain.common.entity.BaseEntity;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
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
public class Place extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;
    private String title;
    private String content;
    private String address;
    private Double longitude;
    private Double latitude;
    private Float star = 0.0F;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Trip trip;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    List<PlaceImg> placeImgs = new ArrayList<>();

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    List<Comment> comments = new ArrayList<>();

    private String tag;

    @Builder
    public Place(String title, String content, String address, Double longitude, Double latitude, Float star, Trip trip, String tag) {
        this.title = title;
        this.content = content;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.star = star;
        this.trip = trip;
        this.tag = tag;
    }

    public void update(String content, Float star, String tag) {
        this.content = content;
        this.star = star;
        this.tag = tag;
    }

    public void setPlaceImgs(List<PlaceImg> placeImgs) {
        this.placeImgs = placeImgs;
    }
}