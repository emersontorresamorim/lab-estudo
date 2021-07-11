package br.com.labestudo.teste.controller;

import br.com.labestudo.api.auth.service.UserService;
import br.com.labestudo.api.controller.UserController;
import br.com.labestudo.api.exception.ApiExceptionHandler;
import br.com.labestudo.api.exception.MessageException;
import br.com.labestudo.teste.fixture.SelfRegisterFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static br.com.labestudo.teste.util.JsonConvertionUtils.asJsonString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    public static final String USER_UPDATE_REQUEST = "/user/{id}";
    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private MessageSource messageSource;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .setControllerAdvice(new ApiExceptionHandler(messageSource))
                .build();
    }

    @Test
    void whenPUTIsCalledThenUserIsUpdate() throws Exception {
        // given
        var userDto = SelfRegisterFixture.validUserDto();
        // when
        doNothing().when(userService).update(userDto, 1L);
        // then
        mockMvc.perform(put(USER_UPDATE_REQUEST,"1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenPUTIsCalledWithWrongNameThenAnExceptionShouldBeThrown() throws Exception {
        // given
        var userDto = SelfRegisterFixture.invalidNameUserDto();
        // then
        mockMvc.perform(put(USER_UPDATE_REQUEST,"1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPUTIsCalledWithInvalidIdThenAnExceptionShouldBeThrown() throws Exception {
        // given
        var userDto = SelfRegisterFixture.validUserDto();
        // when
        doThrow(MessageException.class).when(userService).update(userDto, 2L);
        // then
        mockMvc.perform(put(USER_UPDATE_REQUEST,"2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDto)))
                .andExpect(status().isBadRequest());
    }

    
}
