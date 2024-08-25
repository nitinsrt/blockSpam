package com.spamcalls.instahyre.service;

import com.spamcalls.instahyre.entities.User;
import com.spamcalls.instahyre.models.UserLoginRequestModel;
import com.spamcalls.instahyre.models.UserRegistrationRequestModel;
import com.spamcalls.instahyre.models.UserSearchResult;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    public String registerUser(UserRegistrationRequestModel userRegistrationRequestModel);

    public String loginUser(UserLoginRequestModel userLoginRequestModel);

    public List<UserSearchResult> searchUsersByName(String name, User loggedInUser);

    public List<UserSearchResult> searchUsersByPhoneNumber(String phoneNumber, User loggedInUser);
    User findUserById(Long userId);

}
