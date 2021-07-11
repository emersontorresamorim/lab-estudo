package br.com.labestudo.teste.fixture;

import br.com.labestudo.api.model.entity.SelfRegisterUser;
import br.com.labestudo.api.repository.SelfRegisterRepository;
import org.springframework.stereotype.Component;

@Component
public class SelfRegisterRepositoryFixture {

    private SelfRegisterRepository selfRegisterRepository;

    public SelfRegisterRepositoryFixture(SelfRegisterRepository selfRegisterRepository) {
        this.selfRegisterRepository = selfRegisterRepository;
    }

    public SelfRegisterUser get() {
        return selfRegisterRepository.save(SelfRegisterFixture.validSelfRegisterUser());
    }

    public void deleteAll() {
        selfRegisterRepository.deleteAll();
    }

}
