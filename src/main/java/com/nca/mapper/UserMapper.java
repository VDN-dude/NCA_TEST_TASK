package com.nca.mapper;

import com.nca.dto.request.UserCreateRequestDTO;
import com.nca.entity.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * {@code NewsMapper} for mapping {@link User} to DTOs and vice versa.
 */

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * {@code toEntity} creating new {@link User} and mapping fields from provided {@link UserCreateRequestDTO}.
     *
     * @param userCreateRequestDTO
     */
    User toEntity(UserCreateRequestDTO userCreateRequestDTO);

}
