package az.developia.librarysystemname.service;

import az.developia.librarysystemname.entity.User;
import az.developia.librarysystemname.enums.ErrorCode;
import az.developia.librarysystemname.error.ErrorMessage;
import az.developia.librarysystemname.exception.UserAlreadyExistException;
import az.developia.librarysystemname.mapper.UserMapper;
import az.developia.librarysystemname.repository.UserRepository;
import az.developia.librarysystemname.request.UserRegistrationRequest;
import az.developia.librarysystemname.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse register(@Valid @RequestBody UserRegistrationRequest registrationRequest)
            throws UserAlreadyExistException {
        if (userRepository.findByEmailIgnoreCase(registrationRequest.getEmail()).isPresent() ||
                userRepository.findByUsernameIgnoreCase(registrationRequest.getUsername()).isPresent()) {
            throw UserAlreadyExistException.of(ErrorCode.ALREADY_EXIST.name(), ErrorMessage.USER_ALREADY_EXITS);
        }
        if (validationSignup(registrationRequest)) {
            User user = userRepository.save(userMapper.fromUserRegisterRequestToModel(registrationRequest));
            return userMapper.fromModelToResponse(user);
        } else
            throw UserAlreadyExistException.of(ErrorCode.INTERNAL_SERVER_ERROR.name(), ErrorMessage.SOMETHING_WENT_WRONG);
    }

    private boolean validationSignup(UserRegistrationRequest registrationRequest) {
        log.info("Inside request {}", registrationRequest);
        if (registrationRequest.getUsername() != null && registrationRequest.getPassword() != null
                && registrationRequest.getEmail() != null && registrationRequest.getFirstName() != null
                && registrationRequest.getLastName() != null) {
            return true;
        }
        return false;
    }
}