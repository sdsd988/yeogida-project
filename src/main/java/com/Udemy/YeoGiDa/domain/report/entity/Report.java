package com.Udemy.YeoGiDa.domain.report.entity;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Report {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    private String type;

    private Long targetId;

    public Report(Member member, String type, Long targetId) {
        this.member = member;
        this.type = type;
        this.targetId = targetId;
    }
}
