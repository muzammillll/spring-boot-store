package com.codewithme.store.mappers;

import com.codewithme.store.dtos.RegisterUserRequest;
import com.codewithme.store.dtos.UpdateUserRequest;
import com.codewithme.store.dtos.UserDto;
import com.codewithme.store.entities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-14T01:14:02+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23 (Oracle Corporation)"
)
@Component
public class UserMappersImpl implements UserMappers {

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String email = null;
        String password = null;

        id = user.getId();
        name = user.getName();
        email = user.getEmail();
        password = user.getPassword();

        UserDto userDto = new UserDto( id, name, email, password );

        return userDto;
    }

    @Override
    public User toEntity(RegisterUserRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.name( request.getName() );
        user.email( request.getEmail() );
        user.password( request.getPassword() );

        return user.build();
    }

    @Override
    public void update(UpdateUserRequest request, User user) {
        if ( request == null ) {
            return;
        }

        user.setName( request.getName() );
        user.setEmail( request.getEmail() );
    }
}
