package View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AboutController implements IView{
    //@Override
    public void display() {

        try{
            Stage stage = new Stage();
            FXMLLoader fxmlLoader =new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(root,500,250);
            scene.getStylesheets().add(getClass().getResource("AboutStyle.css").toExternalForm());
            root.getStylesheets().add(getClass().getResource("AboutStyle.css").toExternalForm());
            stage.setTitle("About Us");
            stage.setScene(scene);
            stage.setMinHeight(800);
            stage.setMinWidth(900);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


}
