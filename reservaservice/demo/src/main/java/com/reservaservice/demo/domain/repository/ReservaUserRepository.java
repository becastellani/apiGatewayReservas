package com.reservaservice.demo.domain.repository;

import com.reservaservice.demo.domain.model.ReservaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservaUserRepository extends JpaRepository<ReservaUser, Long> {
    Optional<ReservaUser> findByExternalUserId(Long externalUserId);
    boolean existsByExternalUserId(Long externalUserId);
}