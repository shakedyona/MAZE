package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import javafx.scene.paint.Color;
import javafx.util.Duration;


/**
 * Created by שקד on 16/06/2017.
 */
public class MazeDisplayer extends Canvas {

    private int [][] myMaze;
    private int [][] sol;
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;

    //region Properties
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    private StringProperty imageFileNamefinish = new SimpleStringProperty();

    //media

    private static Media sound = new  Media(new File("./resources/music/Halloween.mp3").toURI().toString());
    private static MediaPlayer mediaPlayer = new MediaPlayer(sound);
    private boolean playerWinGame;
    private MediaPlayer mediaPlayerWin;
    private Media mediaWin;
    private static boolean ON = false;

    public static void PlayBackgroundSounds(){

        if(ON == false) {
            mediaPlayer.setMute(false);
            mediaPlayer.setAutoPlay(true);
            ON = true;
            mediaPlayer.setVolume(0.3);
            mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));

        }
        else {
            ON=false;
            mediaPlayer.setMute(true);
        }
    }

    public MazeDisplayer(){
        playerWinGame = false;
        this.PlayBackgroundSounds();
    }

    public void playTheWin(){
        mediaWin=new Media(new File("./resources/music/evil_laugh.mp3").toURI().toString());
        mediaPlayerWin = new MediaPlayer(mediaWin);
        mediaPlayerWin.setAutoPlay(true);
    }

    public void setMaze(int[][] maze) {
        this.myMaze = maze;
        redraw();
    }

    public void setSol(int[][] sol) {
        this.sol = sol;
        redrawSolution();
    }



    public void setCharacterPosition(int row, int column) {
        characterPositionRow = row;
        characterPositionColumn = column;
        redraw();
    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }



    public void redraw() {
        if (myMaze != null) {
            double canvasHeight = 0.8*getHeight();
            double canvasWidth = 0.8*getWidth();
            double cellHeight = canvasHeight / myMaze.length;
            double cellWidth = canvasWidth /  myMaze[0].length;

            try {
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));

                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());

                //Draw Maze
                for (int i = 0; i < myMaze.length; i++) {
                    for (int j = 0; j < myMaze[i].length; j++) {
                        if ( myMaze[i][j] == 1) {
                            gc.drawImage(wallImage, j * cellWidth, i * cellHeight,cellWidth , cellHeight);
                        }
                    }

                }
                //Draw Character
                gc.drawImage(characterImage, characterPositionColumn * cellWidth , characterPositionRow * cellHeight, cellWidth,cellHeight );
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void redrawSolution() {
        if (sol != null) {
            double canvasHeight = 0.8*getHeight();
            double canvasWidth = 0.8*getWidth();
            double cellHeight = canvasHeight / sol.length;
            double cellWidth = canvasWidth /  sol[0].length;

                GraphicsContext gc = getGraphicsContext2D();

                //Draw solution
                for (int i = 0; i < sol.length; i++) {
                    for (int j = 0; j < sol[i].length; j++) {
                        //System.out.print(myMaze[i][j]+" ");
                        if ( sol[i][j] == 2) {

                            gc.setFill(Color.BLACK);
                            gc.fillRect( j * cellWidth, i * cellHeight,cellWidth , cellHeight);
                        }
                    }

                }
        }
    }

    public void redrawFinish() {

        if (myMaze != null) {
            try {
                Image winImage = new Image(new FileInputStream(imageFileNamefinish.get()));
                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());

                gc.drawImage(winImage,0, 0, getWidth(), getHeight());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public String getImageFileNamefinish() {
        return imageFileNamefinish.get();
    }

    public void setImageFileNamefinish(String imageFileNamefinish) {
        this.imageFileNamefinish.set(imageFileNamefinish);
    }


    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }




    //endregion


}
