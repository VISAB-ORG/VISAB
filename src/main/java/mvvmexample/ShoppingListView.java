package mvvmexample;
import java.net.URL;
import java.util.ResourceBundle;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import mvvmexample.model.ListItem;

public class ShoppingListView implements FxmlView<ShoppingListViewModel>, Initializable {

    @FXML
    TextField listNameField;

    @FXML
    CheckBox listActiveCheckbox;

    @FXML
    TableView<ListItem> listItemsTable;

    @InjectViewModel
    ShoppingListViewModel viewModel;

    @FXML
    public void switchActiveAction() {
        // Execute the command defined in the ViewModel
        viewModel.switchListBoughtCommand().execute();
    }

    @FXML
    public void addItemAction() {
        // Execute the command defined in the ViewModel
        viewModel.addListItemCommand().execute();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind the Checkbox
        listActiveCheckbox.selectedProperty().bind(viewModel.listIsBoughtProperty());

        // Bind the textfield bidirectionally
        listNameField.textProperty().bindBidirectional(viewModel.ListNameProperty());

        // Bind the list of items
        listItemsTable.setItems(viewModel.getListItems());
    }
    
}
