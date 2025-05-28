package br.com.ucs.laboratorio.gestao.infrastructure.repository;

import br.com.ucs.laboratorio.gestao.domain.entity.LaboratoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaboratoryRepository extends JpaRepository<LaboratoryModel, Long> {
}
