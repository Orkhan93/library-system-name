package az.developia.librarysystemname.controller;

import az.developia.librarysystemname.entity.Library;
import az.developia.librarysystemname.request.LibraryRegistration;
import az.developia.librarysystemname.request.LibraryRequest;
import az.developia.librarysystemname.response.LibraryResponse;
import az.developia.librarysystemname.service.LibraryService;
import az.developia.librarysystemname.util.LibraryUtil;
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

import static az.developia.librarysystemname.constant.LibraryConstant.SOMETHING_WENT_WRONG;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

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
        return LibraryUtil.getResponseMessage(SOMETHING_WENT_WRONG, INTERNAL_SERVER_ERROR);
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

    @GetMapping("/get/{checkId}")
    public ResponseEntity<List<Library>> getLibraryByLibrarianId(@PathVariable(name = "checkId") Long checkId) {
        try {
            return libraryService.getLibraryByLibrarianId(checkId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
    }

    @PutMapping("/{checkId}/update/{libraryId}")
    public LibraryResponse update(@PathVariable(name = "checkId") Long checkId, @RequestBody LibraryRequest libraryRequest,
                                  @PathVariable(name = "libraryId") Long libraryId) {
        return libraryService.update(checkId, libraryRequest, libraryId);
    }

    @DeleteMapping("/{checkId}/delete/{libraryId}")
    public ResponseEntity<String> delete(@PathVariable(name = "checkId") Long checkId,
                                         @PathVariable(name = "libraryId") Long libraryId) {
        try {
            return libraryService.deleteLibrary(checkId, libraryId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return LibraryUtil.getResponseMessage(SOMETHING_WENT_WRONG, INTERNAL_SERVER_ERROR);
    }

}