import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // 1. Point to your FXML file
        // The path starts from the root of the 'resources' folder
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/main_view.fxml")));

        // 2. Set the scene with the loaded FXML layout
        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("Meal Planner System");
        stage.setScene(scene);
        stage.show();
    }
}