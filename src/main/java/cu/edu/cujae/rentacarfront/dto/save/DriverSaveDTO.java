package cu.edu.cujae.rentacarfront.dto.save;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DriverSaveDTO {
    private String dni;
    private String name;
    private String address;
    private String email;
    private Integer categoryId;
}
