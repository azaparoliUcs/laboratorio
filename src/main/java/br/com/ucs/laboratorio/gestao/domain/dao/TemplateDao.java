package br.com.ucs.laboratorio.gestao.domain.dao;

import br.com.ucs.laboratorio.gestao.domain.entity.TemplateModel;
import br.com.ucs.laboratorio.gestao.domain.type.TemplateType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TemplateDao {

    @Autowired
    private EntityManager entityManager;

    public List<TemplateModel> filterTemplates(String brand, TemplateType templateType, Long categoryId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TemplateModel> cq = cb.createQuery(TemplateModel.class);
        Root<TemplateModel> template = cq.from(TemplateModel.class);

        List<Predicate> predicates = new ArrayList<>();

        if (brand != null && !brand.isEmpty()) {
            predicates.add(cb.equal(template.get("brand"), brand));
        }

        if (templateType != null) {
            predicates.add(cb.equal(template.get("templateType"), templateType));
        }

        if (categoryId != null) {
            predicates.add(cb.equal(template.get("category").get("id"), categoryId));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        TypedQuery<TemplateModel> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
