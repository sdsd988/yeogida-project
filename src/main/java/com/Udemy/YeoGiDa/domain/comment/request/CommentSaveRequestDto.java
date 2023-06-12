
package com.Udemy.YeoGiDa.domain.comment.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentSaveRequestDto {

    private String content;

    @Builder
    public CommentSaveRequestDto(String content){
        this.content = content;
    }
}

