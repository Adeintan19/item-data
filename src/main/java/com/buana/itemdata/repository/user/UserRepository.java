package com.buana.itemdata.repository.user;

import com.buana.itemdata.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
   User findByUsername(String username);
   Optional<User> findByUsernameOrEmail(String username, String email);
   @Query("SELECT u FROM User u WHERE u.username=:username")
   User getUserByUsername(@Param("username") String username);

   @Query("UPDATE User u SET u.failedAttempt=?1 WHERE u.username = ?2")
   @Modifying
   void updateFailedAttempts(int failAttempts, String username);
}
