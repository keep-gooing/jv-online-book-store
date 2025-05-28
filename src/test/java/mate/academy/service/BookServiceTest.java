package mate.academy.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.dao.book.BookDto;
import mate.academy.dao.book.CreateBookRequestDto;
import mate.academy.mapper.BookMapper;
import mate.academy.model.Book;
import mate.academy.repository.BookRepository;
import mate.academy.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Find all books")
    void findAllBooks_returnBookDtoList() {
        //GIVEN
        Book firstBook = new Book();
        Book secondBook = new Book();
        Page<Book> bookPage = new PageImpl<>(List.of(firstBook, secondBook));
        Pageable pageable = Pageable.ofSize(1);

        BookDto firstBookDto = new BookDto();
        BookDto secondBookDto = new BookDto();
        final List<BookDto> expected = List.of(firstBookDto, secondBookDto);
        //WHEN
        when(bookMapper.toDto(firstBook)).thenReturn(firstBookDto);
        when(bookMapper.toDto(secondBook)).thenReturn(secondBookDto);
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        List<BookDto> actual = bookService.findAll(pageable);
        //THEN
        Assertions.assertEquals(expected, actual);
        verify(bookRepository, Mockito.times(1)).findAll(pageable);
        verify(bookMapper, Mockito.times(1)).toDto(firstBook);
        verify(bookMapper, Mockito.times(1)).toDto(secondBook);
    }

    @Test
    @DisplayName("Save valid book")
    void saveBook_validRequest_returnBookDto() {
        //GIVEN
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        Book book = new Book();
        BookDto expected = new BookDto();
        //WHEN
        when(bookMapper.toEntity(bookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);
        BookDto actual = bookService.save(bookRequestDto);
        //THEN
        Assertions.assertEquals(expected, actual);
        verify(bookRepository, Mockito.times(1)).save(book);
        verify(bookMapper, Mockito.times(1)).toEntity(bookRequestDto);
        verify(bookMapper, Mockito.times(1)).toDto(book);
    }

    @Test
    @DisplayName("Find book by valid id")
    void findBook_validId_Success() {
        //GIVEN
        Long id = 1L;
        Book book = new Book();
        BookDto expected = new BookDto();
        //WHEN
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);
        BookDto actual = bookService.getById(id);
        //THEN
        Assertions.assertEquals(expected, actual);
        verify(bookRepository, Mockito.times(1)).findById(id);
        verify(bookMapper, Mockito.times(1)).toDto(book);
    }
}
