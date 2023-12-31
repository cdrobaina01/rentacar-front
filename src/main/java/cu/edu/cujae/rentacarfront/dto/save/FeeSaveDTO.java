package cu.edu.cujae.rentacarfront.dto.save;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FeeSaveDTO {
    private String name;
    private Double value;
}
