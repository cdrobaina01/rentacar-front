package cu.edu.cujae.rentacarfront.views;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import cu.edu.cujae.rentacarfront.dto.CountryDTO;
import cu.edu.cujae.rentacarfront.dto.GenderDTO;
import cu.edu.cujae.rentacarfront.dto.TouristDTO;
import cu.edu.cujae.rentacarfront.dto.save.TouristSaveDTO;
import cu.edu.cujae.rentacarfront.services.CountryService;
import cu.edu.cujae.rentacarfront.services.GenderService;
import cu.edu.cujae.rentacarfront.services.TouristService;
import cu.edu.cujae.rentacarfront.utils.AggregateService;

import java.util.ArrayList;
import java.util.List;

@Route("tourist")
@PageTitle("Turista | Rider Rent a Car")
public class TouristView extends EntityView<TouristDTO> {
    private final TouristService touristService;
    private final CountryService countryService;
    private final GenderService genderService;
    private TextField name;
    private TextField passport;
    private EmailField email;
    private IntegerField age;
    private TextField phone;
    private ComboBox<CountryDTO> country;
    private ComboBox<GenderDTO> gender;
    public TouristView(AggregateService aggregateService) {
        super(aggregateService);
        this.touristService = aggregateService.getTouristService();
        this.countryService = aggregateService.getCountryService();
        this.genderService = aggregateService.getGenderService();
        this.binder = new Binder<>(TouristDTO.class);
        this.selectedItem = new TouristDTO();
        configureUI();
    }
    @Override
    protected void configureGrid() {
        setDataGrid();
        configureGridListenner();
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
            Notification notification = new Notification(
                    "¡No existen elementos con ese identificador!", 3000, Notification.Position.MIDDLE);
            notification.open();
        }
        else{
            updateGrid(auxiliar);
        }
    }
    private void setDataGrid() {
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
        grid.addColumn(tourist -> tourist.getCountry().getName())
                .setHeader(getTranslation("tourist.country"))
                .setSortable(true);
        grid.addColumn(tourist -> tourist.getGender().getName())
                .setHeader(getTranslation("tourist.gender"))
                .setSortable(true);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    }

    private void configureGridListenner() {
        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedItem = event.getValue();
            if (selectedItem != null) {
                updateForm();
                setUpdateButton();
            } else {
                cleanForm();
                setAddButton();
            }
        });
    }

    private void setUpdateButton() {
        addButton.setText(getTranslation("button.update"));
        addButton.addClickListener(click -> onUpdateButtonClick());
    }

    private void setAddButton() {
        addButton.setText(getTranslation("button.add"));
        addButton.addClickListener(click -> onAddButtonClick());
    }

    private void cleanForm() {
        // Si no hay turista seleccionado, vacía los campos y desbloquea el campo del pasaporte
        name.clear();
        passport.clear();
        age.clear();
        phone.clear();
        email.clear();
        country.clear();
        gender.clear();

        // Desbloquea el campo del pasaporte
        passport.setReadOnly(false);
    }

    private void updateForm() {
        // Llena los campos con los datos del turista seleccionado
        name.setValue(selectedItem.getName());
        passport.setValue(selectedItem.getPassport());
        age.setValue(selectedItem.getAge());
        phone.setValue(selectedItem.getPhone());
        email.setValue(selectedItem.getEmail());
        country.setValue(selectedItem.getCountry());
        gender.setValue(selectedItem.getGender());
        // Bloquea el campo del pasaporte
        passport.setReadOnly(true);
    }
    @Override
    protected void updateGrid(List<TouristDTO> elements) {
        grid.setItems(elements);
    }

    @Override
    protected void updateGrid(TouristDTO element) {
        grid.setItems(element);
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
                        "El pasaporte debe tener entre 6 y 9 caracteres", 6, 12))
                .bind(TouristDTO::getPassport, TouristDTO::setPassport);

        // Valida el campo 'email' para que sea una dirección de correo electrónico válida
        binder.forField(email)
                .withValidator(new EmailValidator(
                        "Debe introducir un correo electrónico"))
                .bind(TouristDTO::getEmail, TouristDTO::setEmail);

        // Valida el campo 'phone' para que sólo contenga dígitos
        binder.forField(phone)
                .withValidator(phoneNumber -> phoneNumber.matches("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$"),
                        "El número de teléfono debe contener sólo dígitos")
                .bind(TouristDTO::getPhone, TouristDTO::setPhone);
    }

    @Override
    public void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = createEditorLayoutDiv();
        FormLayout formLayout = createFormLayout();
        editorLayoutDiv.add(formLayout);
        createButtEditorLayout(editorLayoutDiv);
        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private Div createEditorLayoutDiv() {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");
        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);
        return editorLayoutDiv;
    }

    private FormLayout createFormLayout() {
        FormLayout formLayout = new FormLayout();
        name = createTextField("Nombre");
        passport = createTextField("Pasaporte");
        age = createIntegerField("Edad");
        email = createEmailField("Email");
        phone = createTextField("Telefono");
        country = createComboBox("País", countryService);
        gender = createComboBox("Género", genderService);
        formLayout.add(name, passport, email, phone, age, country, gender);
        return formLayout;
    }

    protected void fillSaveDTO(TouristDTO dto, TouristSaveDTO save) {
        save.setName(dto.getName());
        save.setAge(dto.getAge());
        save.setEmail(dto.getEmail());
        save.setPassport(dto.getPassport());
        save.setPhone(dto.getPhone());
        save.setCountryId(dto.getCountry().getId());
        save.setGenderId(dto.getGender().getId());
    }
    @Override
    protected void onAddButtonClick() {
        TouristDTO dto = new TouristDTO();
        TouristSaveDTO save = new TouristSaveDTO();

        try {
            binder.writeBean(dto);
            fillSaveDTO(dto, save);
            touristService.create(save);
            refreshGrid();
            cleanForm();
        } catch (ValidationException e) {
            showInvalidFieldsNotification();
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void onUpdateButtonClick() {
        TouristDTO dto = binder.getBean();

        if (dto != null) {
            try {
                binder.writeBean(dto);
                TouristSaveDTO save = new TouristSaveDTO();
                fillSaveDTO(dto, save);
                touristService.update(dto.getPassport(),save);
                refreshGrid();
                cleanForm();
            } catch (ValidationException e) {
                showInvalidFieldsNotification();
            }
        } else {
            showNoSelectionNotification();
        }
    }
    @Override
    protected void onDeleteButtonClick() {
        TouristDTO dto = binder.getBean();

        if (dto != null) {
            touristService.delete(dto.getPassport());
            refreshGrid();
            cleanForm();
        } else {
            showNoSelectionNotification();
        }
    }
    @Override
    protected void configureUI() {
        createGridLayout(this.splitLayout);
        createEditorLayout(this.splitLayout);
        validateBinder();
        refreshGrid();
        binder.bindInstanceFields(this);
    }
}