package br.com.labestudo.teste.service;

import br.com.labestudo.api.auth.model.entity.User;
import br.com.labestudo.api.auth.repository.UserRepository;
import br.com.labestudo.api.exception.SelfRegisterFailedValidationException;
import br.com.labestudo.api.exception.UserRegisteredException;
import br.com.labestudo.api.model.dto.HashDto;
import br.com.labestudo.api.model.dto.UserDto;
import br.com.labestudo.api.model.entity.SelfRegisterUser;
import br.com.labestudo.api.model.mapper.UserMapper;
import br.com.labestudo.api.repository.ParameterRepository;
import br.com.labestudo.api.repository.SelfRegisterRepository;
import br.com.labestudo.api.service.SelfRegisterService;
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
class SelfRegisterServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SelfRegisterRepository selfRegisterRepository;

    @Mock
    private ParameterRepository parameterRepository;

    @InjectMocks
    private SelfRegisterService selfRegisterService;

    @Mock
    private UserMapper userMapperInjection;

    @Test
    void whenAlreadyRegisteredUserInformedThenAnExceptionShouldBeThrown() {
        UserDto userDto = SelfRegisterFixture.validUserDto();
        userMapperInjection = UserMapper.INSTANCE;
        User duplicatedUser = userMapperInjection.toUser(userDto);
        duplicatedUser.setId(1L);

        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(duplicatedUser));

        assertThrows(UserRegisteredException.class, () -> selfRegisterService.selfRegister(userDto));
    }

    @Test
    void whenInformedValidHashThenRegisterNewUser() {
        SelfRegisterUser selfRegisterUser = SelfRegisterFixture.validSelfRegisterUser();

        when(selfRegisterRepository.findById(selfRegisterUser.getId())).thenReturn(Optional.of(selfRegisterUser));

        User user = userMapperInjection.toUser(selfRegisterUser);

        when(parameterRepository.findById("accountConfirmationPeriod")).thenReturn(Optional.of(SelfRegisterFixture.validParameter()));

        when(userRepository.save(userMapperInjection.toUser(selfRegisterUser))).thenReturn(user);

        assertDoesNotThrow(() -> selfRegisterService.validateAccount(new HashDto(selfRegisterUser.getId())));
    }


    @Test
    void whenInformedNonExistentHashThenAnExceotionBeThrown() {
        when(selfRegisterRepository.findById("abacate")).thenReturn(Optional.empty());
        assertThrows(SelfRegisterFailedValidationException.class, () -> selfRegisterService.validateAccount(new HashDto("abacate")));
    }

    @Test
    void whenInformedValidHashButInvalidTimeThenAnExceptionBeThorown() {
        SelfRegisterUser selfRegisterUser = SelfRegisterFixture.invalidfRegisterUser();

        when(selfRegisterRepository.findById(selfRegisterUser.getId())).thenReturn(Optional.of(selfRegisterUser));

        when(parameterRepository.findById("accountConfirmationPeriod")).thenReturn(Optional.of(SelfRegisterFixture.validParameter()));

        assertThrows(SelfRegisterFailedValidationException.class, () -> selfRegisterService.validateAccount(new HashDto(selfRegisterUser.getId())));
    }


}
