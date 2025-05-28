package mate.academy.service;

import java.util.List;
import mate.academy.dao.book.BookDto;
import mate.academy.dao.book.BookDtoWithoutCategoryIds;
import mate.academy.dao.book.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    Page<BookDto> findAll(Pageable pageable);

    BookDto getById(Long id);

    BookDto update(Long id, CreateBookRequestDto bookDto);

    void delete(Long id);

    List<BookDtoWithoutCategoryIds> findBooksByCategoryId(Long id);
}
