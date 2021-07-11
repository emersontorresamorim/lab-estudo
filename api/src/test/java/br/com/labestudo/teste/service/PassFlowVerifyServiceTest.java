package br.com.labestudo.teste.service;

import br.com.labestudo.api.exception.PassValidationException;
import br.com.labestudo.api.model.dto.MessageErrorDto;
import br.com.labestudo.api.service.PassFlowVerifyService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PassFlowVerifyServiceTest {

    private final PassFlowVerifyService passFlowVerifyService = new PassFlowVerifyService();

    @ParameterizedTest(name = "Pass: {0}")
    @ValueSource(strings = {"aaBB12-", "1234zZ-@", "1234zZ@1", "1234zZ@1"})
    void WhenPassValid_ThenSuccess(String pass) {
        assertDoesNotThrow(() -> passFlowVerifyService.verifyPass(pass));
    }

    @ParameterizedTest(name = "Invalid pass: {0}")
    @ValueSource(strings = {"aabb12", "1234zz", "1234zz@1", "---@", "@@@[]12", ""})
    void WhenPassInvalid_ThenPassValidationException(String pass) {
        var passValidationException = assertThrows(
                PassValidationException.class,
                () -> passFlowVerifyService.verifyPass(pass)
        );
        Assertions.assertThat(passValidationException.getMessageErrorDtoList())
                .extracting(MessageErrorDto::getKey)
                .containsAnyOf("pass.lenght.invalid", "pass.containsNumber.invalid",
                        "pass.containsUpperCase.invalid", "pass.containsLowerCase.invalid");
    }

}
