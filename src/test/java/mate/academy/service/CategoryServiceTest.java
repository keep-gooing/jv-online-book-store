package mate.academy.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.dao.category.CategoryDto;
import mate.academy.mapper.CategoryMapper;
import mate.academy.model.Category;
import mate.academy.repository.CategoryRepository;
import mate.academy.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Find all categories with pagination")
    void findAllCategories_returnPagedCategoryDtoList() {
        // GIVEN
        Category firstCategory = new Category();
        Category secondCategory = new Category();
        CategoryDto firstCategoryDto = new CategoryDto();
        CategoryDto secondCategoryDto = new CategoryDto();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(List.of(firstCategory, secondCategory));
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(firstCategory)).thenReturn(firstCategoryDto);
        when(categoryMapper.toDto(secondCategory)).thenReturn(secondCategoryDto);

        // WHEN
        Page<CategoryDto> actualPage = categoryService.findAll(pageable);

        // THEN
        assertEquals(List.of(firstCategoryDto,
                secondCategoryDto), actualPage.getContent());
        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toDto(firstCategory);
        verify(categoryMapper).toDto(secondCategory);
    }

    @Test
    @DisplayName("Get category by valid id")
    void getById_validId_returnCategoryDto() {
        // GIVEN
        Long id = 1L;
        Category category = new Category();
        CategoryDto expectedDto = new CategoryDto();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);

        // WHEN
        CategoryDto actualDto = categoryService.getById(id);

        // THEN
        assertEquals(expectedDto, actualDto);
        verify(categoryRepository).findById(id);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Save valid category")
    void saveCategory_validDto_returnCategoryDto() {
        // GIVEN
        CategoryDto inputDto = new CategoryDto();
        Category category = new Category();
        CategoryDto expectedDto = new CategoryDto();

        when(categoryMapper.toEntity(inputDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);

        // WHEN
        CategoryDto actualDto = categoryService.save(inputDto);

        // THEN
        assertEquals(expectedDto, actualDto);
        verify(categoryMapper).toEntity(inputDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Update category by valid id")
    void updateCategory_validIdAndDto_returnCategoryDto() {
        // GIVEN
        Long id = 1L;
        CategoryDto inputDto = new CategoryDto();
        Category category = new Category();
        CategoryDto expectedDto = new CategoryDto();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        doNothing().when(categoryMapper).updateCategoryFromDto(inputDto, category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);

        // WHEN
        CategoryDto actualDto = categoryService.update(id, inputDto);

        // THEN
        assertEquals(expectedDto, actualDto);
        verify(categoryRepository).findById(id);
        verify(categoryMapper).updateCategoryFromDto(inputDto, category);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }
}
