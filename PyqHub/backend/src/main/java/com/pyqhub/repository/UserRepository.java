package com.pyqhub.repository;

import com.pyqhub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByCollegeId(String collegeId);
    Optional<User> findByEmail(String email);
    boolean existsByCollegeId(String collegeId);
    boolean existsByEmail(String email);
}
