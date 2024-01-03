package cu.edu.cujae.rentacarfront.services.ui;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import cu.edu.cujae.rentacarfront.dto.CountryDTO;
import cu.edu.cujae.rentacarfront.dto.GenderDTO;
import cu.edu.cujae.rentacarfront.dto.TouristDTO;

public class TouristUI {
    // Aquí van los campos de la UI
    private TextField name;
    private TextField passport;
    private EmailField email;
    private IntegerField age;
    private TextField phone;
    private ComboBox<CountryDTO> country;
    private ComboBox<GenderDTO> gender;

    public TouristUI() {
        // Inicializa los campos aquí
        name = new TextField();
        passport = new TextField();
        email = new EmailField();
        age = new IntegerField();
        phone = new TextField();
        country = new ComboBox<>();
        gender = new ComboBox<>();
    }

    // Método para configurar la UI
    public void configureUI() {
        // Aquí puedes agregar la lógica para configurar tus campos de la UI
        // Por ejemplo, podrías configurar los placeholders, los valores por defecto, los listeners de eventos, etc.
    }

    // Método para actualizar la UI
    public void updateUI(TouristDTO tourist) {
        // Aquí puedes agregar la lógica para actualizar tus campos de la UI con los datos del turista
        name.setValue(tourist.getName());
        passport.setValue(tourist.getPassport());
        email.setValue(tourist.getEmail());
        age.setValue(tourist.getAge());
        phone.setValue(tourist.getPhone());
        country.setValue(tourist.getCountry());
        gender.setValue(tourist.getGender());
    }
}
