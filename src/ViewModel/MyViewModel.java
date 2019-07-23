package ViewModel;

import Model.IModel;
import algorithms.search.AState;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel iModel;
    private int characterPositionRowIndex;
    private int characterPositionColumnIndex;

    public StringProperty characterPositionRow = new SimpleStringProperty("1"); //For Binding
    public StringProperty characterPositionColumn = new SimpleStringProperty("1"); //For Binding

    public MyViewModel(IModel model){
        this.iModel = model;
    }

    public void generateMaze(int row, int col){
        iModel.generateMaze(row, col);
    }

    public void solveMaze(){
        iModel.solveMaze();
    }

    public void moveCharacter(KeyCode movement){
        iModel.moveCharacter(movement);
    }

    public int[][] getMaze() {
        return iModel.get_MyMaze();
    }

    public int getCharacterPositionRow() {
        return characterPositionRowIndex;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumnIndex;
    }


    public int[][] getSolution() {
        return iModel.get_MySolution();
    }

    public void saveMaze(String nameMaze) {
        iModel.saveMyMaze(nameMaze);
    }

    public boolean loadMaze(String nameLoad) {
        return iModel.loadMyMaze(nameLoad);
    }
    public void exit() {
        iModel.exit();
    }
    @Override
    public void update(Observable o, Object arg) {
        if (o==iModel){

            if(arg=="solve")
            {
                setChanged();
                notifyObservers(arg);
            }

            else if (arg == "finish")
            {
                setChanged();
                notifyObservers(arg);
            }
            else
            {
                Platform.runLater(() -> {
                    characterPositionRowIndex = iModel.getCharacterPositionRow();
                    characterPositionRow.set(characterPositionRowIndex + "");
                    characterPositionColumnIndex = iModel.getCharacterPositionColumn();
                    characterPositionColumn.set(characterPositionColumnIndex + "");
                    setChanged();
                    notifyObservers();
                });
            }
        }
    }




}
