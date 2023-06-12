package com.Udemy.YeoGiDa.domain.report.controller;

import com.Udemy.YeoGiDa.domain.comment.entity.Comment;
import com.Udemy.YeoGiDa.domain.comment.exception.CommentNotFoundException;
import com.Udemy.YeoGiDa.domain.comment.repository.CommentRepository;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.exception.MemberNotFoundException;
import com.Udemy.YeoGiDa.domain.member.repository.MemberRepository;
import com.Udemy.YeoGiDa.domain.member.service.MemberService;
import com.Udemy.YeoGiDa.domain.place.entity.Place;
import com.Udemy.YeoGiDa.domain.place.exception.PlaceNotFoundException;
import com.Udemy.YeoGiDa.domain.place.repository.PlaceRepository;
import com.Udemy.YeoGiDa.domain.report.request.ReportRequestDto;
import com.Udemy.YeoGiDa.domain.report.service.ReportService;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import com.Udemy.YeoGiDa.domain.trip.exception.TripNotFoundException;
import com.Udemy.YeoGiDa.domain.trip.repository.TripRepository;
import com.Udemy.YeoGiDa.global.response.success.DefaultResult;
import com.Udemy.YeoGiDa.global.response.success.StatusCode;
import com.Udemy.YeoGiDa.global.security.annotation.LoginMember;
import com.Udemy.YeoGiDa.global.slack.service.SlackService;
import com.slack.api.Slack;
import com.slack.api.app_backend.interactive_components.ActionResponseSender;
import com.slack.api.app_backend.interactive_components.payload.BlockActionPayload;
import com.slack.api.app_backend.interactive_components.response.ActionResponse;
import com.slack.api.util.json.GsonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.slack.api.model.block.Blocks.section;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ReportController {

    private final ReportService reportService;
    private final SlackService slackService;
    private final MemberRepository memberRepository;
    private final TripRepository tripRepository;
    private final PlaceRepository placeRepository;
    private final CommentRepository commentRepository;
    private final MemberService memberService;

    public ReportController(ReportService reportService, SlackService slackService, MemberRepository memberRepository, TripRepository tripRepository, PlaceRepository placeRepository, CommentRepository commentRepository, MemberService memberService) {
        this.reportService = reportService;
        this.slackService = slackService;
        this.memberRepository = memberRepository;
        this.tripRepository = tripRepository;
        this.placeRepository = placeRepository;
        this.commentRepository = commentRepository;
        this.memberService = memberService;
    }

    @PostMapping("/report")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity report(@LoginMember Member member,
                                  @RequestBody ReportRequestDto reportRequestDto) throws IOException {
        reportService.report(member, reportRequestDto);
        return new ResponseEntity(DefaultResult.res(StatusCode.OK,
                "신고 성공 "), HttpStatus.OK);
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void delete(@RequestParam String payload) throws IOException {
        // Json String -> BlockActionPayload 변경
        BlockActionPayload blockActionPayload =
                GsonFactory.createSnakeCase()
                        .fromJson(payload, BlockActionPayload.class);

        // Block 수정
        blockActionPayload.getMessage().getBlocks().remove(0);
        blockActionPayload.getActions().forEach(action -> {
            String[] value = action.getValue().split(",");
            String type = value[0];
            Long targetId = Long.parseLong(value[1]);

            if (action.getActionId().equals("action_pass")) {
                blockActionPayload.getMessage().getBlocks().add(0,
                        section(section ->
                                section.text(markdownText("신고를 *보류* 하였습니다."))
                        )
                );
            } else {
                blockActionPayload.getMessage().getBlocks().add(0,
                        section(section ->
                                section.text(markdownText("해당 신고물을 *삭제* 하였습니다."))
                        )
                );
                switch (type) {
                    case "MEMBER":
                        Member reportedMember = memberRepository.findById(targetId).orElseThrow(MemberNotFoundException::new);
//                        memberService.setDefaultNicknameAndImage(reportedMember);
                        memberRepository.delete(reportedMember);
                        break;
                    case "TRIP":
                        Trip reportedTrip = tripRepository.findById(targetId).orElseThrow(TripNotFoundException::new);
                        tripRepository.delete(reportedTrip);
                        break;
                    case "PLACE":
                        Place reportedPlace = placeRepository.findById(targetId).orElseThrow(PlaceNotFoundException::new);
                        placeRepository.delete(reportedPlace);
                        break;
                    case "COMMENT":
                        Comment reportedComment = commentRepository.findById(targetId).orElseThrow(CommentNotFoundException::new);
                        commentRepository.delete(reportedComment);
                        break;
                }
            }
        });

        // 재전송할 응답 객체 생성
        ActionResponse response =
                ActionResponse.builder()
                        .replaceOriginal(true)
                        .blocks(blockActionPayload.getMessage().getBlocks())
                        .build();

        Slack slack = Slack.getInstance();
        ActionResponseSender sender = new ActionResponseSender(slack);
        sender.send(blockActionPayload.getResponseUrl(), response);

        log.info("slack 버튼 api 성공");
    }

}
