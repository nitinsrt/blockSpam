package com.spamcalls.instahyre.repository;

import com.spamcalls.instahyre.entities.SpamReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpamRepository extends JpaRepository<SpamReport, Long> {

    List<SpamReport> findByReportedPhoneNumber(String phoneNumber);

}
