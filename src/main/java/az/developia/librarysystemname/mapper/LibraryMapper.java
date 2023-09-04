package az.developia.librarysystemname.mapper;

import az.developia.librarysystemname.entity.Library;
import az.developia.librarysystemname.request.LibraryRequest;
import az.developia.librarysystemname.response.LibraryResponse;
import az.developia.librarysystemname.wrapper.LibraryWrapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface LibraryMapper {

    Library fromRequestToModel(LibraryRequest libraryRequest);

    LibraryResponse fromModelToResponse(Library library);

    List<LibraryWrapper> fromModelToWrapper(List<Library> libraries);

}