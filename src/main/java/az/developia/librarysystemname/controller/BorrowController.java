package az.developia.librarysystemname.controller;

import az.developia.librarysystemname.request.BorrowRequest;
import az.developia.librarysystemname.response.BorrowResponse;
import az.developia.librarysystemname.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/borrow")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;

    @PostMapping("/brought/{checkId}")
    public BorrowResponse broughtBook(@RequestBody BorrowRequest borrowRequest, @PathVariable(name = "checkId") Long checkId) {
        return borrowService.broughtBook(borrowRequest, checkId);
    }

    @GetMapping("/get/{userId}")
    public List<BorrowResponse> getBroughtBooksByUserId(@PathVariable(name = "userId") Long userId) {
        return borrowService.getBroughtBooksByUserId(userId);
    }

}