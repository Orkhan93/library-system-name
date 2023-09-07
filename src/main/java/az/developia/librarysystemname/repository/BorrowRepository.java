package az.developia.librarysystemname.repository;

import az.developia.librarysystemname.entity.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    Borrow findByBook_Id(Long id);

    List<Borrow> findByUser_Id(Long id);

}