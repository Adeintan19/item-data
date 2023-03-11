package com.buana.itemdata.service.user;

import com.buana.itemdata.model.user.User;
import com.buana.itemdata.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@Slf4j
@Transactional
public class CustomUserServices {

    public static final int MAX_FAILED_ATTEMPTS = 3;

    private static final long LOCK_TIME_DURATION = 300000; // 5 minute 5 * 60 * 1 * 1000;

    private static final long FAILED_TIME_DURATION = 300000; // 5 minute 5 * 60 * 1 * 1000;

    @Autowired
    private UserRepository repo;

    public void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempt() + 1;
        user.setFailedTime(new Date());
        user.setFailedAttempt(newFailAttempts);
        repo.save(user);

    }

    public void resetFailedAttempts(String username) {
        repo.updateFailedAttempts(0, username);
    }

    public User getUser(String username) {
        return repo.getUserByUsername(username);
    }

    public void lock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        repo.save(user);
    }

    public boolean unlockWhenTimeExpired(User user) {
        if (user.getLockTime() != null) {
            long lockTimeInMillis = user.getLockTime().getTime();
            long currentTimeInMillis = System.currentTimeMillis();

            if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
                user.setAccountNonLocked(true);
                user.setLockTime(null);
                user.setFailedAttempt(0);
                repo.save(user);
                return true;
            }
        } else if (user.isAccountNonLocked()) {
            return true;
        }

        return false;
    }

    public void resetFailedAttemptWhenTimeExpired(String username) {
        User user = repo.findByUsername(username);
        if (user.getFailedTime() != null) {
            long lockTimeInMillis = user.getFailedTime().getTime();
            long currentTimeInMillis = System.currentTimeMillis();

            if (lockTimeInMillis + FAILED_TIME_DURATION < currentTimeInMillis) {
                user.setFailedTime(new Date());
                user.setFailedAttempt(0);
                repo.save(user);
            }
        }
    }
}
