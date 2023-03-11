package com.buana.itemdata.controller.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.buana.itemdata.dto.CustomResponse;
import com.buana.itemdata.dto.UserSaveRequest;
import com.buana.itemdata.model.user.Role;
import com.buana.itemdata.model.user.User;
import com.buana.itemdata.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService usersService;

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        List<User> allUser = usersService.getUsers();
        CustomResponse response = new CustomResponse();
        response.setHttpCode(HttpStatus.OK.value());
        response.setResponseCode(200);
        response.setResponseMessage("SUCCESS");
        response.setData(allUser);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpCode()));
    }

    @PostMapping("/users/save")
    public ResponseEntity<?> saveUsers(@Valid @RequestBody UserSaveRequest users) {
        URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/save").toUriString());
        CustomResponse response = usersService.saveUsers(users);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpCode()));
    }

    @PostMapping("/role/save")
    public ResponseEntity<?> saveRoles(@RequestBody Role roles) {
        URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/roles/save").toUriString());
        CustomResponse response = usersService.saveRoles(roles);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpCode()));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addRoleToUsers(@RequestBody RoleToUsersForm form) {
        CustomResponse response = usersService.addRolesToUsers(form.getUsername(), form.getRoleName());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpCode()));
    }

    @PostMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null & authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                User user = usersService.getUsers(username);
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + /*10 * 60 * 1000*/1000 * 60 * 60 * 1))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);

                CustomResponse response1 =  new CustomResponse(HttpStatus.OK.value(), 200, "SUCCESS", access_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), response1);

            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }

    }

}

@Data
class RoleToUsersForm {
    private String username;
    private String roleName;
}
