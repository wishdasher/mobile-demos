package hu.ait.ksmori.tictactoe.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TicTacToeModel {

    private static TicTacToeModel instance = null;

    public static final short EMPTY = 0;
    public static final short CIRCLE = 1;
    public static final short CROSS = 2;

    private short[][] model = {
            {EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY}
    };

    private short nextPlayer = CIRCLE;

    List<List<Integer>> winningCombos= new ArrayList<>();
    long lastStart = 0;
    long circleTime = 0;
    long crossTime = 0;

    private TicTacToeModel() {
        winningCombos.add(new ArrayList<>(Arrays.asList(0, 0, 0, 1, 0, 2)));
        winningCombos.add(new ArrayList<>(Arrays.asList(1, 0, 1, 1, 1, 2)));
        winningCombos.add(new ArrayList<>(Arrays.asList(2, 0, 2, 1, 2, 2)));
        winningCombos.add(new ArrayList<>(Arrays.asList(0, 0, 1, 0, 2, 0)));
        winningCombos.add(new ArrayList<>(Arrays.asList(0, 1, 1, 1, 2, 1)));
        winningCombos.add(new ArrayList<>(Arrays.asList(0, 2, 1, 2, 2, 2)));
        winningCombos.add(new ArrayList<>(Arrays.asList(0, 0, 1, 1, 2, 2)));
        winningCombos.add(new ArrayList<>(Arrays.asList(0, 2, 1, 1, 2, 0)));

    }

    public static TicTacToeModel getInstance() {
        if (instance == null) {
            instance = new TicTacToeModel();
        }
        return instance;
    }

    public void resetModel() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                model[i][j] = EMPTY;
            }
        }
        nextPlayer = CIRCLE;
        crossTime = 0;
        circleTime = 0;
        lastStart = 0;
    }

    public void startTimer() {
        lastStart = System.currentTimeMillis();
    }

    public void addTime() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastStart;
        lastStart = currentTime;
        if (nextPlayer == CIRCLE) {
            crossTime += elapsedTime;
        } else {
            circleTime += elapsedTime;
        }
    }

    public double getCircleTime() {
        return circleTime / 1000.0;
    }

    public double getCrossTime() {
        return crossTime / 1000.0;
    }

    public long getLastStart() {
        return lastStart;
    }

    public short checkWinner() {
        return 0;
    }

    public void changeNextPlayer() {
        nextPlayer = (nextPlayer == CIRCLE) ? CROSS : CIRCLE;
    }

    public void setField(int x, int y, short player) {
        model[x][y] = player;
    }

    public short getField(int x, int y) {
        return model[x][y];
    }

    public short getNextPlayer() {
        return nextPlayer;
    }

    public short getWinner() {
        if (isDraw()) {
            return 3;
        }


        for (List<Integer> combo : winningCombos) {
            short x = model[combo.get(0)][combo.get(1)];
            short y = model[combo.get(2)][combo.get(3)];
            short z = model[combo.get(4)][combo.get(5)];
            if (x == y && x == z && x != 0) {
                return model[combo.get(0)][combo.get(1)];
            }
        }

        return 0;
    }

    private boolean isDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (model[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

}
