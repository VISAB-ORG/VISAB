package mvvmexample;

import java.util.ArrayList;
import java.util.Random;

import org.visab.newgui.ViewModelBase;

import de.saxsys.mvvmfx.utils.commands.Command;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mvvmexample.model.ListItem;

public class ShoppingListViewModel extends ViewModelBase {

    private BooleanProperty listIsBought = new SimpleBooleanProperty();

    private StringProperty listName = new SimpleStringProperty();

    private ObservableList<ListItem> listItems = FXCollections.observableArrayList();

    public BooleanProperty listIsBoughtProperty() {
        return listIsBought;
    }

    public StringProperty ListNameProperty() {
        return listName;
    }

    public ObservableList<ListItem> getListItems() {
        return listItems;
    }

    public Command switchListBoughtCommand() {
        return runnableCommand(() -> {
            listIsBought.set(!listIsBought.get());
        });
    }

    public Command addListItemCommand() {
        var randomItems = new ArrayList<ListItem>();
        randomItems.add(new ListItem("Apple", 0.23));
        randomItems.add(new ListItem("Coke", 0.99));
        randomItems.add(new ListItem("Water", 1.23));
        
        return runnableCommand(() -> {
            // Pick a random item
            var item = randomItems.get(new Random().nextInt(randomItems.size()));

            listItems.add(item);

            System.out.println(listName.get() + " now has " + listItems.size() + " items!");
        });
    }

}
