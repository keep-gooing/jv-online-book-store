package mate.academy.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dao.book.BookDto;
import mate.academy.dao.book.BookDtoWithoutCategoryIds;
import mate.academy.dao.book.CreateBookRequestDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.BookMapper;
import mate.academy.model.Book;
import mate.academy.repository.BookRepository;
import mate.academy.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toEntity(requestDto);
        bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto)
                .getContent();
    }

    @Override
    public BookDto getById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Canʼt find book by id " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto bookDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id " + id));
        bookMapper.updateBookFromDto(bookDto, book);
        bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Can't find category by id " + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findBooksByCategoryId(Long id) {
        List<Book> books = bookRepository.findAllByCategories_Id(id);
        return books.stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
