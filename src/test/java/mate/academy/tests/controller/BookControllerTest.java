package mate.academy.tests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.dao.book.BookDto;
import mate.academy.dao.book.CreateBookRequestDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "admin", roles = "ADMIN")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = "classpath:database/products/delete-first-book.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("database/books/add-three-books.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        tearDown(dataSource);
    }

    @Test
    @DisplayName("Create a new book")
    void createBook_ValidRequestDto_Success() throws Exception {
        //Given
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setAuthor("John Doe")
                .setTitle("Dog")
                .setDescription("Happy Dog")
                .setPrice(BigDecimal.ONE)
                .setIsbn("123456789")
                .setCoverImage("112233")
                .setCategoryIds(Set.of(1L, 2L, 3L));

        CreateBookRequestDto expected = new CreateBookRequestDto()
                .setAuthor(requestDto.getAuthor())
                .setTitle(requestDto.getTitle())
                .setDescription(requestDto.getDescription())
                .setPrice(requestDto.getPrice())
                .setIsbn(requestDto.getIsbn())
                .setCoverImage(requestDto.getCoverImage())
                .setCategoryIds(requestDto.getCategoryIds());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        //Then
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(expected.getAuthor(), actual.getAuthor());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getPrice(), actual.getPrice());
        Assertions.assertEquals(expected.getIsbn(), actual.getIsbn());
        Assertions.assertEquals(expected.getCoverImage(), actual.getCoverImage());
        Assertions.assertEquals(expected.getCategoryIds(), actual.getCategoryIds());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get all books")
    void getAll_GivenBooksInCatalog_ShouldReturnAllBooks() throws Exception {
        // When
        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto[] actualBooks = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                BookDto[].class
        );

        Assertions.assertEquals(3, actualBooks.length);

        List<String> titles = List.of("Cat", "Apple", "Flower");
        List<String> authors = List.of("John Harvy", "Mark Harvy", "Hanna Wolly");
        List<String> isbns = List.of("123456788", "123456787", "123456786");

        for (BookDto book : actualBooks) {
            Assertions.assertTrue(titles.contains(book.getTitle()), "Unexpected title: " + book.getTitle());
            Assertions.assertTrue(authors.contains(book.getAuthor()), "Unexpected author: " + book.getAuthor());
            Assertions.assertTrue(isbns.contains(book.getIsbn()), "Unexpected ISBN: " + book.getIsbn());

            Assertions.assertNotNull(book.getId());
            Assertions.assertNotNull(book.getPrice());
            Assertions.assertNotNull(book.getDescription());
            Assertions.assertNotNull(book.getCoverImage());
            Assertions.assertNotNull(book.getCategoryIds());
        }
    }
}

    /*
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get specific book", description = "Get specific book")
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getById(id);
    }
     */
}
