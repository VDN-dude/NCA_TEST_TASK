package com.nca.dto.request;

import com.nca.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestDTO {

    @NotNull
    @Size(min = 1, max = 20)
    private String name;

    @NotNull
    @Size(min = 1, max = 20)
    private String surname;

    @NotNull
    @Size(min = 1, max = 20)
    private String parentName;

    @NotNull
    @Size(min = 1, max = 40)
    private String username;

    @NotNull
    private UserRole userRole;
}
