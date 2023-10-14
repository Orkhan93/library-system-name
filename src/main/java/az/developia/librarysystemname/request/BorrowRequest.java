package az.developia.librarysystemname.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BorrowRequest {

    private Long id;
    private LocalDateTime broughtDate;
    private Long userId;
    private Long bookId;

}