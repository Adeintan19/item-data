package com.buana.itemdata.auth.security;

import com.buana.itemdata.dto.CustomResponse;
import com.buana.itemdata.model.user.User;
import com.buana.itemdata.repository.user.UserRepository;
import com.buana.itemdata.service.user.CustomUserServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;


@Component
@Slf4j
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Autowired
    private CustomUserServices customUserServices;
    @Autowired
    private UserRepository userDao;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");
        customUserServices.resetFailedAttemptWhenTimeExpired(username);
        User user = userDao.getUserByUsername(username);
        String loginFailed = "Username / Password doesn't match, please check your password and try again.";

        if (user != null) {
            if (user.isEnabled() && user.isAccountNonLocked()) {
                if (user.getFailedAttempt() < CustomUserServices.MAX_FAILED_ATTEMPTS - 1) {
                    customUserServices.increaseFailedAttempts(user);

                    CustomResponse response1 = new CustomResponse();
                    response1.setHttpCode(HttpStatus.BAD_REQUEST.value());
                    response1.setResponseCode(400);
                    response1.setResponseMessage(loginFailed);
                    response1.setData(null);
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), response1);
                } else {
                    customUserServices.lock(user);
                    CustomResponse response1 = new CustomResponse();
                    response1.setHttpCode(HttpStatus.BAD_REQUEST.value());
                    response1.setResponseCode(400);
                    response1.setResponseMessage("Your account has been locked due to 3 failed attempts."
                            + " It will be unlocked after 5 minutes.");
                    response1.setData(null);

                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), response1);
                }
            } else if (!user.isAccountNonLocked()) {
                if (customUserServices.unlockWhenTimeExpired(user)) {
                    CustomResponse response1 = new CustomResponse();
                    response1.setHttpCode(HttpStatus.BAD_REQUEST.value());
                    response1.setResponseCode(400);
                    response1.setResponseMessage("Your account has been unlocked. Please try to login again.");
                    response1.setData(null);
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), response1);
                } else {
                    CustomResponse response1 = new CustomResponse();
                    response1.setHttpCode(HttpStatus.FORBIDDEN.value());
                    response1.setResponseCode(403);
                    response1.setResponseMessage("Your account locked,please try again later.");
                    response1.setData(null);
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), response1);
                }
            }

        }
        super.setDefaultFailureUrl("login?error");
    }


}
