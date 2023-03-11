package com.buana.itemdata.service.user;


import com.buana.itemdata.dto.CustomResponse;
import com.buana.itemdata.dto.UserSaveRequest;
import com.buana.itemdata.model.user.Role;
import com.buana.itemdata.model.user.User;


import java.util.List;

public interface UserService {
    CustomResponse saveUsers(UserSaveRequest user);
    CustomResponse saveRoles(Role role);
    CustomResponse addRolesToUsers(String username,String roleName);
    User getUsers(String username);
    List<User> getUsers();
}
