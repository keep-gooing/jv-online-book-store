package mate.academy.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.dao.category.CategoryDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.util.TestUtil;
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

@Sql(scripts = {"classpath:/database/add-category-to-categories-table.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:/database/delete-categories-from-category-table.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
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
    @DisplayName("Get category by id")
    void getCategory_validId_Success() throws Exception {
        //GIVEN
        //WHEN
        MvcResult result = mockMvc.perform(
                        get("/categories/{id}", validId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //THEN
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto.class);
        assertNotNull(actual);
        assertTrue(reflectionEquals(TestUtil.createCategoryResponseDto(), actual, "id"));
    }

    @Test
    @WithMockUser(username = "User", roles = "USER")
    @DisplayName("Get category by invalid id")
    void getBook_invalidId_Fail() throws Exception {
        //GIVEN
        Long invalidId = -1L;
        //WHEN
        mockMvc.perform(
                        get("/categories/{id}", invalidId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof EntityNotFoundException));
        //THEN
    }

    @Test
    @WithMockUser(username = "User", roles = "USER")
    @DisplayName("Get all categories")
    void getAllCategories_validSize_Success() throws Exception {
        //GIVEN
        //WHEN
        MvcResult result = mockMvc.perform(
                        get("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //THEN
        CategoryDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto[].class);
        assertNotNull(actual);
        assertEquals(actual.length, 1);
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("Delete category by id")
    void deleteCategory_validId_Success() throws Exception {
        //GIVEN
        //WHEN
        mockMvc.perform(
                        delete("/categories/{id}", validId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
    }
}
