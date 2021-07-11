package br.com.labestudo.api.model.validation;

public interface ValidationGroup {

    interface Default {}

    interface Insert extends Default {}

    interface Update extends Default {}
}
