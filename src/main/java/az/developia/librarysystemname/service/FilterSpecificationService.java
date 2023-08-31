package az.developia.librarysystemname.service;

import az.developia.librarysystemname.request.SearchRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilterSpecificationService<T> {

    public Specification<T> getSearchSpecification(SearchRequest searchRequest) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder
                        .like(root.get(searchRequest.getColumn()), "%" + searchRequest.getValue() + "%");
            }
        };
    }

}