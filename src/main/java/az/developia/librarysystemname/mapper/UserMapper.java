package az.developia.librarysystemname.mapper;

import az.developia.librarysystemname.entity.User;
import az.developia.librarysystemname.request.UserRegistrationRequest;
import az.developia.librarysystemname.request.UserRequest;
import az.developia.librarysystemname.response.UserResponse;
import az.developia.librarysystemname.wrapper.UserWrapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User fromUserRegisterRequestToModel(UserRegistrationRequest userRegistrationRequest);

    UserResponse fromModelToResponse(User user);

    User fromToRequestToModel(UserRequest userRequest);

    List<UserWrapper> fromModelToWrapper(List<User> book);


}