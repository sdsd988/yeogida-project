
package com.Udemy.YeoGiDa.domain.comment.service;

import com.Udemy.YeoGiDa.domain.alarm.entity.Alarm;
import com.Udemy.YeoGiDa.domain.alarm.entity.AlarmType;
import com.Udemy.YeoGiDa.domain.alarm.repository.AlarmRepository;
import com.Udemy.YeoGiDa.domain.comment.entity.Comment;
import com.Udemy.YeoGiDa.domain.comment.exception.CommentNotFoundException;
import com.Udemy.YeoGiDa.domain.comment.repository.CommentRepository;
import com.Udemy.YeoGiDa.domain.comment.request.CommentSaveRequestDto;
import com.Udemy.YeoGiDa.domain.comment.response.CommentListResponseDto;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.exception.MemberNotFoundException;
import com.Udemy.YeoGiDa.domain.place.entity.Place;
import com.Udemy.YeoGiDa.domain.place.exception.PlaceNotFoundException;
import com.Udemy.YeoGiDa.domain.place.repository.PlaceRepository;
import com.Udemy.YeoGiDa.global.exception.ForbiddenException;
import com.Udemy.YeoGiDa.global.fcm.service.FirebaseCloudMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final PlaceRepository placeRepository;
    private final CommentRepository commentRepository;
    private final AlarmRepository alarmRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @Transactional(readOnly = true)
    public List<CommentListResponseDto> getCommentListByDesc(Long placeId){

        return commentRepository.findAllByPlaceByIdDesc(placeId)
                .stream()
                .map(CommentListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentListResponseDto> getCommentListByAsc(Long placeId){

        return commentRepository.findAllByPlaceByIdAsc(placeId)
                .stream()
                .map(CommentListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Page<CommentListResponseDto> getTest(Long placeId,
                                                int page,
                                                int size,
                                                String condition){

        Pageable pageable = PageRequest.of(page, size);
        return commentRepository.test(placeId,pageable,condition);

    }

    public CommentListResponseDto save(CommentSaveRequestDto commentSaveRequestDto, Long placeId, Member member) throws IOException {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(PlaceNotFoundException::new);

        if(member == null){
            throw new MemberNotFoundException();
        }

        Comment comment = Comment.builder() 
                .content(commentSaveRequestDto.getContent())
                .member(member)
                .place(place)
                .build();

        Comment saveComment = commentRepository.save(comment);

        //알람 추가
        if(!place.getTrip().getMember().getNickname().equals(member.getNickname())) {
            alarmRepository.save(new Alarm(
                    place.getTrip().getMember(),
                    AlarmType.NEW_COMMENT,
                    member.getId(),
                    null,
                    place.getTrip().getId(),
                    placeId,
                    comment.getId()
            ));
        }

        //푸쉬 알림 보내기
        if(!place.getTrip().getMember().getNickname().equals(member.getNickname())) {
            firebaseCloudMessageService.sendMessageTo(place.getTrip().getMember().getDeviceToken(),
                    "여기다", member.getNickname() + AlarmType.NEW_COMMENT.getAlarmText(),
                    "NEW_COMMENT",
                    place.getTrip().getId().toString() + "," + place.getId().toString());
        }

        return new CommentListResponseDto(saveComment);
    }

    public void delete(Long commentId, Member member) {

        if(member == null){
            throw new MemberNotFoundException();
        }

        Comment comment = Optional.ofNullable(commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException())).get();

        if(comment.getMember().getId() != member.getId()) {
            throw new ForbiddenException();
        }

        Place place = comment.getPlace();

        if(place==null){
            throw new PlaceNotFoundException();
        }

        commentRepository.delete(comment);

        //알림 삭제
        if(!place.getTrip().getMember().getNickname().equals(member.getNickname())) {
            Alarm findAlarm = alarmRepository.findAlarmByCommentIdAndMakeMemberId(commentId, member.getId())
                    .orElseThrow(CommentNotFoundException::new);
            alarmRepository.delete(findAlarm);
        }
    }
}
