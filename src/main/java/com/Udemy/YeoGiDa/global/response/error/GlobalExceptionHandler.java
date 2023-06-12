package com.Udemy.YeoGiDa.global.response.error;

import com.Udemy.YeoGiDa.domain.alarm.exception.AlarmNotFoundException;
import com.Udemy.YeoGiDa.domain.comment.exception.CommentNotFoundException;
import com.Udemy.YeoGiDa.domain.common.exception.ImgNotFoundException;
import com.Udemy.YeoGiDa.domain.common.exception.WrongImgFormatException;
import com.Udemy.YeoGiDa.domain.follow.exception.AlreadyFollowException;
import com.Udemy.YeoGiDa.domain.follow.exception.FollowNotFoundException;
import com.Udemy.YeoGiDa.domain.follow.exception.NoOneFollowException;
import com.Udemy.YeoGiDa.domain.heart.exception.AlreadyHeartException;
import com.Udemy.YeoGiDa.domain.heart.exception.HeartNotFoundException;
import com.Udemy.YeoGiDa.domain.member.exception.AlreadyExistsNicknameException;
import com.Udemy.YeoGiDa.domain.member.exception.MemberDuplicateException;
import com.Udemy.YeoGiDa.domain.member.exception.MemberNotFoundException;
import com.Udemy.YeoGiDa.domain.place.exception.PlaceNotFoundException;
import com.Udemy.YeoGiDa.domain.trip.exception.HeartTripNotFoundException;
import com.Udemy.YeoGiDa.domain.trip.exception.TripImgEssentialException;
import com.Udemy.YeoGiDa.domain.trip.exception.TripNotFoundException;
import com.Udemy.YeoGiDa.global.exception.ForbiddenException;
import com.Udemy.YeoGiDa.global.exception.NotFoundException;
import com.Udemy.YeoGiDa.global.jwt.exception.TokenHasExpiredException;
import com.Udemy.YeoGiDa.global.jwt.exception.TokenIsInvalidException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    protected DefaultErrorResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new DefaultErrorResult(400, "Validation Error!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    protected DefaultErrorResult MethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
        return new DefaultErrorResult(400, "TypeMismatch Error!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    protected DefaultErrorResult handleIllegalArgumentException(IllegalArgumentException e) {
        return new DefaultErrorResult(400, "IllegalArgument Error!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    protected DefaultErrorResult handleMemberDuplicateException(MemberDuplicateException e){
        return new DefaultErrorResult(400, "MemberDuplicate Error!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    protected DefaultErrorResult handleAlreadyExistsNicknameException(AlreadyExistsNicknameException e){
        return new DefaultErrorResult(400, "AlreadyExistsNickname Error!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    protected DefaultErrorResult handleAlreadyLikedException(AlreadyHeartException e) {
        return new DefaultErrorResult(400, "AlreadyLiked Error!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    protected DefaultErrorResult handleAlreadyLikedException(AlreadyFollowException e) {
        return new DefaultErrorResult(400, "AlreadyFollowed Error!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    protected DefaultErrorResult handleAlreadyLikedException(TripImgEssentialException e) {
        return new DefaultErrorResult(400, "TripImgEssential Error!");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    protected DefaultErrorResult handleForbiddenException(ForbiddenException e){
        return new DefaultErrorResult(403, "Forbidden Error!");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    protected DefaultErrorResult handleJwtException(JwtException e){
        return new DefaultErrorResult(403, "JwtInvalid Error!");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    protected DefaultErrorResult handleTokenHasExpiredException(TokenHasExpiredException e){
        return new DefaultErrorResult(403, "TokenHasExpired Error!");
    }


    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    protected DefaultErrorResult handleTokenIsInvalidException(TokenIsInvalidException e){
        return new DefaultErrorResult(403, "TokenIsInvalid Error!");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    protected DefaultErrorResult handleNotFoundException(NotFoundException e){
        return new DefaultErrorResult(404, "NotFound Error!");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    protected DefaultErrorResult handleNotFoundException(MemberNotFoundException e){
        return new DefaultErrorResult(404, "Member NotFound Error!");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    protected DefaultErrorResult handleNotFoundException(TripNotFoundException e){
        return new DefaultErrorResult(404, "Trip NotFound Error!");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    protected DefaultErrorResult handleNotFoundException(PlaceNotFoundException e){
        return new DefaultErrorResult(404, "Place NotFound Error!");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    protected DefaultErrorResult handleNotFoundException(CommentNotFoundException e){
        return new DefaultErrorResult(404, "Comment NotFound Error!");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    protected DefaultErrorResult handleNotFoundException(HeartNotFoundException e){
        return new DefaultErrorResult(404, "Heart NotFound Error!");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    protected DefaultErrorResult handleNotFoundException(HeartTripNotFoundException e){
        return new DefaultErrorResult(404, "HeartedTrip NotFound Error!");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    protected DefaultErrorResult handleNotFoundException(AlarmNotFoundException e){
        return new DefaultErrorResult(404, "Alarm NotFound Error!");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    protected DefaultErrorResult handleNotFoundException(ImgNotFoundException e){
        return new DefaultErrorResult(404, "Img NotFound Error!");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    protected DefaultErrorResult handleNotFoundException(FollowNotFoundException e){
        return new DefaultErrorResult(404, "Follow NotFound Error!");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    protected DefaultErrorResult handleNotFoundException(NoOneFollowException e){
        return new DefaultErrorResult(404, "No one Follow Error!");
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler
    protected DefaultErrorResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        return new DefaultErrorResult(405, "HttpMethod Error!");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    protected DefaultErrorResult handleFileUploadException(FileUploadException e){
        return new DefaultErrorResult(500, "FileUpload Error!");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    protected DefaultErrorResult handleAmazonS3Exception(AmazonS3Exception e){
        return new DefaultErrorResult(500, "AmazonS3 Error!");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    protected DefaultErrorResult handleException(Exception e){
        return new DefaultErrorResult(500, "Global Exception Error!", e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    protected DefaultErrorResult handleException(WrongImgFormatException e){
        return new DefaultErrorResult(500, "WrongImgFormat Exception Error!", e.getMessage());
    }
}
