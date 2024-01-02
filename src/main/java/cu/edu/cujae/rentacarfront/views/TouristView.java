package cu.edu.cujae.rentacarfront.views;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.EmailField;
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
    private NumberField passport;
    private EmailField email;
    private NumberField phone;
    private NumberField age;
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
    }
    private void updateGrid() {
        List<TouristDTO> tourists = touristService.getAll();
        grid.setItems(tourists);
    }
    @Override
    public void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        name = new TextField("Nombre");
        passport = new NumberField("Pasaporte");
        age = new NumberField("Edad");
        email = new EmailField("Email");
        phone = new NumberField("Telefono");
        country = new ComboBox("País");
        country.setItems(countryService.getAll());  // Llena el ComboBox con los datos del CountryService
        country.setItemLabelGenerator(CountryDTO::getName);
        country.setAllowCustomValue(false);
        gender = new ComboBox("Género");
        gender.setItems(genderService.getAll());
        gender.setItemLabelGenerator(GenderDTO::getName);

        formLayout.add(name, passport, email, phone, age, country, gender);

        editorDiv.add(formLayout);
        //createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

}


/*


package cu.edu.cujae.rentacarfront.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import cu.edu.cujae.rentacarfront.dto.TouristDTO;
import cu.edu.cujae.rentacarfront.services.TouristService;
import cu.edu.cujae.rentacarfront.utils.AggregateService;

import java.util.List;

@Route("tourist")
@PageTitle("Turista | Rider Rent a Car")
public class TouristView extends EntityView<TouristDTO> {

    private final Grid<TouristDTO> grid = new Grid<>();
    private final Binder<TouristDTO> binder = new Binder<>(TouristDTO.class);
    private TouristDTO selectedTourist;
    private final TouristService touristService;

    private TextField name;
    private NumberField passport;
    private EmailField email;
    private NumberField phone;
    private NumberField age;
    private ComboBox country;
    private ComboBox gender;
    private Button addButton;
    private Button deleteButton;

    public TouristView(AggregateService aggregateService) {
        super(aggregateService);
        this.touristService = aggregateService.getTouristService();
        //configureForm();
        configureButtons();
        SplitLayout splitLayout = new SplitLayout();
        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);
        configureGrid();
        updateGrid();
    }

    public void configureGrid() {
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


        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedTourist = event.getValue();
            binder.setBean(selectedTourist);
        });
    }

    @Override
    protected void configureForm() {

    }


    private void configureButtons() {
        addButton = new Button(getTranslation("button.add"), clickEvent -> {
            // Aquí puedes añadir un nuevo turista
        });

        deleteButton = new Button(getTranslation("button.delete"), clickEvent -> {
            // Aquí puedes eliminar el turista seleccionado
        });

        add(addButton, deleteButton);
    }

    private void updateGrid() {
        List<TouristDTO> tourists = touristService.getAll();
        grid.setItems(tourists);
    }
    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        name = new TextField("Nombre");
        passport = new NumberField("Pasaporte");
        email = new EmailField("Email");
        phone = new NumberField("Teléfono");
        age = new NumberField("Edad");
        country = new ComboBox("País");
        gender = new ComboBox("Genero");

        formLayout.add(name, passport, email, email, phone, age, country, gender);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        addButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(addButton, deleteButton);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(TouristDTO value) {
        this.selectedTourist = value;
        binder.readBean(this.selectedTourist);

    }
}

 */
