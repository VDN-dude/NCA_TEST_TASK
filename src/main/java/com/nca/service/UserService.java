package com.nca.service;

import com.nca.dto.request.UserCreateRequestDTO;
import com.nca.entity.User;
import com.nca.mapper.UserMapper;
import com.nca.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;

import static com.nca.exception.Message.ENTITY_ALREADY_EXISTS;

/**
 * {@code UserService} for providing business logic with {@link User}.
 */

@Slf4j
@Service
@Transactional
public class UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserService() {
    }

    /**
     * {@code save} for providing save {@link User} business logic with.
     *
     * @param userCreateRequest
     */
    public void save(UserCreateRequestDTO userCreateRequest) {

        if (userRepository.existsByUsername(userCreateRequest.getUsername())) {
            throw new EntityExistsException(
                    String.format(ENTITY_ALREADY_EXISTS.getMessage(),
                            User.class.getSimpleName(),
                            "username"));
        }

        User user = userMapper.toEntity(userCreateRequest);
        user.setPassword(passwordEncoder.encode(RandomStringUtils.random(20, true, true)));

        User saved = userRepository.save(user);
        log.trace("Saved user: {}", saved);

    }
}
