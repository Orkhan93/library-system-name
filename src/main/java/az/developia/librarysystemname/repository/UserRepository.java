package az.developia.librarysystemname.repository;

import az.developia.librarysystemname.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByEmail(String email);

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByUsernameIgnoreCase(String username);

    List<User> findByFirstName(String firstName);
    List<User> findByLastName(String lastName);
    List<User> findByStatus(String status);

    List<User> findByLibrary_Id(Long id);

    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);

}