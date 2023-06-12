package com.Udemy.YeoGiDa.domain.report.service;

import com.Udemy.YeoGiDa.domain.member.entity.Member;
import com.Udemy.YeoGiDa.domain.report.entity.Report;
import com.Udemy.YeoGiDa.domain.report.repository.ReportRepository;
import com.Udemy.YeoGiDa.domain.report.request.ReportRequestDto;
import com.Udemy.YeoGiDa.global.slack.service.SlackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional
@Slf4j
public class ReportService {

    private final ReportRepository reportRepository;
    private final SlackService slackService;

    public ReportService(ReportRepository reportRepository, SlackService slackService) {
        this.reportRepository = reportRepository;
        this.slackService = slackService;
    }

    public void report(Member member, ReportRequestDto reportRequestDto) throws IOException {
        reportRepository.save(new Report(member, reportRequestDto.getType(), reportRequestDto.getTargetId()));

        slackService.stackBlock(reportRequestDto);
    }

}
