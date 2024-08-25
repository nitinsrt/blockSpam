package com.spamcalls.instahyre.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class SpamReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String reportedPhoneNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User reporter;

    @Column(nullable = false)
    private LocalDateTime reportedAt;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportedPhoneNumber() {
        return reportedPhoneNumber;
    }

    public void setReportedPhoneNumber(String reportedPhoneNumber) {
        this.reportedPhoneNumber = reportedPhoneNumber;
    }

    public User getReporter() {
        return reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public LocalDateTime getReportedAt() {
        return reportedAt;
    }

    public void setReportedAt(LocalDateTime reportedAt) {
        this.reportedAt = reportedAt;
    }

}
