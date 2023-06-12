package com.Udemy.YeoGiDa.domain.member.entity;

import com.Udemy.YeoGiDa.domain.common.entity.BaseEntity;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String nickname;

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private MemberImg memberImg = new MemberImg();

    private String role;

    private String refreshToken;

    private String deviceToken;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Trip> trips = new ArrayList<>();

    private Integer heartCount = 0;

    @Builder
    public Member(String email, String password, String nickname, String role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public void update(String nickname) {
        this.nickname = nickname;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void setMemberImg(MemberImg memberImg) {
        this.memberImg = memberImg;
    }

    public void plusHeartCount() {
        this.heartCount++;
    }

    public void minusHeartCount() {
        this.heartCount--;
    }

    public void setHeartCount(Integer heartCount) {
        this.heartCount = heartCount;
    }
}
