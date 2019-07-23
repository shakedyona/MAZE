package Model;

import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import Server.*;
import Client.*;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.*;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by שקד on 16/06/2017.
 */
public class MyModel extends Observable implements IModel {

    Maze maze;
    Solution mySol;
    private int characterPositionRow;
    private int characterPositionColumn;
    //servers
    Server mazeGeneratingServer;
    Server solveSearchProblemServer;


    //server
    private ExecutorService threadPool = Executors.newCachedThreadPool();

    public MyModel() {

        mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        //Raise the servers !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    public void startServers() {
        mazeGeneratingServer.start();
        solveSearchProblemServer.start();
    }

    public void stopServers() {
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();
    }

    @Override
    public Maze getMyMaze() {
        return maze;
    }

    @Override
    public Solution getMySolution() {

        return mySol;
    }

    @Override
    public void generateMaze(int row, int col) {

        //Generate maze
        threadPool.execute(() -> {
            try {
                CommunicateWithServer_MazeGenerating(row, col);

            } catch (Exception e) {
                e.printStackTrace();
            }
            setChanged();
            notifyObservers();

        });
    }

    @Override
    public void solveMaze() {

        //solveMaze maze
        threadPool.execute(() -> {
            try {
                CommunicateWithServer_SolveSearchProblem();
            } catch (Exception e) {
                e.printStackTrace();
            }
            setChanged();
            notifyObservers("solve");
        });

    }

    private void CommunicateWithServer_MazeGenerating(int row, int col) {

        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[100000000 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes

                        maze = new Maze(decompressedMaze);

                        characterPositionRow = maze.getStartPosition().getRowIndex();
                        characterPositionColumn = maze.getStartPosition().getColumnIndex();
                        // maze.print();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }


    private void CommunicateWithServer_SolveSearchProblem() {

        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        mySol = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void moveCharacter(KeyCode movement) {

        int rowMaze = maze.getMaze().length - 1;
        int colMaze = maze.getMaze()[0].length - 1;

        int newRowPosition = characterPositionRow;
        int newColPosition = characterPositionColumn;


        switch (movement) {
            case UP:
                newRowPosition--;
                if (newRowPosition >= 0 && checkIfOk(newRowPosition, characterPositionColumn))
                    characterPositionRow--;
                break;
            case DOWN:
                newRowPosition++;
                if (newRowPosition <= rowMaze && checkIfOk(newRowPosition, characterPositionColumn))
                    characterPositionRow++;
                break;
            case RIGHT:
                newColPosition++;
                if (newColPosition <= colMaze && checkIfOk(characterPositionRow, newColPosition))
                    characterPositionColumn++;
                break;
            case LEFT:
                newColPosition--;
                if (newColPosition >= 0 && checkIfOk(characterPositionRow, newColPosition))
                    characterPositionColumn--;
                break;
        }

        if(characterPositionColumn == maze.getGoalPosition().getColumnIndex() && characterPositionRow==maze.getGoalPosition().getRowIndex())
        {
            setChanged();
            notifyObservers("finish");
        }

        else {
            setChanged();
            notifyObservers();
        }

    }

    private boolean checkIfOk(int row, int col) {
        return (maze.getMaze()[row][col] == 0);
    }


    @Override
    public void exit() {
        threadPool.shutdown();
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();
    }

    @Override
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    @Override
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }


    @Override
    public int[][] get_MyMaze() {
        return maze.getMaze();
    }

    @Override
    public int[][] get_MySolution() {

        ArrayList<AState> solutionPath = mySol.getSolutionPath();

        int row = maze.getMaze().length;
        int col = maze.getMaze()[0].length;

        int[][] arrSol = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                arrSol[i][j] = 0;
            }

        }

        for (AState state : solutionPath) {
            int size = state.getState().length();
            String newState = state.getState().substring(1, size - 1);
            String[] splitString = newState.split(",");
            int currRow = Integer.parseInt(splitString[0]);
            int currCol = Integer.parseInt(splitString[1]);

            arrSol[currRow][currCol] = 2;

        }

        return arrSol;
    }

    @Override
    public void saveMyMaze(String mazeFileName) {
        try {
            // save maze to a file
            OutputStream out = new MyCompressorOutputStream(new FileOutputStream(mazeFileName));
            out.write(maze.toByteArray());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean loadMyMaze(String nameLoad) {

        boolean Ok= true;
        byte[] savedMazeBytes = new byte[0];
        try {
            //read maze from file
            InputStream in = new MyDecompressorInputStream(new FileInputStream(nameLoad));
            savedMazeBytes = new byte[1000000];
            in.read(savedMazeBytes);
            in.close();
        }
        catch (IOException e) {
            //e.printStackTrace();
            Ok = false;
        }

        if (Ok)
        {
            Maze newMaze =  new Maze(savedMazeBytes);

            maze =newMaze;
            characterPositionRow = maze.getStartPosition().getRowIndex();
            characterPositionColumn = maze.getStartPosition().getColumnIndex();

            setChanged();
            notifyObservers();
        }


        return Ok;

    }
}
