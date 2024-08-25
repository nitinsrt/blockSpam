package com.spamcalls.instahyre.repository;

import com.spamcalls.instahyre.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<User> findByPhoneNumber(String phoneNumber);
    List<User> findByNameStartingWithIgnoreCase(String start);
    List<User> findByNameContainingIgnoreCase(String contains);
    Optional<User> findById(Long id);
   // List<User> findByNameStartingWithOrContaining(String name);
}
