package com.chudzick.expanses.domain.filter.transactions;

import com.chudzick.expanses.domain.filter.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransactionSpecificationBuilder<T> {

    private final List<SearchCriteria> params;

    public TransactionSpecificationBuilder(List<SearchCriteria> params) {
        this.params = params;
    }

    public TransactionSpecificationBuilder with(SearchCriteria searchCriteria) {
        params.add(searchCriteria);
        return this;
    }

    public Specification<T> build() {
        List<Specification<T>> specs = params.stream()
                .map((Function<SearchCriteria, TransactionSpecification<T>>) TransactionSpecification::new)
                .collect(Collectors.toList());

        Specification<T> result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }

        return result;
    }
}
