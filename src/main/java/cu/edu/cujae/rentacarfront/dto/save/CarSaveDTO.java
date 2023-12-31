package cu.edu.cujae.rentacarfront.dto.save;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CarSaveDTO {
    private String plate;
    private Integer km;
    private String color;
    private Integer modelId;
    private Integer situationId;
}
