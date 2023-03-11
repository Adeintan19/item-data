package com.buana.itemdata.service.user;

import com.buana.itemdata.dto.CustomResponse;
import com.buana.itemdata.dto.UserSaveRequest;
import com.buana.itemdata.model.user.Role;
import com.buana.itemdata.model.user.User;
import com.buana.itemdata.repository.user.RoleRepository;
import com.buana.itemdata.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userDao;
    private final RoleRepository roleDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User in database:{}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public CustomResponse saveUsers(UserSaveRequest request) {
        try {
            Optional<User> byUsernameOrEmail = userDao.findByUsernameOrEmail(request.getUsername(), request.getEmail());
            if (byUsernameOrEmail.isPresent()) {
                return new CustomResponse(HttpStatus.BAD_REQUEST.value(), 400, "username or email already exist", null);
            }
            User user = new User();
            log.info("saving new user{} to the database", request.getName());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEmail(request.getEmail());
            user.setName(request.getName());
            user.setUsername(request.getUsername());
            userDao.save(user);
            return new CustomResponse(HttpStatus.OK.value(), 200, "SUCCESS", user);
        } catch (Exception e) {
            return new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), 400, e.getMessage(), null);
        }
    }

    @Override
    public CustomResponse saveRoles(Role reqRole) {
        try {
            log.info("saving new role{} to the database", reqRole.getName());
            Role byName = roleDao.findByName(reqRole.getName());
            if (byName != null) {
                return new CustomResponse(HttpStatus.BAD_REQUEST.value(), 400, "Role already exist", null);
            }
            Role role1 = new Role();
            role1.setName(reqRole.getName());
            roleDao.save(role1);
            return new CustomResponse(HttpStatus.OK.value(), 200, "SUCCESS", role1);
        } catch (Exception e) {
            return new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), 400, e.getMessage(), null);
        }
    }

    @Override
    public CustomResponse addRolesToUsers(String username, String roleName) {
        log.info("Adding role{}to user{}", roleName, username);
        User users = userDao.findByUsername(username);
        Role roles = roleDao.findByName(roleName);
        users.getRoles().add(roles);
        return new CustomResponse(HttpStatus.OK.value(), 200, "SUCCESS", null);
    }

    @Override
    public User getUsers(String username) {
        log.info("Fecting user{}", username);
        return userDao.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        log.info("Fecting all users{}");
        return userDao.findAll();
    }


}
