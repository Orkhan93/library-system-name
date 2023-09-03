package az.developia.librarysystemname.controller;

import az.developia.librarysystemname.constant.LibraryConstant;
import az.developia.librarysystemname.request.LibraryRegistration;
import az.developia.librarysystemname.request.LibraryRequest;
import az.developia.librarysystemname.service.LibraryService;
import az.developia.librarysystemname.util.LibraryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/library")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @PostMapping("/add/{userId}")
    public ResponseEntity<String> addLibrary(@PathVariable(name = "userId") Long userId,
                                             @RequestBody LibraryRequest libraryRequest) {
        try {
            return libraryService.addLibrary(userId, libraryRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return LibraryUtil.getResponseMessage(LibraryConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/register/{checkId}")
    public ResponseEntity<String> register(
            @RequestBody LibraryRegistration registrationRequest,
            @PathVariable(name = "checkId") Long checkId) {
        try {
            return libraryService.register(registrationRequest, checkId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}