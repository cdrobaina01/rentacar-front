package cu.edu.cujae.rentacarfront.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.theme.lumo.LumoIcon;
import cu.edu.cujae.rentacarfront.services.BaseService;
import cu.edu.cujae.rentacarfront.utils.AggregateService;
import cu.edu.cujae.rentacarfront.utils.NamedEntity;

public abstract class EntityView<T> extends VerticalLayout {
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
    protected NumberField idField;
    protected Button searchButton;
    protected Button addButton;
    protected Button deleteButton;

    protected final AggregateService aggregateService;

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

    protected abstract void configureGrid();

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
        idField = new NumberField();
        idField.setPlaceholder("Identificador");
        searchButton = new Button("Buscar", click -> searchById());
        HorizontalLayout searchLayout = new HorizontalLayout(idField, searchButton);
        wrapper.add(searchLayout);
    }

    protected abstract void searchById();

    protected abstract void createEditorLayout(SplitLayout splitLayout);

    private void initButtons() {
        addButton = new Button("Agregar");
        addButton.addClickListener(click -> onAddButtonClick());
        deleteButton = new Button("Eliminar");
        deleteButton.addClickListener(click -> onDeleteButtonClick());
    }

    protected abstract void onAddButtonClick();

    protected abstract void onDeleteButtonClick();

    protected void createButtEditorLayout(Div editorLayout){
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(addButton, deleteButton);
        editorLayout.add(buttonLayout);
    }
    protected <U extends NamedEntity> ComboBox<U> createComboBox(String label, BaseService<U, ?> service) {
        ComboBox<U> comboBox = new ComboBox<>(label);
        comboBox.setItems(service.getAll());
        comboBox.setItemLabelGenerator(U::getName);
        comboBox.setAllowCustomValue(false);
        return comboBox;
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
}
