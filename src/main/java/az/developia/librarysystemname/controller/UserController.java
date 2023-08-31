package az.developia.librarysystemname.controller;

import az.developia.librarysystemname.constant.LibraryConstant;
import az.developia.librarysystemname.error.ErrorMessage;
import az.developia.librarysystemname.exception.UserAlreadyExistException;
import az.developia.librarysystemname.request.UserLoginRequest;
import az.developia.librarysystemname.request.UserRegistrationRequest;
import az.developia.librarysystemname.request.UserRequest;
import az.developia.librarysystemname.response.AuthenticationResponse;
import az.developia.librarysystemname.response.UserResponse;
import az.developia.librarysystemname.service.UserService;
import az.developia.librarysystemname.util.LibraryUtil;
import az.developia.librarysystemname.wrapper.UserWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegistrationRequest registrationRequest)
            throws UserAlreadyExistException {
        try {
            userService.register(registrationRequest);
            return LibraryUtil.getResponseMessage(LibraryConstant.SUCCESSFULLY_REGISTER, HttpStatus.OK);
        } catch (UserAlreadyExistException ex) {
            return new ResponseEntity<String>(ErrorMessage.USER_ALREADY_EXITS, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@Valid @RequestBody UserLoginRequest userLogin) {
        String jwt = userService.login(userLogin);
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            AuthenticationResponse response = new AuthenticationResponse();
            response.setJwtToken(jwt);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/updateStatus/{userId}")
    public ResponseEntity<String> updateStatus(@PathVariable Long userId, @RequestBody UserRegistrationRequest registrationRequest) {
        try {
            return userService.updateStatus(userId, registrationRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return LibraryUtil.getResponseMessage(LibraryConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/get/{userId}")
    public UserResponse getUserById(@PathVariable(name = "userId") Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/get/firstname/{firstName}")
    public List<UserWrapper> getUserByFirstName(@PathVariable(name = "firstName") String firstName) {
        return userService.getUserByFirstName(firstName);
    }

    @GetMapping("/get/lastName/{lastName}")
    public List<UserWrapper> getUserByLastName(@PathVariable(name = "lastName") String lastName) {
        return userService.getUserByLastName(lastName);
    }

    @GetMapping("/get/status/{status}")
    public List<UserWrapper> getUserByStatus(@PathVariable(name = "status") String status) {
        return userService.getUserByStatus(status);
    }

    @PutMapping("/update/{userId}")
    public UserResponse updateUser(@PathVariable Long userId,
                                   @RequestBody UserRequest request) {
        return userService.updateUser(userId, request);
    }

    @PutMapping("/update/{checkId}/{userId}")
    public ResponseEntity<UserResponse> updateUserByLibrarian(@PathVariable(name = "checkId") Long checkId,
                                                              @PathVariable(name = "userId") Long userId,
                                                              @RequestBody UserRequest userRequest) {
        return userService.updateUserByLibrarian(checkId, userId, userRequest);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> delete(@PathVariable(name = "userId") Long userId) {
        return userService.delete(userId);
    }

}