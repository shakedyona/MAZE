package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

/**
 * Created by שקד on 16/06/2017.
 */
public interface IModel {

    public Maze getMyMaze();
    public int [][] get_MyMaze();
    void generateMaze(int width, int height);
    public void solveMaze();
    public void moveCharacter(KeyCode movement);
    public int getCharacterPositionRow();
    public int getCharacterPositionColumn();
    public void exit();

    public Solution getMySolution();
    public int [][] get_MySolution();

    void saveMyMaze(String nameMaze);

    boolean loadMyMaze(String nameLoad);
}
