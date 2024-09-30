package com.nca.controller;

import com.nca.dto.request.AuthenticationRequestDTO;
import com.nca.dto.request.UserCreateRequestDTO;
import com.nca.dto.response.AuthenticationResponseDTO;
import com.nca.entity.User;
import com.nca.service.AuthenticationService;
import com.nca.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("/authentication")
public class AuthenticationController {

    @Autowired
    public AuthenticationService authenticationService;
    @Autowired
    public UserService userService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity<AuthenticationResponseDTO> login(
            @RequestBody @Valid AuthenticationRequestDTO authenticationRequestDTO) {

        return new ResponseEntity(authenticationService.requestAuthentication(authenticationRequestDTO),
                HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "registration", method = RequestMethod.POST)
    public ResponseEntity<Void> register(@RequestBody @Valid UserCreateRequestDTO userCreateRequest) {

        userService.save(userCreateRequest);
        return new ResponseEntity(HttpStatus.OK);
    }
}
