package br.com.ucs.laboratorio.gestao.domain.dao;

import br.com.ucs.laboratorio.gestao.domain.entity.EventModel;
import br.com.ucs.laboratorio.gestao.domain.type.EventType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class EventDao {

    @Autowired
    private EntityManager entityManager;

    public List<EventModel> findEventsByEquipments(List<Long> equipmentIds,
                                                   EventType eventType,
                                                   LocalDate startDate,
                                                   LocalDate endDate) {

        if (equipmentIds == null || equipmentIds.isEmpty()) return Collections.emptyList();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EventModel> query = cb.createQuery(EventModel.class);
        Root<EventModel> root = query.from(EventModel.class);
        query.select(root);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(root.get("equipment").get("id").in(equipmentIds));

        if (eventType != null) {
            predicates.add(cb.equal(root.get("status"), eventType));
        }

        if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("requestDate"), startDate));
        }

        if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("requestDate"), endDate));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }
}
