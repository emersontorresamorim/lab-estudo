package br.com.labestudo.api.auth.service;

import br.com.labestudo.api.auth.model.entity.User;
import br.com.labestudo.api.auth.repository.UserRepository;
import br.com.labestudo.api.exception.MessageException;
import br.com.labestudo.api.model.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void update(UserDto userDto, Long id) throws MessageException {
        Optional<User> optUser = userRepository.findById(id);
        var user = optUser.orElseThrow(() -> new MessageException("user.errorNotFound"));
        user.setName(userDto.getName());
        userRepository.save(user);
    }
}
