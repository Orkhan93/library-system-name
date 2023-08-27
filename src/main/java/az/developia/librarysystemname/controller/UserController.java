package az.developia.librarysystemname.controller;

import az.developia.librarysystemname.constant.LibraryConstant;
import az.developia.librarysystemname.enums.ErrorCode;
import az.developia.librarysystemname.error.ErrorMessage;
import az.developia.librarysystemname.exception.UserAlreadyExistException;
import az.developia.librarysystemname.request.UserRegistrationRequest;
import az.developia.librarysystemname.service.UserService;
import az.developia.librarysystemname.util.LibraryUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegistrationRequest registrationRequest) throws UserAlreadyExistException {
        try {
            userService.register(registrationRequest);
            return LibraryUtil.getResponseMessage(LibraryConstant.SUCCESSFULLY_REGISTER, HttpStatus.OK);
        } catch (UserAlreadyExistException ex) {
            throw new UserAlreadyExistException(ErrorCode.ALREADY_EXIST.name(), ErrorMessage.USER_ALREADY_EXITS);
        }
    }

}