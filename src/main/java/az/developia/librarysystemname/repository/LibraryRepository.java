package az.developia.librarysystemname.repository;

import az.developia.librarysystemname.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibraryRepository extends JpaRepository<Library, Long> {

    Optional<Library> findByName(String name);

}