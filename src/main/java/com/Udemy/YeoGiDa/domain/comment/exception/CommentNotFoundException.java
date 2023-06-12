package com.Udemy.YeoGiDa.domain.comment.exception;

import com.Udemy.YeoGiDa.global.exception.NotFoundException;

public class CommentNotFoundException extends NotFoundException {
    public CommentNotFoundException() {
        super("댓글을 찾을 수 없습니다.");
    }
}
