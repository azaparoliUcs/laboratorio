package br.com.ucs.laboratorio.gestao.infrastructure.repository;

import br.com.ucs.laboratorio.gestao.domain.entity.BlockModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<BlockModel, Long> {
}
