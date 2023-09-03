package az.developia.librarysystemname.mapper;

import az.developia.librarysystemname.entity.Library;
import az.developia.librarysystemname.request.LibraryRequest;
import az.developia.librarysystemname.response.LibraryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface LibraryMapper {

    Library fromRequestToModel(LibraryRequest libraryRequest);

    LibraryResponse fromModelToResponse(Library library);

}