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
public class ModelDTO implements NamedEntity {
    private Integer id;
    private String name;
    private BrandDTO brand;
}
