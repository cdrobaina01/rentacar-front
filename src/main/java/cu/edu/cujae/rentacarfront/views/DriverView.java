package cu.edu.cujae.rentacarfront.views;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import cu.edu.cujae.rentacarfront.dto.DriverDTO;
import cu.edu.cujae.rentacarfront.utils.AggregateService;

import java.util.List;

public class DriverView extends EntityView<DriverDTO> {
    public DriverView(AggregateService aggregateService) {
        super(aggregateService);
    }

    @Override
    protected void configureGrid() {

    }

    @Override
    protected void onSearchButtonClick() {

    }

    @Override
    protected void createEditorLayout(SplitLayout splitLayout) {

    }

    @Override
    protected FormLayout createFormLayout() {
        return null;
    }

    @Override
    protected void setDataGrid() {

    }

    @Override
    protected void cleanForm() {

    }

    @Override
    protected void updateForm() {

    }

    @Override
    protected void onAddButtonClick() {

    }

    @Override
    protected void onDeleteButtonClick() {

    }

    @Override
    protected void onUpdateButtonClick() {

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

    }
}
