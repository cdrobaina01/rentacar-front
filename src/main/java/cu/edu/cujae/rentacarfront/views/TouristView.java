package cu.edu.cujae.rentacarfront.views;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import cu.edu.cujae.rentacarfront.dto.CountryDTO;
import cu.edu.cujae.rentacarfront.dto.GenderDTO;
import cu.edu.cujae.rentacarfront.dto.TouristDTO;
import cu.edu.cujae.rentacarfront.services.CountryService;
import cu.edu.cujae.rentacarfront.services.GenderService;
import cu.edu.cujae.rentacarfront.services.TouristService;
import cu.edu.cujae.rentacarfront.utils.AggregateService;

import java.util.List;

@Route("tourist")
@PageTitle("Turista | Rider Rent a Car")
public class TouristView extends EntityView<TouristDTO> {
    private final TouristService touristService;
    private final CountryService countryService;
    private final GenderService genderService;
    private TouristDTO selectedTourist;
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
        createGridLayout(this.splitLayout);
        createEditorLayout(this.splitLayout);
        updateGrid();
        binder.bindInstanceFields(this);
    }
    @Override
    protected void configureGrid() {
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
        grid.asSingleSelect().addValueChangeListener(event -> {
            TouristDTO selectedTourist = event.getValue();
            if (selectedTourist != null) {
                // Llena los campos con los datos del turista seleccionado
                name.setValue(selectedTourist.getName());
                passport.setValue(selectedTourist.getPassport());
                age.setValue(selectedTourist.getAge());
                phone.setValue(selectedTourist.getPhone());
                email.setValue(selectedTourist.getEmail());
                country.setValue(selectedTourist.getCountry());
                gender.setValue(selectedTourist.getGender());
                // Bloquea el campo del pasaporte
                passport.setReadOnly(true);

                addButton.setText("Actualizar");
            } else {
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

                addButton.setText("Agregar");
            }
        });
    }
    @Override
    protected void searchById() {
    }
    private void updateGrid() {
        List<TouristDTO> tourists = touristService.getAll();
        grid.setItems(tourists);
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

    @Override
    protected void onAddButtonClick() {

    }

    @Override
    protected void onDeleteButtonClick() {

    }
}