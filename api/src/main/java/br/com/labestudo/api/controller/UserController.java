package br.com.labestudo.api.controller;

import br.com.labestudo.api.auth.service.UserService;
import br.com.labestudo.api.exception.MessageException;
import br.com.labestudo.api.model.dto.UserDto;
import br.com.labestudo.api.model.validation.ValidationGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated() and @loggedUserValidator.isLoggedUserRequesting(#id)")
    public void update(@RequestBody @Validated(value = ValidationGroup.Update.class) UserDto userDto, @PathVariable Long id) throws MessageException {
        userService.update(userDto, id);
    }
}
