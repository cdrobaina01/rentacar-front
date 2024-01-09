package cu.edu.cujae.rentacarfront.views;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import cu.edu.cujae.rentacarfront.dto.TouristDTO;
import cu.edu.cujae.rentacarfront.dto.save.TouristSaveDTO;
import cu.edu.cujae.rentacarfront.services.TouristService;
import cu.edu.cujae.rentacarfront.utils.AggregateService;
import cu.edu.cujae.rentacarfront.utils.TouristGender;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Route("tourist")
@PageTitle("Turista | Rider Rent a Car")
public class TouristView extends EntityView<TouristDTO, TouristSaveDTO> {
    private final TouristService touristService;
    private TextField name;
    private TextField passport;
    private EmailField email;
    private IntegerField age;
    private TextField phone;
    private TextField country;
    private ComboBox<TouristGender> gender;
    public TouristView(AggregateService aggregateService) {
        super(aggregateService);
        this.touristService = aggregateService.getTouristService();
        this.binder = new Binder<>(TouristDTO.class);
        this.selectedItem = new TouristDTO();
        configureUI();
        binder.bindInstanceFields(this);
        validateBinder();
    }
    @Override
    protected void onSearchButtonClick() {
        List<TouristDTO> result = touristService.getAll();
        List<TouristDTO> auxiliar = new ArrayList<>();
        for(TouristDTO tourist : result){
            if(tourist.getPassport().toLowerCase().contains(searchField.getValue().toLowerCase()) ){
                auxiliar.add(tourist);
            }
        }
        if(auxiliar.isEmpty()){
            showInvalidIdentifier();
        }
        else{
            updateGrid(auxiliar);
        }
    }
    @Override
    protected void setDataGrid() {
        grid.addColumn(TouristDTO::getName)
                .setHeader(getTranslation("tourist.name"))
                .setSortable(true);
        grid.addColumn(TouristDTO::getPassport)
                .setHeader(getTranslation("tourist.passport"))
                .setSortable(true);
        grid.addColumn(TouristDTO::getAge)
                .setHeader(getTranslation("tourist.age"))
                .setSortable(true);
        grid.addColumn(TouristDTO::getEmail)
                .setHeader(getTranslation("tourist.email"))
                .setSortable(true);
        grid.addColumn(TouristDTO::getPhone)
                .setHeader(getTranslation("tourist.phone"))
                .setSortable(true);
        grid.addColumn(TouristDTO::getCountry)
                .setHeader(getTranslation("tourist.country"))
                .setSortable(true);
        grid.addColumn(TouristDTO::getGender)
                .setHeader(getTranslation("tourist.gender"))
                .setSortable(true);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    }
    @Override
    protected void cleanForm() {
        name.clear();
        passport.clear();
        age.clear();
        phone.clear();
        email.clear();
        country.clear();
        gender.clear();

        // Desbloquea el campo del pasaporte
        passport.setReadOnly(false);

        // Limpia la selección actual
        selectedItem = null;
        binder.setBean(null);

        // Limpia el estado del Binder
        binder.readBean(null);
    }

