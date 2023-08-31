package az.developia.librarysystemname.controller;

import az.developia.librarysystemname.entity.Book;
import az.developia.librarysystemname.mapper.BookMapper;
import az.developia.librarysystemname.repository.BookRepository;
import az.developia.librarysystemname.request.FilterRequest;
import az.developia.librarysystemname.service.FilterSpecificationService;
import az.developia.librarysystemname.wrapper.BookWrapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/filter")
@RequiredArgsConstructor
public class FilterController {

    private final FilterSpecificationService<Book> specificationService;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @PostMapping("/specification")
    public List<BookWrapper> getBooksWithFilter(@RequestBody FilterRequest filterRequest) {
        Specification<Book> specification = specificationService.getSearchSpecification(filterRequest.getSearchRequest());
        return bookMapper.fromModelToWrapper(bookRepository.findAll(specification));
    }

}