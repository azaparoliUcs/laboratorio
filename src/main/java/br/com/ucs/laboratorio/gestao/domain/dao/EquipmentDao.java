package br.com.ucs.laboratorio.gestao.domain.dao;

import br.com.ucs.laboratorio.gestao.domain.entity.EquipmentModel;
import br.com.ucs.laboratorio.gestao.domain.type.EquipmentStatusType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EquipmentDao {

    @Autowired
    private EntityManager entityManager;

    public List<EquipmentModel> findEquipment(Long laboratoryId, Long categoryId, EquipmentStatusType status) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EquipmentModel> query = cb.createQuery(EquipmentModel.class);
        Root<EquipmentModel> root = query.from(EquipmentModel.class);

        query.select(root);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("laboratory").get("id"), laboratoryId));

        if (categoryId != null) {
            predicates.add(cb.equal(root.get("template").get("category").get("id"), categoryId));
        }

        if (status != null) {
            predicates.add(cb.equal(root.get("equipmentStatusType"), status));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }
}
