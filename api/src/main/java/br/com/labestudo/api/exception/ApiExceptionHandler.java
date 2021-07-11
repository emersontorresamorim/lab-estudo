package br.com.labestudo.api.exception;

import br.com.labestudo.api.model.dto.ErrorDto;
import br.com.labestudo.api.model.dto.ErrorsDto;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    public ApiExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var errorsDto = new ErrorsDto();
        ex.getBindingResult().getAllErrors().forEach(objectError -> errorsDto.getErrors().add(montarError(objectError)));
        return handleExceptionInternal(ex, errorsDto, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        InvalidFormatException cause = (InvalidFormatException) ex.getCause();
        return handleExceptionInternal(ex, new ErrorDto(messageSource.getMessage("message.invalid", new String[]{cause.getPath().stream().findFirst().map(JsonMappingException.Reference::getFieldName).orElse("")}, LocaleContextHolder.getLocale())), headers, status, request);
    }

    @ExceptionHandler(MessageException.class)
    public ResponseEntity<Object> handleMessagesException(MessageException ex, WebRequest request) {
        var message = messageSource.getMessage(ex.getKey(), ex.getArgs(), LocaleContextHolder.getLocale());
        var errorsDto = new ErrorsDto();
        errorsDto.setErrors(Collections.singletonList(new ErrorDto(message)));
        return handleExceptionInternal(ex, errorsDto, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(MessagesException.class)
    public ResponseEntity<Object> handleMessagesException(MessagesException ex, WebRequest request) {
        List<ErrorDto> listErrorDto = new ArrayList<>();
        ex.getMessageErrorDtoList().forEach(messagesErrorDto ->
                listErrorDto.add(new ErrorDto(messageSource.getMessage(messagesErrorDto.getKey(), messagesErrorDto.getArgs(), LocaleContextHolder.getLocale()))));
        var errorsDto = new ErrorsDto();
        errorsDto.setErrors(listErrorDto);
        return handleExceptionInternal(ex, errorsDto, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    private ErrorDto montarError(ObjectError objectError) {
        return new ErrorDto(messageSource.getMessage(objectError, LocaleContextHolder.getLocale()));
    }
}
