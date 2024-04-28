package com.jhm.springbootwebservice.domain.validation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailValidationRepository extends JpaRepository<EmailValidation, Long> {
    EmailValidation findByEmail(String email);
}
