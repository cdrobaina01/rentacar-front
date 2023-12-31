package cu.edu.cujae.rentacarfront.dto.save;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ModelSaveDTO {
    private String name;
    private Integer brandId;
}
