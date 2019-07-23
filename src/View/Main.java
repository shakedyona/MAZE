package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        MyModel model = new MyModel();
        model.startServers();
        MyViewModel viewModel = new MyViewModel(model);
        model.addObserver(viewModel);
        //--------------
        primaryStage.setTitle("The Halloween Maze !");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());
        primaryStage.setScene(scene);

        //--------------
        MyViewController view = fxmlLoader.getController();
        view.setResizeEvent(scene);
        view.setViewModel(viewModel);
        viewModel.addObserver(view);
        //--------------
        view.setStage(primaryStage);
        primaryStage.show();



    }



    public static void main(String[] args) {
        launch(args);
    }
}