    @Override
    protected void updateForm() {
        // Comprueba si hay un elemento seleccionado
        if (selectedItem != null) {
            // Enlaza los campos del formulario al objeto seleccionado
            binder.setBean(selectedItem);
        }
        // Bloquea el campo del pasaporte
        passport.setReadOnly(true);
    }
    @Override
    protected void refreshGrid() {
        List<TouristDTO> all = touristService.getAll();
        grid.setItems(all);
    }
    @Override
    protected void validateBinder() {
        // Valida el campo 'name' para que no esté vacío y tenga menos de 50 caracteres
        binder.forField(name)
                .withValidator(new StringLengthValidator(
                        "El nombre debe tener menos de 50 caracteres", 1, 50))
                .bind(TouristDTO::getName, TouristDTO::setName);

        // Valida el campo 'passport' para que tenga exactamente 9 caracteres
        binder.forField(passport)
                .withValidator(new StringLengthValidator(
                        "El pasaporte debe tener entre 6 y 12 caracteres", 6, 12))
                .bind(TouristDTO::getPassport, TouristDTO::setPassport);

        // Valida el campo 'email' para que sea una dirección de correo electrónico válida
        binder.forField(email)
                .withValidator(new EmailValidator(
                        "Debe introducir un correo electrónico"))
                .bind(TouristDTO::getEmail, TouristDTO::setEmail);

        // Valida el campo 'phone' para que sólo contenga dígitos
        binder.forField(phone)
                .withValidator(phoneNumber -> phoneNumber.matches("^[-.()\\d]{6,20}$"),
                        "El número de teléfono debe tener entre 6 y 12 caracteres y sólo puede contener números, '-', '.', y '()'")
                .bind(TouristDTO::getPhone, TouristDTO::setPhone);
        // Validación para el campo 'gender'
        binder.forField(gender)
                .withValidator(Objects::nonNull,
                        "Debe seleccionar un género")
                .bind(TouristDTO::getGender, TouristDTO::setGender);

        // Validación para el campo 'country'
        binder.forField(country)
                .withValidator(Objects::nonNull,
                        "Debe seleccionar un país")
                .bind(TouristDTO::getCountry, TouristDTO::setCountry);

        // Validación para el campo 'age'
        binder.forField(age)
                .withValidator(ageValue -> ageValue != null && ageValue >= 18 && ageValue <= 150,
                        "Debe escribir una edad mayor de 18 y menor que 150")
                .bind(TouristDTO::getAge, TouristDTO::setAge);
    }
    @Override
    protected FormLayout createFormLayout() {
        FormLayout formLayout = new FormLayout();
        name = createTextField("Nombre");
        passport = createTextField("Pasaporte");
        age = createIntegerField("Edad");
        email = createEmailField("Email");
        phone = createTextField("Telefono");
        country = createTextField("País");
        gender = new ComboBox<TouristGender>("Genero");
        gender.setItems(TouristGender.values());
        formLayout.add(name, passport, email, phone, age, country, gender);
        return formLayout;
    }
    @Override
    protected void onAddButtonClick() {
        TouristDTO dto = new TouristDTO();
        TouristSaveDTO save = new TouristSaveDTO();
        BinderValidationStatus<TouristDTO> status = binder.validate();

        if (status.isOk()) {
            try {
                binder.writeBean(dto);
                fillSaveDTO(dto,save);
                touristService.create(save);
                binder.setBean(null);
                cleanForm();
                refreshGrid();
                showAddSuccessNotification();
            } catch (ValidationException e) {
                showInvalidFieldsNotification();
            }
        } else {
            showInvalidFieldsNotification();
        }
    }
    @Override
    protected void onUpdateButtonClick() {
        TouristDTO dto = binder.getBean();
        BinderValidationStatus<TouristDTO> status = binder.validate();

        if (status.isOk()) {
            if (dto != null) {
                try {
                    binder.writeBean(dto);
                    TouristSaveDTO save = new TouristSaveDTO();
                    fillSaveDTO(dto, save);
                    touristService.update(dto.getPassport(),save);
                    binder.setBean(null);
                    cleanForm();
                    refreshGrid();
                    showUpdateSuccessNotification();
                } catch (ValidationException e) {
                    showInvalidFieldsNotification();
                }
            } else {
                showNoSelectionNotification();
            }
        } else {
            showInvalidFieldsNotification();
        }
    }

    @Override
    protected void fillSaveDTO(TouristDTO dto, TouristSaveDTO save) {
        save.setName(dto.getName());
        save.setAge(dto.getAge());
        save.setEmail(dto.getEmail());
        save.setPassport(dto.getPassport());
        save.setPhone(dto.getPhone());
        save.setCountry(dto.getCountry());
        save.setGender(dto.getGender());
    }

    @Override
    protected void onDeleteButtonClick() {
        TouristDTO dto = binder.getBean();

        if (dto != null) {
            try {
                touristService.delete(dto.getPassport());
                cleanForm();
                refreshGrid();
                showDeleteSuccessNotification();
            } catch (HttpServerErrorException e) {
                if (e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                    showInvalidElementSelected();
                }
            }
        } else {
            showNoSelectionNotification();
        }
    }
}
