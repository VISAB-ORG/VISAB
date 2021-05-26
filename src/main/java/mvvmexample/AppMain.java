package mvvmexample;

import de.saxsys.mvvmfx.FluentViewLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Shopping list");

        // Resolves the View.java and View.fxml class. Then invokes initialize on them
        var viewTupel = FluentViewLoader.fxmlView(ShoppingListView.class).load();
        var root = viewTupel.getView();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
