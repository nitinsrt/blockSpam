package com.spamcalls.instahyre.service;

import com.spamcalls.instahyre.entities.User;

public interface SpamService {

    public void markSpam(String phoneNumber, User reporter);

    public int getSpamLikelihood(String phoneNumber);

}
