package mate.academy.controller;

import static mate.academy.util.TestUtil.createBookResponseDto;
import static mate.academy.util.TestUtil.getSecondBookIsbn;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.dao.book.BookDto;
import mate.academy.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

@Sql(scripts = {"classpath:database/add-books-to-book-table.sql",
        "classpath:database/add-category-to-categories-table.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:database/delete-books-categories-relations.sql",
        "classpath:database/delete-categories-from-category-table.sql",
        "classpath:database/delete-books-from-book-table.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;
    private static final Long validId = 1L;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext context) {
        mockMvc = webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "User", roles = "USER")
    @DisplayName("Get book by id")
    void getBook_validId_Success() throws Exception {
        //GIVEN
        BookDto expected = createBookResponseDto();
        expected.setIsbn(getSecondBookIsbn());
        //WHEN
        MvcResult result = mockMvc.perform(
                        get("/books/{id}", validId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //THEN
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class);
        assertNotNull(actual);
        compare(expected, actual);
    }

    @Test
    @WithMockUser(username = "User", roles = "USER")
    @DisplayName("Get book by invalid id")
    void getBook_invalidId_Fail() throws Exception {
        //GIVEN
        Long invalidId = -1L;
        //WHEN
        mockMvc.perform(
                        get("/books/{id}", invalidId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof EntityNotFoundException));
        //THEN
    }

    @Test
    @WithMockUser(username = "User", roles = "USER")
    @DisplayName("Get all books")
    void getBooks_valid_Success() throws Exception {
        // WHEN
        MvcResult result = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        // THEN
        String json = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(json);
        JsonNode contentNode = rootNode.get("content");

        BookDto[] actual = objectMapper.readValue(contentNode.toString(), BookDto[].class);
        assertNotNull(actual);
        assertEquals(4, actual.length);
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("Delete book by id")
    void deleteBook_validId_Success() throws Exception {
        //GIVEN
        //WHEN
        mockMvc.perform(delete("/books/{id}", validId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
        //THEN
    }

    void compare(BookDto expected, BookDto actual) {
        assertEquals(expected.getIsbn(), actual.getIsbn());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getAuthor(), actual.getAuthor());
        assertEquals(0, expected.getPrice().compareTo(actual.getPrice()));
    }
}
