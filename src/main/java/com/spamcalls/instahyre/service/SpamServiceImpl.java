package com.spamcalls.instahyre.service;

import com.spamcalls.instahyre.entities.SpamReport;
import com.spamcalls.instahyre.entities.User;
import com.spamcalls.instahyre.repository.SpamRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SpamServiceImpl implements SpamService{

    private final SpamRepository spamRepository;

    public SpamServiceImpl(SpamRepository spamRepository) {
        this.spamRepository = spamRepository;
    }
    @Override
    public void markSpam(String phoneNumber, User reporter) {
        SpamReport spamReport = new SpamReport();
        spamReport.setReportedPhoneNumber(phoneNumber);
        spamReport.setReporter(reporter);
        spamReport.setReportedAt(LocalDateTime.now());

        spamRepository.save(spamReport);
    }

    @Override
    public int getSpamLikelihood(String phoneNumber) {
        List<SpamReport> reports = spamRepository.findByReportedPhoneNumber(phoneNumber);

        long totalReports = reports.size();
        if (totalReports == 0) {
            return 0; // No reports, so 0% likelihood
        }

        return Math.min((int) ((totalReports / 100.0) * 100), 100);
    }
}
