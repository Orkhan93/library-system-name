package az.developia.librarysystemname.mapper;

import az.developia.librarysystemname.entity.Borrow;
import az.developia.librarysystemname.request.BorrowRequest;
import az.developia.librarysystemname.response.BorrowResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface BorrowMapper {

    BorrowResponse modelToResponse(Borrow borrow);

    BorrowResponse requestToResponse(BorrowRequest borrowRequest);

    Borrow requestToModel(BorrowRequest borrowRequest);

    List<BorrowResponse> listModelToListResponse(List<Borrow> borrows);

}