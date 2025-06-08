package mate.academy.util;

import java.math.BigDecimal;
import java.util.Set;
import mate.academy.dao.book.BookDto;
import mate.academy.dao.book.CreateBookRequestDto;
import mate.academy.dao.category.CategoryDto;
import mate.academy.dao.category.CreateCategoryRequestDto;

public final class TestUtil {
    private TestUtil() {
    }

    public static String getFirstBookIsbn() {
        return "0000000001";
    }

    public static String getSecondBookIsbn() {
        return "0000000002";
    }

    public static CreateBookRequestDto createBookRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("Book 1")
                .setAuthor("Author")
                .setIsbn(getFirstBookIsbn())
                .setPrice(BigDecimal.valueOf(50.00))
                .setCategoryIds(Set.of(1L));
    }

    public static BookDto createBookResponseDto() {
        return new BookDto()
                .setTitle(createBookRequestDto().getTitle())
                .setAuthor(createBookRequestDto().getAuthor())
                .setIsbn(createBookRequestDto().getIsbn())
                .setPrice(createBookRequestDto().getPrice())
                .setCategoryIds(createBookRequestDto().getCategoryIds());
    }

    public static CreateCategoryRequestDto createCategoryRequestDto() {
        return new CreateCategoryRequestDto()
                .setName("Fairy Tale")
                .setDescription("Fairy Tale");
    }

    public static CategoryDto createCategoryResponseDto() {
        return new CategoryDto()
                .setName(createCategoryRequestDto().getName())
                .setDescription(createCategoryRequestDto().getDescription());
    }
}
