package com.reservaservice.demo.application.service;

import com.reservaservice.demo.domain.model.ReservaUser;
import com.reservaservice.demo.domain.repository.ReservaUserRepository;
import com.reservaservice.demo.infrastructure.messaging.dto.UserEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ReservaUserService {

    @Autowired
    private ReservaUserRepository reservaUserRepository;

    @Transactional
    public ReservaUser createLocalUser(UserEventDto userDto) {
        if (reservaUserRepository.existsByExternalUserId(userDto.getId())) {
            System.out.println("Usuário já existe localmente: " + userDto.getId());
            return updateLocalUser(userDto);
        }

        ReservaUser reservaUser = new ReservaUser();
        reservaUser.setExternalUserId(userDto.getId());
        reservaUser.setNomeCompleto(userDto.getNome() != null ? userDto.getNome().getNome() : "");

        ReservaUser savedUser = reservaUserRepository.save(reservaUser);
        System.out.println("Usuário criado localmente: " + savedUser.getNomeCompleto());

        return savedUser;
    }

    @Transactional
    public ReservaUser updateLocalUser(UserEventDto userDto) {
        Optional<ReservaUser> optionalUser = reservaUserRepository.findByExternalUserId(userDto.getId());

        if (optionalUser.isPresent()) {
            ReservaUser existingUser = optionalUser.get();

            existingUser.setNomeCompleto(userDto.getNome() != null ? userDto.getNome().getNome() : existingUser.getNomeCompleto());

            ReservaUser updatedUser = reservaUserRepository.save(existingUser);
            System.out.println("Usuário atualizado localmente: " + updatedUser.getNomeCompleto());

            return updatedUser;
        }

        return createLocalUser(userDto);
    }

    public Optional<ReservaUser> findByExternalUserId(Long externalUserId) {
        return reservaUserRepository.findByExternalUserId(externalUserId);
    }
}