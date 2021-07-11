package br.com.labestudo.teste.controller;

import br.com.labestudo.api.controller.SelfRegisterController;
import br.com.labestudo.api.exception.ApiExceptionHandler;
import br.com.labestudo.api.exception.SelfRegisterFailedValidationException;
import br.com.labestudo.api.model.dto.HashDto;
import br.com.labestudo.api.service.SelfRegisterService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SelfRegisterControllerTest {

    private static final String SELF_REGISTER_API_URL_PATH = "/selfregister";

    private MockMvc mockMvc;

    @Mock
    private SelfRegisterService selfRegisterService;

    @InjectMocks
    private SelfRegisterController selfRegisterController;

    @Mock
    private MessageSource messageSource;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(selfRegisterController)
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .setControllerAdvice(new ApiExceptionHandler(messageSource))
                .build();
    }

    @Test
    void whenPUTIsCalledThenASelfRegisterUserIsCreated() throws Exception {
        // given
        var userDto = SelfRegisterFixture.validUserDto();
        // when
        when(selfRegisterService.selfRegister(userDto)).thenReturn(userDto);
        // then
        mockMvc.perform(put(SELF_REGISTER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void whenPutIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        var userDto = SelfRegisterFixture.emptyUserDto();
        // then
        mockMvc.perform(put(SELF_REGISTER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPutIsCalledWithWrongEmailThenAnErrorIsReturned() throws Exception {
        // given
        var userDto = SelfRegisterFixture.invalidEmailUserDto();
        // then
        mockMvc.perform(put(SELF_REGISTER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPostIsCalledWithWrongHashThenAnErrorIsReturned() throws Exception {
        // given
        var hashDto = new HashDto("invalidHash");
        // when
        doThrow(SelfRegisterFailedValidationException.class).when(selfRegisterService).validateAccount(hashDto);
        // then
        mockMvc.perform(post(SELF_REGISTER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(hashDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPostIsCalledWithValidHashThenAnOkIsReturned() throws Exception {
        // given
        var hashDto = new HashDto("validHash");
        // then
        mockMvc.perform(post(SELF_REGISTER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(hashDto)))
                .andExpect(status().isOk());
    }

    @Test
    void whenPostIsCalledWithEmptyHashThenAnErrorIsReturned() throws Exception {
        // given
        var hashDto = new HashDto("");
        // then
        mockMvc.perform(post(SELF_REGISTER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(hashDto)))
                .andExpect(status().isBadRequest());
    }

}
