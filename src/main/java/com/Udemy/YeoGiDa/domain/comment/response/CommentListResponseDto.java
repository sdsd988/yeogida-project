package com.Udemy.YeoGiDa.domain.comment.response;

import com.Udemy.YeoGiDa.domain.comment.entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class CommentListResponseDto {

    private Long commentId;
    private Long memberId;
    private String imgUrl;
    private String nickname;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;

    private String content;


    @Builder
    public CommentListResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.memberId = comment.getMember().getId();
        this.imgUrl = comment.getMember().getMemberImg().getImgUrl();
        this.nickname = comment.getMember().getNickname();
        this.createdTime = comment.getCreatedTime();
        this.content = comment.getContent();
    }
}
