package com.Udemy.YeoGiDa.domain.trip.exception;

import org.apache.tomcat.util.http.fileupload.FileUploadException;

public class TripImgEssentialException extends FileUploadException {
    public TripImgEssentialException() {
        super("여행지 사진 한장은 필수입니다.");
    }
}
