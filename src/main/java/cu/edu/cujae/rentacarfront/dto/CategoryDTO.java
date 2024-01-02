package cu.edu.cujae.rentacarfront.dto;

import cu.edu.cujae.rentacarfront.utils.NamedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CategoryDTO implements NamedEntity {
    private Integer id;
    private String name;
}
