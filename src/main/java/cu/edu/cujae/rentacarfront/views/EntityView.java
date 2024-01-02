package cu.edu.cujae.rentacarfront.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.theme.lumo.LumoIcon;
import cu.edu.cujae.rentacarfront.utils.AggregateService;

public abstract class EntityView<T> extends VerticalLayout {
    // Constantes
    private static final String FULL_SIZE = "100%";
    private static final double GRID_SIZE = 80;
    private static final String ICON_SIZE = "48px";

    protected final AggregateService aggregateService;

    // Componentes de la UI
    protected AppLayout appLayout;
    protected Div navigationBar;
    protected Div header;
    protected Div splitDiv;
    protected SplitLayout splitLayout;
    protected final Grid<T> grid = new Grid<>();
    protected Binder<T> binder = new Binder<>();

    ///CONSTRUCTOR

    public EntityView(AggregateService aggregateService) {
        this.aggregateService = aggregateService;
        setSizeFull();
        addClassNames("master-detail-view");

        initLayout();
        initHeader();
        initNavigationBar();
        initSplitLayout();

        configureGrid();
    }

    private void initLayout() {
        appLayout = new AppLayout();
        appLayout.getElement().getStyle().set("height", FULL_SIZE);
        appLayout.getElement().getStyle().set("width", FULL_SIZE);  // Establece el ancho del AppLayout al 100%
        add(appLayout);
    }

    private void initSplitLayout() {
        splitLayout = new SplitLayout();
        splitLayout.setSizeFull();   // Asegura que el SplitLayout ocupe todo el espacio disponible
        splitDiv = createDiv();
        splitDiv.add(splitLayout);
        addToPrimaryGrid();
        setSplitLayoutContent();
    }

    private Div createDiv() {
        return new Div();
    }

    private void addToPrimaryGrid() {
        splitLayout.addToPrimary(grid);
    }

    private void setSplitLayoutContent() {
        splitLayout.setSplitterPosition(GRID_SIZE);  // Establece que la tabla ocupe el 80% del espacio total
        appLayout.setContent(splitLayout);  // Establece el SplitLayout como el contenido del AppLayout
    }

    // Métodos abstractos para ser implementados en las subclases
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
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        grid.setSizeFull();
        wrapper.add(grid);

    }
    protected abstract void createEditorLayout(SplitLayout splitLayout);
}
