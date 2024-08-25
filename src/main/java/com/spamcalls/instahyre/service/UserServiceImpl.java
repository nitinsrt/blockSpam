package com.spamcalls.instahyre.service;


import com.spamcalls.instahyre.entities.User;
import com.spamcalls.instahyre.models.UserLoginRequestModel;
import com.spamcalls.instahyre.models.UserRegistrationRequestModel;
import com.spamcalls.instahyre.models.UserSearchResult;
import com.spamcalls.instahyre.repository.SpamRepository;
import com.spamcalls.instahyre.repository.UserRepository;
import com.spamcalls.instahyre.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;
    private final SpamService spamService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, SpamService spamService){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtUtils=jwtUtils;
        this.spamService = spamService;
    }
    @Override
    public String registerUser(UserRegistrationRequestModel userRegistrationRequestModel) {

        if (userRepository.existsByPhoneNumber(userRegistrationRequestModel.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already in use");
        }

        User user = new User();
        user.setName(userRegistrationRequestModel.getUserName());
        user.setPhoneNumber(userRegistrationRequestModel.getPhoneNumber());
        user.setEmail(userRegistrationRequestModel.getEmailId());
        user.setPassword(passwordEncoder.encode(userRegistrationRequestModel.getPassword()));

        userRepository.save(user);

        return jwtUtils.generateToken(userRegistrationRequestModel.getPhoneNumber());

    }

    @Override
    public String loginUser(UserLoginRequestModel userLoginRequestModel) {
        User user = userRepository.findByPhoneNumber(userLoginRequestModel.getPhoneNumber())
                .orElseThrow(() -> new IllegalArgumentException("Invalid phone number or password"));

        if (passwordEncoder.matches(userLoginRequestModel.getPassword(), user.getPassword())) {
            return jwtUtils.generateToken(user.getPhoneNumber());
        } else {
            throw new IllegalArgumentException("Invalid phone number or password");
        }
    }

    @Override
    public List<UserSearchResult> searchUsersByName(String name, User loggedInUser) {
        List<User> usersStartingWith = userRepository.findByNameStartingWithIgnoreCase(name);
        List<User> usersContaining = userRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .filter(user -> !usersStartingWith.contains(user))
                .collect(Collectors.toList());

        return mergeAndSortUsers(usersStartingWith, usersContaining, loggedInUser);
    }

    @Override
    public List<UserSearchResult> searchUsersByPhoneNumber(String phoneNumber, User loggedInUser) {
        Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return List.of(new UserSearchResult(user, spamService.getSpamLikelihood(phoneNumber), loggedInUser));
        } else {
            List<User> usersByPhoneNumber = userRepository.findByNameContainingIgnoreCase(phoneNumber);
            return mergeAndSortUsers(usersByPhoneNumber, List.of(), loggedInUser);
        }
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    private List<UserSearchResult> mergeAndSortUsers(List<User> usersStartingWith, List<User> usersContaining, User loggedInUser) {
        List<UserSearchResult> results = usersStartingWith.stream()
                .map(user -> new UserSearchResult(user, spamService.getSpamLikelihood(user.getPhoneNumber()), loggedInUser))
                .collect(Collectors.toList());

        results.addAll(usersContaining.stream()
                .map(user -> new UserSearchResult(user, spamService.getSpamLikelihood(user.getPhoneNumber()), loggedInUser))
                .collect(Collectors.toList()));

        return results;
    }
}
