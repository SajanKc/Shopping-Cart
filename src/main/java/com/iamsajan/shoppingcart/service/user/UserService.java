package com.iamsajan.shoppingcart.service.user;

import com.iamsajan.shoppingcart.dto.request.user.CreateUserRequest;
import com.iamsajan.shoppingcart.dto.request.user.UpdateUserRequest;
import com.iamsajan.shoppingcart.dto.request.user.UserDto;
import com.iamsajan.shoppingcart.exceptions.AlreadyExistsException;
import com.iamsajan.shoppingcart.exceptions.ResourceNotFoundException;
import com.iamsajan.shoppingcart.model.User;
import com.iamsajan.shoppingcart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    @Override
    public User createUser(CreateUserRequest createUserRequest) {
        return Optional.of(createUserRequest)
                .filter(user -> !userRepository.existsByEmail(createUserRequest.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setFirstName(createUserRequest.getFirstName());
                    user.setLastName(createUserRequest.getLastName());
                    user.setEmail(createUserRequest.getEmail());
                    user.setPassword(createUserRequest.getPassword().toCharArray());
                    return userRepository.save(user);
                }).orElseThrow(() -> new AlreadyExistsException("Oops!" + createUserRequest.getEmail() + " already exists!"));
    }

    @Override
    public User updateUser(UpdateUserRequest updateUserRequest, Long userId) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setFirstName(updateUserRequest.getFirstName());
            existingUser.setLastName(updateUserRequest.getLastName());

            return userRepository.save(existingUser);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete, () -> {
            throw new ResourceNotFoundException("User not found!");
        });
    }

    @Override
    public UserDto convertUserToDto(User user) {
        return UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }
}
