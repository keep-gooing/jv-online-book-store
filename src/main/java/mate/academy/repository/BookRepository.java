package mate.academy.repository;

import java.util.List;
import mate.academy.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByCategories_Id(Long categoryId);

    void deleteAll();
}
