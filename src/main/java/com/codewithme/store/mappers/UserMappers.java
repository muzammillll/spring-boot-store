package com.codewithme.store.mappers;

import com.codewithme.store.dtos.RegisterUserRequest;
import com.codewithme.store.dtos.UpdateUserRequest;
import com.codewithme.store.dtos.UserDto;
import com.codewithme.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMappers {


    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);

    void update(UpdateUserRequest request,@MappingTarget User user);
}
