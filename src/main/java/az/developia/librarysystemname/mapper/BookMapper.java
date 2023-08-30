package az.developia.librarysystemname.mapper;

import az.developia.librarysystemname.entity.Book;
import az.developia.librarysystemname.request.BookRequest;
import az.developia.librarysystemname.response.BookResponse;
import az.developia.librarysystemname.wrapper.BookWrapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

    Book fromRequestToModel(BookRequest bookRequest);

    BookResponse fromModelToResponse(Book book);

    List<BookWrapper> fromModelToWrapper(List<Book> book);

}