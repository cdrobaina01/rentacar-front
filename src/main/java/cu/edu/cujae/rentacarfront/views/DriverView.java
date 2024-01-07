package cu.edu.cujae.rentacarfront.views;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import cu.edu.cujae.rentacarfront.dto.CategoryDTO;
import cu.edu.cujae.rentacarfront.dto.DriverDTO;
import cu.edu.cujae.rentacarfront.dto.TouristDTO;
import cu.edu.cujae.rentacarfront.dto.save.DriverSaveDTO;
import cu.edu.cujae.rentacarfront.dto.save.TouristSaveDTO;
import cu.edu.cujae.rentacarfront.services.CategoryService;
import cu.edu.cujae.rentacarfront.services.DriverService;
import cu.edu.cujae.rentacarfront.utils.AggregateService;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
@Route("driver")
@PageTitle("Conductor| Rider Rent a Car")
public class DriverView extends EntityView<DriverDTO, DriverSaveDTO> {
    private final DriverService driverService;
    private CategoryService CategoryService;
    private TextField dni;
    private TextField name;
    private TextField address;
    private EmailField email;
    private ComboBox<CategoryDTO> category;



    public DriverView(AggregateService aggregateService) {
        super(aggregateService);
        this.driverService = aggregateService.getDriverService();
        this.binder = new Binder<>(DriverDTO.class);
        this.selectedItem = new DriverDTO();
        configureUI();
        binder.bindInstanceFields(this);
    }

    @Override
    protected void configureGrid() {

    }

    @Override
    protected void onSearchButtonClick() {
        List<DriverDTO> result = driverService.getAll();
        List<DriverDTO> auxiliar = new ArrayList<>();
        for(DriverDTO driver : result){
            if(driver.getDni().toLowerCase().contains(searchField.getValue().toLowerCase()) ){
                auxiliar.add(driver);
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

    @Override
    protected void createEditorLayout(SplitLayout splitLayout) {

    }

    @Override
    protected FormLayout createFormLayout() {
        FormLayout formLayout = new FormLayout();
        name = createTextField("Nombre");
        dni = createTextField("Carnet");
        email = createEmailField("Email");
        address = createTextField("Direcciópn");
        category = createComboBox("Categoría", CategoryService);
        formLayout.add(name, dni, email, category);
        return formLayout;
    }

    @Override
    protected void setDataGrid() {
        grid.addColumn(DriverDTO::getName)
                .setHeader(getTranslation("driver.name"))
                .setSortable(true);
        grid.addColumn(DriverDTO::getDni)
                .setHeader(getTranslation("driver.getId"))
                .setSortable(true);
        grid.addColumn(DriverDTO::getEmail)
                .setHeader(getTranslation("driver.email"))
                .setSortable(true);
        grid.addColumn(DriverDTO::getAddress)
                .setHeader(getTranslation("driver.address"))
                .setSortable(true);
        grid.addColumn(driver -> driver.getCategory().getName())
                .setHeader(getTranslation("driver.category"))
                .setSortable(true);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    }

    @Override
    protected void cleanForm() {
        name.clear();
        dni.clear();
        address.clear();
        email.clear();
        category.clear();

        // Desbloquea el campo del dni
        dni.setReadOnly(false);

        // Limpia la selección actual
        selectedItem = null;
        binder.setBean(null);

    }

    @Override
    protected void updateForm() {
        // Llena los campos con los datos del conductor seleccionado
        name.setValue(selectedItem.getName());
        dni.setValue(selectedItem.getDni());
        address.setValue(selectedItem.getAddress());
        email.setValue(selectedItem.getEmail());
        category.setValue(selectedItem.getCategory());
        // Bloquea el campo del dni
        dni.setReadOnly(true);

    }

    @Override
    protected void onAddButtonClick() {
        DriverDTO dto = new DriverDTO();
        DriverSaveDTO save = new DriverSaveDTO();
        if (binder.isValid()) {
            try {
                binder.writeBean(dto);
                fillSaveDTO(dto, save);
                driverService.create(save);
                binder.setBean(null);
                cleanForm();
                refreshGrid();
            } catch (ValidationException e) {
                showInvalidFieldsNotification();
            }
        }
        else {
            // Muestra una única notificación de error
            Notification notification = new Notification(
                    "Tienes campos inválidos", 5000, Notification.Position.MIDDLE);
            notification.open();
        }

    }
    protected void fillSaveDTO(DriverDTO dto, DriverSaveDTO save) {
        save.setName(dto.getName());
        save.setDni(dto.getDni());
        save.setEmail(dto.getEmail());
        save.setAddress(dto.getAddress());
        save.setCategoryId(dto.getCategory().getId());

    }

    @Override
    protected void onDeleteButtonClick() {
        DriverDTO dto = binder.getBean();

        if (dto != null) {
            try {
                driverService.delete(dto.getDni());
                cleanForm();
                refreshGrid();
        }catch (HttpServerErrorException e) {
                if (e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                    Notification notification = new Notification(
                            "El elemento no puede ser eliminado porque todavía tiene contratos asociados",
                            3000,
                            Notification.Position.TOP_CENTER);
                    notification.open();
                }
            }
        } else {
            showNoSelectionNotification();
        }

    }

    @Override
    protected void onUpdateButtonClick() {
        DriverDTO dto = binder.getBean();
        System.out.println("Binder antes de la actualización: " + binder.getBean());
        System.out.println("dto antes de la actualización: " + dto);
        if (binder.isValid()) {
            if (dto != null) {
                try {
                    binder.writeBean(dto);
                    System.out.println("Binder después de la actualización: " + binder.getBean());
                    DriverSaveDTO save = new DriverSaveDTO();
                    fillSaveDTO(dto, save);
                    driverService.update(dto.getDni(),save);
                    binder.setBean(null);
                    cleanForm();
                    refreshGrid();
                } catch (ValidationException e) {
                    showInvalidFieldsNotification();
                }
            } else {
                showNoSelectionNotification();
            }
        }
        else {
            // Muestra una única notificación de error
            Notification notification = new Notification(
                    "Tienes campos inválidos", 5000, Notification.Position.MIDDLE);
            notification.open();
        }

    }

    @Override
    protected void configureUI() {

    }

    @Override
    protected void updateGrid(List<DriverDTO> elements) {

    }

    @Override
    protected void updateGrid(DriverDTO element) {

    }

    @Override
    protected void refreshGrid() {

    }
    @Override
    protected void validateBinder() {
            binder.forField(name)
                    .withValidator(new StringLengthValidator(
                            "El nombre debe tener menos de 50 caracteres", 1, 50))
                    .bind(DriverDTO::getName, DriverDTO::setName);

            binder.forField(dni)
                    .withValidator(new StringLengthValidator(
                            "El DNI debe tener entre 11 caracteres", 11, 11))
                    .bind(DriverDTO::getDni, DriverDTO::setDni);

            binder.forField(email)
                    .withValidator(new EmailValidator(
                            "Debe introducir un correo electrónico válido"))
                    .bind(DriverDTO::getEmail, DriverDTO::setEmail);

            binder.forField(address)
                    .withValidator(new StringLengthValidator(
                            "La dirección debe tener entre 1 y 100 caracteres", 1, 100))
                    .bind(DriverDTO::getAddress, DriverDTO::setAddress);

            binder.forField(category)
                    .withValidator(categoryValue -> !(categoryValue == null),
                            "Debe seleccionar una categoría")
                    .bind(DriverDTO::getCategory, DriverDTO::setCategory);
        }
    }

