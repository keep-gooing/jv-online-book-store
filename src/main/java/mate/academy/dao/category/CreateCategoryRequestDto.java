package mate.academy.dao.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateCategoryRequestDto {
    @NotBlank
    @Size(min = 4, max = 50)
    private String name;
    @Size(min = 8, max = 255)
    private String description;
}
