package com.Udemy.YeoGiDa.domain.place.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class PlaceImg {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_img_id")
    private Long id;

    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Place place;

    public PlaceImg(String imgUrl, Place place) {
        this.imgUrl = imgUrl;
        this.place = place;
    }
}
