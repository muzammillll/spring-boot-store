package com.codewithme.store.controllers;

import com.codewithme.store.dtos.ChangePasswordRequest;
import com.codewithme.store.dtos.RegisterUserRequest;
import com.codewithme.store.dtos.UpdateUserRequest;
import com.codewithme.store.dtos.UserDto;
import com.codewithme.store.entities.Role;
import com.codewithme.store.mappers.UserMappers;
import com.codewithme.store.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserMappers userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, UserMappers userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping
    public Iterable<UserDto> getAllUsers(
            @RequestParam(required = false, defaultValue = "", name = "sort") String sort) {

        if (!Set.of("name", "email").contains(sort))
            sort = "name";
        return userRepository.findAll(Sort.by(sort)).
                stream().
                map(userMapper::toDto).
                toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);

        if (user == null) {

            return ResponseEntity.notFound().build();
        }


        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest request,
                                              UriComponentsBuilder uriBuilder) {

       if (userRepository.existsByEmail(request.getEmail()) )
       {
           return ResponseEntity.badRequest().body(
                   Map.of("email", "email is already registered")
           );
       }

        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        var userDto = userMapper.toDto(user);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateUserRequest request
    ) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        userMapper.update(request, user);
        userRepository.save(user);

        return ResponseEntity.ok(userMapper.toDto(user));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        userRepository.delete(user);
        return ResponseEntity.noContent().build();

    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest request) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (!user.getPassword().equals(request.getOldPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);

        return ResponseEntity.noContent().build();

    }

}