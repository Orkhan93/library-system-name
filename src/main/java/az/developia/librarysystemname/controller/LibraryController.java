package az.developia.librarysystemname.controller;

import az.developia.librarysystemname.entity.Library;
import az.developia.librarysystemname.request.LibraryRegistration;
import az.developia.librarysystemname.request.LibraryRequest;
import az.developia.librarysystemname.response.LibraryResponse;
import az.developia.librarysystemname.service.LibraryService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/library")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @PostMapping("/add/{userId}")
    public ResponseEntity<String> addLibrary(@PathVariable(name = "userId") Long userId,
                                             @RequestBody LibraryRequest libraryRequest) {
            return libraryService.addLibrary(userId, libraryRequest);
    }

    @PostMapping("/register/{checkId}")
    public ResponseEntity<String> register(
            @RequestBody LibraryRegistration registrationRequest,
            @PathVariable(name = "checkId") Long checkId) {
            return libraryService.register(registrationRequest, checkId);
    }

    @GetMapping("/get/{checkId}")
    public ResponseEntity<List<Library>> getLibraryByLibrarianId(@PathVariable(name = "checkId") Long checkId) {
            return libraryService.getLibraryByLibrarianId(checkId);
    }

    @PutMapping("/{checkId}/update/{libraryId}")
    public LibraryResponse update(@PathVariable(name = "checkId") Long checkId, @RequestBody LibraryRequest libraryRequest,
                                  @PathVariable(name = "libraryId") Long libraryId) {
        return libraryService.update(checkId, libraryRequest, libraryId);
    }

    @DeleteMapping("/{checkId}/delete/{libraryId}")
    public ResponseEntity<String> delete(@PathVariable(name = "checkId") Long checkId,
                                         @PathVariable(name = "libraryId") Long libraryId) {
            return libraryService.deleteLibrary(checkId, libraryId);
    }

}