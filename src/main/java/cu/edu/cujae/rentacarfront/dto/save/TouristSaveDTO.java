package cu.edu.cujae.rentacarfront.dto.save;

import cu.edu.cujae.rentacarfront.utils.TouristGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TouristSaveDTO {
    private String passport;
    private String name;
    private Integer age;
    private String phone;
    private String email;
    private TouristGender gender;
    private String country;
}
