package com.iamsajan.shoppingcart.service.user;

import com.iamsajan.shoppingcart.dto.request.user.CreateUserRequest;
import com.iamsajan.shoppingcart.dto.request.user.UpdateUserRequest;
import com.iamsajan.shoppingcart.dto.request.user.UserDto;
import com.iamsajan.shoppingcart.model.User;

public interface IUserService {
    User getUserById(Long id);

    User createUser(CreateUserRequest createUserRequest);

    User updateUser(UpdateUserRequest updateUserRequest, Long userId);

    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);
}
