package com.spamcalls.instahyre.models;

import com.spamcalls.instahyre.entities.User;

public class UserSearchResult {

    private User user;
    private int spamLikelihood;
    private boolean emailVisible;

    public UserSearchResult(User user, int spamLikelihood, User loggedInUser) {
        this.user = user;
        this.spamLikelihood = spamLikelihood;
        this.emailVisible = isEmailVisible(user, loggedInUser);
    }

    private boolean isEmailVisible(User user, User loggedInUser) {
        return user.getEmail() != null && user.getContacts().stream()
                .anyMatch(contact -> contact.getUser().equals(loggedInUser));
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getSpamLikelihood() {
        return spamLikelihood;
    }

    public void setSpamLikelihood(int spamLikelihood) {
        this.spamLikelihood = spamLikelihood;
    }

    public boolean isEmailVisible() {
        return emailVisible;
    }

    public void setEmailVisible(boolean emailVisible) {
        this.emailVisible = emailVisible;
    }

}
