package br.com.ucs.laboratorio.gestao.infrastructure.repository;

import br.com.ucs.laboratorio.gestao.domain.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    Optional<UserModel> findByEmail(String email);
}
