package View;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by Hadar on 23/06/2017.
 */
public class PropertiesDisplay {

    public void  propertiesDisplay(){
        Stage theWindow = new Stage();
        theWindow.initModality(Modality.APPLICATION_MODAL);
        theWindow.setTitle("Properties");
        theWindow.setMinHeight(100);
        theWindow.setMinWidth(250);

        Pane pane = new Pane();

        Scene scene = new Scene(pane);
//        scene.getStylesheets().add(getClass().getResource("ViewStyle").toExternalForm());
        Properties properties = new Properties();
        InputStream inputStream = null;

        try {
        inputStream = new FileInputStream("src/config.properties");
        properties.load(inputStream);

        String config = "";
            Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()){
            String key = (String) enumeration.nextElement();
            String value = properties.getProperty(key);
            config = config + "key:" + key + ", value:" + value + "\n";
        }
            Label label = new Label(config);
            pane.getChildren().addAll(label);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        theWindow.setScene(scene);
        theWindow.showAndWait();
    }
}
