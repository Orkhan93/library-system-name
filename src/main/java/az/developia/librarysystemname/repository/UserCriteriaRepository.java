package az.developia.librarysystemname.repository;

import az.developia.librarysystemname.entity.User;
import az.developia.librarysystemname.request.UserCriteriaSearch;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserCriteriaRepository {

    private final EntityManager entityManager;

    public List<User> findUsersByFirstNameAndLastName(UserCriteriaSearch search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);

        Root<User> root = criteriaQuery.from(User.class);
        Predicate firstNamePredicate = criteriaBuilder.like(root.get("firstName"), "%" + search.getFirstName() + "%");
        Predicate lastNamePredicate = criteriaBuilder.like(root.get("lastName"), "%" + search.getLastName() + "%");
        criteriaQuery.where(firstNamePredicate, lastNamePredicate);

        TypedQuery<User> userTypedQuery = entityManager.createQuery(criteriaQuery);
        return userTypedQuery.getResultList();
    }

}