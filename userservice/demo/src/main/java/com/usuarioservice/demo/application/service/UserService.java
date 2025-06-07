package com.usuarioservice.demo.application.service;

import com.usuarioservice.demo.domain.model.User;
import com.usuarioservice.demo.infrastructure.messaging.UserEventPublisher;
import com.usuarioservice.demo.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private UserEventPublisher eventPublisher;

    public List<User> listar() {
        return repository.findAll();
    }

    public User salvar(User user) {

        User savedUser = repository.save(user);

        eventPublisher.publishUserCreated(savedUser);

        return savedUser;
    }

    public Optional<User> buscarPorEmail(String email) {
        return repository.findByEmail(email);
    }
}