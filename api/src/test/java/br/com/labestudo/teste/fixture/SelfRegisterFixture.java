package br.com.labestudo.teste.fixture;

import br.com.labestudo.api.model.dto.UserDto;
import br.com.labestudo.api.model.entity.Parameter;
import br.com.labestudo.api.model.entity.SelfRegisterUser;

import java.time.OffsetDateTime;
import java.util.UUID;

public class SelfRegisterFixture {

    public static UserDto validUserDto() {
        return UserDto.builder().email("vinicionagel@gmail.com")
                .name("vinicio-nagel")
                .pass("Abc123")
                .build();
    }

    public static SelfRegisterUser validSelfRegisterUser() {
        return SelfRegisterUser.builder().email("vinicionagel@gmail.com")
                .name("vinicio nagel")
                .pass("tesT@123")
                .id(UUID.randomUUID().toString())
                .created(OffsetDateTime.now())
                .build();
    }

    public static SelfRegisterUser invalidfRegisterUser() {
        return SelfRegisterUser.builder().email("vinicionagel@gmail.com")
                .name("vinicio nagel")
                .pass("tesT@123")
                .id(UUID.randomUUID().toString())
                .created(OffsetDateTime.now().minusHours(13))
                .build();
    }

    public static UserDto invalidEmailUserDto() {
        return UserDto.builder().email("vvsdfdsfds")
                .name("vinicio nagel")
                .build();
    }

    public static UserDto invalidNameUserDto() {
        return UserDto.builder().email("vvsdfdsfds")
                .build();
    }

    public static Parameter validParameter() {
        return new Parameter();
    }

    public static UserDto emptyUserDto() {
        return UserDto.builder().build();
    }

}
