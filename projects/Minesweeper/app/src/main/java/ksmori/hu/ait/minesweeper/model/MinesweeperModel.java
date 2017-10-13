package ksmori.hu.ait.minesweeper.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MinesweeperModel {

    public enum State {
        UNDUG, DUG, FLAG
    }

    public final static int MINE = -1;
    public final static int DEFAULT_SIZE = 5;
    public final static int DEFAULT_MINES = 3;

    private static MinesweeperModel instance = null;

    private final State[][] boardStates;
    private final int[][] boardValues;
    private final int size;
    private final int numMines;

    private int numCellsDug;
    private int numFlags;
    private boolean ongoing;

    private MinesweeperModel(int size, int numMines) {
        this.size = size;
        this.numMines = numMines;

        boardStates = new State[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                boardStates[x][y] = State.UNDUG;
            }
        }
        boardValues = new int[size][size];

        numCellsDug = 0;
        numFlags = 0;
        ongoing = true;

        initializeMines();
        initializeValues();
    }

    private void initializeMines() {
        List<Integer> range = new ArrayList<>();
        for (int i = 0; i < size * size; i++) {
            range.add(i);
        }
        Collections.shuffle(range);
        for (int m = 0; m < numMines; m++) {
            int x = range.get(m) / size;
            int y = range.get(m) % size;
            boardValues[x][y] = MINE;
        }
    }

    private void initializeValues() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (boardValues[x][y] != MINE) {
                    boardValues[x][y] = countSurroundingMines(x, y);
                }
            }
        }
    }

    private int countSurroundingMines(int x, int y) {
        int numSurroundingMines = 0;
        for (int neighborX  = Math.max(0, x - 1); neighborX < Math.min(size, x + 2); neighborX++) {
            for (int neighborY = Math.max(0, y - 1); neighborY < Math.min(size, y + 2); neighborY++) {
                if (boardValues[neighborX][neighborY] == MINE) {
                    numSurroundingMines++;
                }
            }
        }
        return numSurroundingMines;
    }

    public static MinesweeperModel getInstance() {
        if (instance == null) {
            instance = new MinesweeperModel(DEFAULT_SIZE, DEFAULT_MINES);
        }
        return instance;
    }

    public static void createCustomInstance(int size, int numMines) {
        instance = new MinesweeperModel(size, numMines);
    }

    /**
     * @return true if the board has been changed
     */
    public boolean digCell(int x, int y) {
        if (boardStates[x][y] == State.UNDUG) {
            boardStates[x][y] = State.DUG;
            numCellsDug++;
            digRecursive(x, y);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return true if the board has been changed
     */
    public boolean toggleCellFlag(int x, int y) {
        if (boardStates[x][y] == State.FLAG) {
            boardStates[x][y] = State.UNDUG;
            numFlags--;
            return true;
        } else if (boardStates[x][y] == State.UNDUG){
            boardStates[x][y] = State.FLAG;
            numFlags++;
            return true;
        } else {
            return false;
        }
    }

    private void digRecursive(int x, int y) {
        if (boardValues[x][y] == 0) {
            for (int neighborX = Math.max(0, x - 1); neighborX < Math.min(size, x + 2); neighborX++) {
                for (int neighborY = Math.max(0, y - 1); neighborY < Math.min(size, y + 2); neighborY++) {
                    if (!(x == neighborX && y == neighborY) && boardStates[neighborX][neighborY] == State.UNDUG) {
                        boardStates[neighborX][neighborY] = State.DUG;
                        numCellsDug++;
                        digRecursive(neighborX, neighborY);
                    }
                }
            }
        }
    }

    public boolean checkMoveOkay(int x, int y) {
        if (boardStates[x][y] == State.DUG && boardValues[x][y] == MINE) {
            return false;
        }
        return true;
    }

    public boolean checkWin() {
        if (numCellsDug == size * size - numMines) {
            return true;
        }
        return false;
    }

    public int getSize() {
        return size;
    }

    public int getNumMines() {
        return numMines;
    }

    public State getCellState(int x, int y) {
        return boardStates[x][y];
    }

    public int getCellValue(int x, int y) {
        return boardValues[x][y];
    }

    public void setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
    }

    public boolean ongoing() {
        return ongoing;
    }

    public int getNumFlags() {
        return numFlags;
    }

    public void revealAllMines() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (boardValues[x][y] == MINE) {
                    boardStates[x][y] = State.DUG;
                }
            }
        }
        ongoing = false;
    }

    public static void resetModel(int size, int numMines) {
        createCustomInstance(size, numMines);
    }
}
