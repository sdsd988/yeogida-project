package com.Udemy.YeoGiDa.domain.trip.entity;

import com.Udemy.YeoGiDa.domain.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class TripImg extends BaseEntity {

    //이미지를 엔티티에 포함
    //컨버터 있음
    //ListConverter -> AttributeConverter
    //enum 안에 넣어도돔

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_img_id")
    private Long id;

    private String imgUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Trip trip;

    //NULL 체크는 여기서 적용
    //아예 못들어갈때는 여기서 체크
    public TripImg(String imgUrl, Trip trip) {
        this.imgUrl = imgUrl;
        this.trip = trip;
    }

}
