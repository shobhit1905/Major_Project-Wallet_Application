package com.majorproject.user_service.repository;

import com.majorproject.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM wallet_users  WHERE user_full_name LIKE %?%" , nativeQuery = true)
    User findUserByName(String name) ;
}
