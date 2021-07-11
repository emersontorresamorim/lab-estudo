package br.com.labestudo.teste.service;

import br.com.labestudo.api.auth.model.entity.User;
import br.com.labestudo.api.auth.repository.UserRepository;
import br.com.labestudo.api.auth.service.UserService;
import br.com.labestudo.api.exception.MessageException;
import br.com.labestudo.api.model.dto.UserDto;
import br.com.labestudo.api.model.mapper.UserMapper;
import br.com.labestudo.teste.fixture.SelfRegisterFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private UserMapper userMapperInjection;


    @Test
    void whenInformedValidUserDtoThenUpdateUser() {
        UserDto userDto = SelfRegisterFixture.validUserDto();

        userMapperInjection = UserMapper.INSTANCE;

        User userReturn = userMapperInjection.toUser(userDto);

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(userReturn));
        when(userRepository.save(userReturn)).thenReturn(userReturn);

        assertDoesNotThrow(() -> userService.update(userDto, 1L));
    }

    @Test
    void whenInformedInvalidUserThenUpdateUser() {
        UserDto userDto = SelfRegisterFixture.validUserDto();

        userMapperInjection = UserMapper.INSTANCE;

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MessageException.class, () -> userService.update(userDto, 1L));
    }



}
