package com.Udemy.YeoGiDa.global.slack.service;

import com.Udemy.YeoGiDa.domain.comment.entity.Comment;
import com.Udemy.YeoGiDa.domain.comment.exception.CommentNotFoundException;
import com.Udemy.YeoGiDa.domain.comment.repository.CommentRepository;
import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.member.exception.MemberNotFoundException;
import com.Udemy.YeoGiDa.domain.member.repository.MemberRepository;
import com.Udemy.YeoGiDa.domain.place.entity.Place;
import com.Udemy.YeoGiDa.domain.place.exception.PlaceNotFoundException;
import com.Udemy.YeoGiDa.domain.place.repository.PlaceRepository;
import com.Udemy.YeoGiDa.domain.report.repository.ReportRepository;
import com.Udemy.YeoGiDa.domain.report.request.ReportRequestDto;
import com.Udemy.YeoGiDa.domain.trip.entity.Trip;
import com.Udemy.YeoGiDa.domain.trip.exception.TripNotFoundException;
import com.Udemy.YeoGiDa.domain.trip.repository.TripRepository;
import com.slack.api.Slack;
import com.slack.api.model.block.LayoutBlock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.model.block.element.BlockElements.asElements;
import static com.slack.api.model.block.element.BlockElements.button;
import static com.slack.api.webhook.WebhookPayloads.payload;

@Service
@Slf4j
public class SlackService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final TripRepository tripRepository;
    private final PlaceRepository placeRepository;
    private final CommentRepository commentRepository;

    public SlackService(ReportRepository reportRepository, MemberRepository memberRepository,
                         TripRepository tripRepository, PlaceRepository placeRepository,
                         CommentRepository commentRepository) {
        this.reportRepository = reportRepository;
        this.memberRepository = memberRepository;
        this.tripRepository = tripRepository;
        this.placeRepository = placeRepository;
        this.commentRepository = commentRepository;
    }

    @Value("${notification.slack.webhook.url}")
    private String webhookUrl;

    public void stackBlock(ReportRequestDto reportRequestDto) throws IOException {
        List<LayoutBlock> layoutBlocks = new ArrayList<>();
        // 텍스트를 남길 SectionBlock 입니다.
        layoutBlocks.add(section(section -> section.text(markdownText(":alert:  NEW REPORT"))));
        layoutBlocks.add(divider());
        layoutBlocks.add(section(section -> section.text(markdownText("[TYPE] : " + reportRequestDto.getType()))));
        switch (reportRequestDto.getType()) {
            case "MEMBER":
                Member reportedMember = memberRepository.findById(reportRequestDto.getTargetId()).orElseThrow(MemberNotFoundException::new);
                layoutBlocks.add(section(section -> section.text(markdownText("[회원 Id] : " + reportedMember.getId()))));
                layoutBlocks.add(section(section -> section.text(markdownText("[회원 닉네임] : " + reportedMember.getNickname()))));
                layoutBlocks.add(section(section -> section.text(markdownText("[회원 ImgUrl] : " + reportedMember.getMemberImg().getImgUrl()))));
                break;
            case "TRIP":
                Trip reportedTrip = tripRepository.findById(reportRequestDto.getTargetId()).orElseThrow(TripNotFoundException::new);
                layoutBlocks.add(section(section -> section.text(markdownText("[여행지 Id] : " + reportedTrip.getId()))));
                layoutBlocks.add(section(section -> section.text(markdownText("[여행지 제목] : " + reportedTrip.getTitle()))));
                layoutBlocks.add(section(section -> section.text(markdownText("[여행지 소제목] : " + reportedTrip.getSubTitle()))));
                layoutBlocks.add(section(section -> section.text(markdownText("[여행지 ImgUrl] : " + reportedTrip.getTripImg().getImgUrl()))));
                break;
            case "PLACE":
                Place reportedPlace = placeRepository.findById(reportRequestDto.getTargetId()).orElseThrow(PlaceNotFoundException::new);
                layoutBlocks.add(section(section -> section.text(markdownText("[장소 Id] : " + reportedPlace.getId()))));
                layoutBlocks.add(section(section -> section.text(markdownText("[장소 제목] : " + reportedPlace.getTitle()))));
                layoutBlocks.add(section(section -> section.text(markdownText("[장소 리뷰] : " + reportedPlace.getContent()))));
                //TODO: 리스트 처리 해야함 지금은 맨 처음 사진만
                layoutBlocks.add(section(section -> section.text(markdownText("[장소 ImgUrl] : " + reportedPlace.getPlaceImgs().get(0).getImgUrl()))));
                break;
            case "COMMENT":
                Comment reportedComment = commentRepository.findById(reportRequestDto.getTargetId()).orElseThrow(CommentNotFoundException::new);
                layoutBlocks.add(section(section -> section.text(markdownText("[댓글 Id] : " + reportedComment.getId()))));
                layoutBlocks.add(section(section -> section.text(markdownText("[댓글 내용] : " + reportedComment.getContent()))));
                break;
        }
        layoutBlocks.add(divider());
        layoutBlocks.add(
                actions(actions -> actions
                        .elements(asElements(
                                button(b -> b.text(plainText(pt -> pt.emoji(true).text("pass")))
                                        .value(reportRequestDto.getType() + "," + reportRequestDto.getTargetId().toString())
                                        .style("primary")
                                        .actionId("action_pass")
                                ),
                                button(b -> b.text(plainText(pt -> pt.emoji(true).text("delete")))
                                        .value(reportRequestDto.getType() + "," + reportRequestDto.getTargetId().toString())
                                        .style("danger")
                                        .actionId("action_delete")
                                )
                        ))
                )
        );

        Slack.getInstance().send(webhookUrl,
                payload(p -> p
                        .text("슬랙에 메시지를 출력하지 못했습니다.")
                        .blocks(layoutBlocks)
                )
        );
    }
}
