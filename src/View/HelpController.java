package View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HelpController implements IView{
    //@Override
    public void display() {

        try{
            Stage stage = new Stage();
            FXMLLoader fxmlLoader =new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Help.fxml").openStream());
            Scene scene = new Scene(root,235,300);
            scene.getStylesheets().add(getClass().getResource("HelpStyle.css").toExternalForm());
            stage.setTitle("The Game Rules");
            stage.setScene(scene);
            stage.setMinHeight(900);
            stage.setMinWidth(800);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


}
