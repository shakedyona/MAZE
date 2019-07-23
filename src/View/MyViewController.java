package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

import javax.print.attribute.standard.Media;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by שקד on 17/06/2017.
 */
public class MyViewController implements Observer,IView {

    @FXML
    private MyViewModel viewModel;
    public MazeDisplayer mazeDisplayer;
    public int[][] theArrMaze;
    public boolean win = false;
    public boolean solve = false;
    private Stage myStage;

    //user input
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_columnsNum;
    public javafx.scene.control.TextField txtfld_nameSaveMaze;
    public javafx.scene.control.TextField txtfld_nameLoadMaze;
    //bottom
    public javafx.scene.control.Button btn_generateMaze;
    public javafx.scene.control.Button btn_solveMaze;
    //position now
    public javafx.scene.control.Label lbl_rowsNum;
    public javafx.scene.control.Label lbl_columnsNum;
    //for the width and high of border
    public javafx.scene.layout.BorderPane txtfld_BorderPane;
    //String Property for Binding
    public StringProperty CharacterRow = new SimpleStringProperty();
    public StringProperty CharacterColumn = new SimpleStringProperty();



    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void display() {
        Platform.runLater(()->{
            //redraw maze
            mazeDisplayer.setMaze(theArrMaze);
            //redraw position
            int characterPositionRow = viewModel.getCharacterPositionRow();
            int characterPositionColumn = viewModel.getCharacterPositionColumn();
            mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
            //update property position
            CharacterRow.set(characterPositionRow + "");
            CharacterColumn.set(characterPositionColumn + "");
            //update in user's view
            lbl_rowsNum.textProperty().bind(viewModel.characterPositionRow);
            lbl_columnsNum.textProperty().bind(viewModel.characterPositionColumn);
        });


    }

    public void SolutionMaze(int[][] sol) {

        mazeDisplayer.setSol(sol);
    }


    private void finishMaze() {
        win = true;
        mazeDisplayer.playTheWin();
        mazeDisplayer.redrawFinish();
    }

    public void generateMaze() {
        win = false;
        solve= false;
        String rowString = txtfld_rowsNum.getText();
        String colString = txtfld_columnsNum.getText();
        int x=0;
        char c;
        boolean flag1 = true;
        int ascii;
        String rowcol = rowString + colString;
        while (x < rowcol.length() && flag1){
            c=rowcol.charAt(x);
            ascii = (int) c;
            if (ascii< 48 || ascii>57){
                flag1 = false;
                Stage theWindow = new Stage();
                theWindow.initModality(Modality.APPLICATION_MODAL);
                theWindow.setTitle("Warning!!!");
                theWindow.setMinHeight(100);
                theWindow.setMinWidth(300);
                Pane pane = new Pane();

                Scene scene = new Scene(pane);

                String config = "sory!\n" + "you can not put characters that are not a numbers";

                Label label = new Label(config);
                pane.getChildren().addAll(label);

                theWindow.setScene(scene);
                theWindow.showAndWait();
            }
            x++;
        }
        if (flag1) {
            int row = Integer.valueOf(txtfld_rowsNum.getText());
            int col = Integer.valueOf(txtfld_columnsNum.getText());

            if (flag1 && (row < 10 || col < 10)) {
                Stage theWindow = new Stage();
                theWindow.initModality(Modality.APPLICATION_MODAL);
                theWindow.setTitle("Warning!!!");
                theWindow.setMinHeight(100);
                theWindow.setMinWidth(300);
                Pane pane = new Pane();

                Scene scene = new Scene(pane);

                String config = "sory!\n" + "you can not put a number smaller than 10";

                Label label = new Label(config);
                pane.getChildren().addAll(label);

                theWindow.setScene(scene);
                theWindow.showAndWait();
            } else  {
                btn_solveMaze.setDisable(false);
                viewModel.generateMaze(row, col);
            }
        }

    }

    public void solveMaze() {
        solve= true;
        viewModel.solveMaze();
        btn_solveMaze.setDisable(true);

    }

    public void stopMusic() {
        mazeDisplayer.PlayBackgroundSounds();

    }

    public void saveMaze()
    {
        viewModel.saveMaze(txtfld_nameSaveMaze.getText());
    }

    public void loadMaze()
    {
        if(!viewModel.loadMaze(txtfld_nameLoadMaze.getText()))
        {
            Stage theWindow = new Stage();
            theWindow.initModality(Modality.APPLICATION_MODAL);
            theWindow.setTitle("Warning!!!");
            theWindow.setMinHeight(100);
            theWindow.setMinWidth(300);
            Pane pane = new Pane();

            Scene scene = new Scene(pane);

            String config = "sory!\n" + "You can not put a name that does not exist";

            Label label = new Label(config);
            pane.getChildren().addAll(label);

            theWindow.setScene(scene);
            theWindow.showAndWait();
        }
    }


    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void KeyPressed(KeyEvent keyEvent) {
        viewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();
    }

    public String getCharacterRow() {
        return CharacterRow.get();
    }

    public StringProperty characterRowProperty() {
        return CharacterRow;
    }

    public String getCharacterColumn() {
        return CharacterColumn.get();
    }

    public StringProperty characterColumnProperty() {
        return CharacterColumn;
    }

    public void setResizeEvent(Scene scene) {

        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {

                mazeDisplayer.setWidth(0.8*txtfld_BorderPane.getWidth());
                if(win == false)
                {
                    mazeDisplayer.redraw();
                }
                else if(solve == true)
                {
                    mazeDisplayer.redrawSolution();
                }
                else {
                    mazeDisplayer.redrawFinish();
                }

            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {

                mazeDisplayer.setHeight(txtfld_BorderPane.getHeight());
                if(win == false)
                {
                    mazeDisplayer.redraw();
                }
                else if(solve == true)
                {
                    mazeDisplayer.redrawSolution();
                }
                else {
                    mazeDisplayer.redrawFinish();
                }
            }
        });
    }

    public void About() {

        try {
            AboutController aboutViewController = new AboutController();
            aboutViewController.display();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void Help() {

        try {
            HelpController helpViewController = new HelpController();
            helpViewController.display();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public void properties(){
        PropertiesDisplay prop = new PropertiesDisplay();
        prop.propertiesDisplay();
    }

    public void setStage(Stage s)
    {
        this.myStage = s;
        myStage.setOnCloseRequest( event -> {
            event.consume();
            myExit();
        });
    }

    public void myExit() {
        this.viewModel.exit();
        myStage.close();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == viewModel) {

            if(arg == "solve")
            {
                SolutionMaze(viewModel.getSolution());
            }

            else if(arg == "finish")
            {
                btn_solveMaze.setDisable(true);
                finishMaze();
            }
            else {
                theArrMaze = viewModel.getMaze();
                display();
                btn_solveMaze.setDisable(false);
                btn_generateMaze.setDisable(false);
            }

        }


    }



}


    //endregion
