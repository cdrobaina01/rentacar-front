package cu.edu.cujae.rentacarfront.views;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoIcon;
import cu.edu.cujae.rentacarfront.utils.AggregateService;
import cu.edu.cujae.rentacarfront.utils.NamedEntity;

import java.util.List;

public abstract class EntityView<T, U> extends VerticalLayout {
    // Constantes
    private static final String FULL_SIZE = "100%";
    private static final double GRID_SIZE = 80;
    private static final String ICON_SIZE = "48px";

    // Componentes de la UI
    protected AppLayout appLayout;
    protected Div navigationBar;
    protected Div splitDiv;
    protected SplitLayout splitLayout;
    protected final Grid<T> grid = new Grid<>();
    protected Binder<T> binder = new Binder<>();
    protected TextField searchField;
    protected Button searchButton;
    protected Button addButton;
    protected Button deleteButton;

    // Variables
    protected final AggregateService aggregateService;
    protected T selectedItem;
    private Registration buttonClickListener;

    //Metodos Abstractos
    protected abstract void onSearchButtonClick();
    protected abstract void onAddButtonClick();

    protected abstract void fillSaveDTO(T dto, U save);
    protected abstract void onDeleteButtonClick();
    protected abstract void onUpdateButtonClick();
    protected abstract FormLayout createFormLayout();
    protected abstract void setDataGrid();
    protected abstract void cleanForm();
    protected abstract void updateForm();
    protected abstract void refreshGrid();
    protected abstract void validateBinder();
    public EntityView(AggregateService aggregateService) {
        this.aggregateService = aggregateService;
        setSizeFull();
        addClassNames("master-detail-view");
        initUIComponents();
        configureGrid();
    }
    private void initUIComponents() {
        initLayout();
        initHeader();
        initNavigationBar();
        initSplitLayout();
        initButtons();
    }
    private void initLayout() {
        appLayout = new AppLayout();
        appLayout.getElement().getStyle().set("height", FULL_SIZE);
        appLayout.getElement().getStyle().set("width", FULL_SIZE);
        add(appLayout);
    }
    private void initSplitLayout() {
        splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitDiv = createDiv();
        splitDiv.add(splitLayout);
        addToPrimary();
        setSplitLayoutContent();
    }
    private Div createDiv() {
        return new Div();
    }

