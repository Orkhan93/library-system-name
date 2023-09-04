package az.developia.librarysystemname.repository;

import az.developia.librarysystemname.entity.Library;
import az.developia.librarysystemname.wrapper.LibraryWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibraryRepository extends JpaRepository<Library, Long> {

    List<Library> findByUsers_Id(Long id);

    List<LibraryWrapper> getAllLibrary();

    Optional<Library> findByName(String name);

}