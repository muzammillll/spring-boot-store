package com.codewithme.store.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    public String name;
    public String email;
}