    private void addToPrimary() {
        VerticalLayout content = new VerticalLayout();
        splitLayout.addToPrimary(content);
    }
    private void setSplitLayoutContent() {
        splitLayout.setSplitterPosition(GRID_SIZE);
        appLayout.setContent(splitLayout);
    }
    private void initNavigationBar() {
        navigationBar = new Div();
        navigationBar.setText("Aquí va la barra de navegación");
        appLayout.setDrawerOpened(true);
        appLayout.addToDrawer(navigationBar);
    }
    private void initHeader() {
        HorizontalLayout header = new HorizontalLayout();
        Div iconDiv = new Div();
        Icon icon = LumoIcon.PLAY.create();
        icon.setSize(ICON_SIZE);
        iconDiv.add(icon);
        header.add(iconDiv, new H1("Rider Rent a Car"));
        appLayout.addToNavbar(header);
    }
    protected void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        grid.setSizeFull();
        Div gridContainer = new Div(grid);
        gridContainer.setHeight(90, Unit.PERCENTAGE);
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        initSearchComponents(wrapper);
        wrapper.add(gridContainer);
    }
    private void initSearchComponents(Div wrapper) {
        searchField = new TextField();
        searchField.setPlaceholder(getTranslation("placeholder.id"));
        searchButton = new Button(getTranslation("button.search"), click -> onSearchButtonClick());
        HorizontalLayout searchLayout = new HorizontalLayout(searchField, searchButton);
        wrapper.add(searchLayout);
    }
    protected void configureGridListener() {
        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedItem = event.getValue();
            if (selectedItem != null) {
                binder.setBean(selectedItem);  // Establece el bean del binder
                updateForm();
                setUpdateButton();  // Configura el botón para actualizar
            } else {
                binder.setBean(null);  // Limpia el bean del binder
                cleanForm();
                setAddButton();  // Configura el botón para añadir
            }
        });
    }
    protected void configureGrid() {
        setDataGrid();
        configureGridListener();
    }

    protected void createButtEditorLayout(Div editorLayout){
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(addButton, deleteButton);
        editorLayout.add(buttonLayout);
        setAddButton();
        deleteButton.addClickListener(click -> onDeleteButtonClick());
    }
    protected <U extends NamedEntity> ComboBox<U> createComboBox(String label) {
        ComboBox<U> comboBox = new ComboBox<>(label);
        comboBox.setItemLabelGenerator(U::getName);
        comboBox.setAllowCustomValue(false);
        return comboBox;
    }
    protected void showNoSelectionNotification() {
        Notification notification = new Notification("No se seleccionó ningún elemento", 500, Notification.Position.TOP_CENTER);
        notification.open();
    }
    protected void showInvalidFieldsNotification() {
        Notification notification = new Notification("Campos inválidos", 500, Notification.Position.TOP_CENTER);
        notification.open();
    }
    protected void showAddSuccessNotification() {
        Notification notification = new Notification(
                "Elemento agregado con éxito", 3000, Notification.Position.MIDDLE);
        notification.open();
    }
    protected void showUpdateSuccessNotification() {
        Notification notification = new Notification(
                "Elemento actualizado con éxito", 3000, Notification.Position.MIDDLE);
        notification.open();
    }
    protected void showDeleteSuccessNotification() {
        Notification notification = new Notification(
                "Elemento eliminado con éxito", 3000, Notification.Position.MIDDLE);
        notification.open();
    }
    protected void showInvalidElementSelected() {
        Notification notification = new Notification(
                "El elemento no puede ser eliminado porque todavía tiene contratos asociados",
                3000, Notification.Position.TOP_CENTER);
        notification.open();
    }
    protected void showInvalidIdentifier() {
        Notification notification = new Notification(
                "¡No existen elementos con ese identificador!", 3000, Notification.Position.MIDDLE);
        notification.open();
    }
    protected void configureUI() {
        createGridLayout(this.splitLayout);
        createEditorLayout(this.splitLayout);
        refreshGrid();
    }
    protected void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = createEditorLayoutDiv();
        FormLayout formLayout = createFormLayout();
        editorLayoutDiv.add(formLayout);
        createButtEditorLayout(editorLayoutDiv);
        splitLayout.addToSecondary(editorLayoutDiv);
    }
    protected Div createEditorLayoutDiv() {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");
        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);
        return editorLayoutDiv;
    }
    private void initButtons() {
        addButton = new Button(getTranslation("button.add"));
        deleteButton = new Button(getTranslation("button.delete"));

        // Añade los listeners a los botones
        buttonClickListener = addButton.addClickListener(click -> onAddButtonClick());
        deleteButton.addClickListener(click -> onDeleteButtonClick());
    }

    protected void setUpdateButton() {
        // Elimina el listener existente
        if (buttonClickListener != null) {
            buttonClickListener.remove();
        }

        addButton.setText(getTranslation("button.update"));
        // Añade el nuevo listener y guarda la referencia
        buttonClickListener = addButton.addClickListener(click -> onUpdateButtonClick());
    }

    protected void setAddButton() {
        // Elimina el listener existente
        if (buttonClickListener != null) {
            buttonClickListener.remove();
        }

        addButton.setText(getTranslation("button.add"));
        // Añade el nuevo listener y guarda la referencia
        buttonClickListener = addButton.addClickListener(click -> onAddButtonClick());
    }
    protected TextField createTextField(String label) {
        return new TextField(label);
    }
    protected IntegerField createIntegerField(String label) {
        return new IntegerField(label);
    }
    protected EmailField createEmailField(String label) {
        return new EmailField(label);
    }
    protected void updateGrid(List<T> elements) {
        grid.setItems(elements);
    }
    protected void updateGrid(T element) {
        grid.setItems(element);
    }
}
