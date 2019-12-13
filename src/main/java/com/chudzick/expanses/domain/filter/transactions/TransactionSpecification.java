package com.chudzick.expanses.domain.filter.transactions;

import com.chudzick.expanses.domain.filter.SearchCriteria;
import com.chudzick.expanses.domain.filter.SearchOperation;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class TransactionSpecification<T> implements Specification<T> {

    private SearchCriteria criteria;

    TransactionSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperation().equals(SearchOperation.GRATER_THAN)) {
            return builder.greaterThanOrEqualTo(
                    root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN)) {
            return builder.lessThanOrEqualTo(
                    root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equals(SearchOperation.EQUALITY)) {
            return builder.equal(root.get(criteria.getKey()), criteria.getValue());
        } else if (criteria.getOperation().equals(SearchOperation.LIKE)) {
            return builder.like(
                    root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
        }
        return null;
    }

}
