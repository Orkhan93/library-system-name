package az.developia.librarysystemname.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookWrapper {

    private Long id;
    private String name;
    private String description;
    private String price;
    private String status;
    private Long userId;
    private String userName;

}