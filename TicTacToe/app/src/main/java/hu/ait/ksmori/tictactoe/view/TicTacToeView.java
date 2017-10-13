package hu.ait.ksmori.tictactoe.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hu.ait.ksmori.tictactoe.MainActivity;
import hu.ait.ksmori.tictactoe.model.TicTacToeModel;

public class TicTacToeView extends View {

    private Paint paintBackground;
    private Paint paintLine;
    private Paint paintCircle;
    private Paint paintCross;

    public TicTacToeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintBackground = new Paint();
        paintBackground.setColor(Color.BLACK);
        paintBackground.setStyle(Paint.Style.FILL);

        paintLine = new Paint();
        paintLine.setColor(Color.WHITE);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(5);

        paintCircle = new Paint();
        paintCircle.setColor(Color.RED);
        paintCircle.setStyle(Paint.Style.STROKE);
        paintCircle.setStrokeWidth(10);

        paintCross = new Paint();
        paintCross.setColor(Color.GREEN);
        paintCross.setStyle(Paint.Style.STROKE);
        paintCross.setStrokeWidth(10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBackground);
        drawGameGrid(canvas);
        drawPlayers(canvas);
    }

    private void drawGameGrid(Canvas canvas) {
        // border
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintLine);
        // two horizontal lines
        canvas.drawLine(0, getHeight() / 3, getWidth(), getHeight() / 3, paintLine);
        canvas.drawLine(0, 2 * getHeight() / 3, getWidth(), 2 * getHeight() / 3, paintLine);
        // two vertical lines
        canvas.drawLine(getWidth() / 3, 0, getWidth() / 3, getHeight(), paintLine);
        canvas.drawLine(2 * getWidth() / 3, 0, 2 * getWidth() / 3, getHeight(), paintLine);
    }

    private void drawPlayers(Canvas canvas) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (TicTacToeModel.getInstance().getField(i,j) == TicTacToeModel.CIRCLE) {

                    // draw a circle at the center of the field

                    // X coordinate: left side of the square + half width of the square
                    float centerX = i * getWidth() / 3 + getWidth() / 6;
                    float centerY = j * getHeight() / 3 + getHeight() / 6;
                    int radius = getHeight() / 6 - 2;

                    canvas.drawCircle(centerX, centerY, radius, paintCircle);

                } else if (TicTacToeModel.getInstance().getField(i,j) == TicTacToeModel.CROSS) {
                    canvas.drawLine(i * getWidth() / 3, j * getHeight() / 3,
                            (i + 1) * getWidth() / 3,
                            (j + 1) * getHeight() / 3, paintCross);

                    canvas.drawLine((i + 1) * getWidth() / 3, j * getHeight() / 3,
                            i * getWidth() / 3, (j + 1) * getHeight() / 3, paintCross);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (TicTacToeModel.getInstance().getLastStart() == 0) {
            TicTacToeModel.getInstance().startTimer();
        }
        if (TicTacToeModel.getInstance().getWinner() == 0) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int tX = ((int) event.getX()) / (getWidth() / 3);
                int tY = ((int) event.getY()) / (getHeight() / 3);

                if (TicTacToeModel.getInstance().getField(tX, tY) == TicTacToeModel.EMPTY) {
                    TicTacToeModel.getInstance().setField(tX, tY,
                            TicTacToeModel.getInstance().getNextPlayer());
                    TicTacToeModel.getInstance().changeNextPlayer();
                    TicTacToeModel.getInstance().addTime();
                    invalidate();

                    String next = "O";
                    if (TicTacToeModel.getInstance().getNextPlayer() == TicTacToeModel.CROSS) {
                        next = "X";
                    }

                    String message = "";
                    if (TicTacToeModel.getInstance().getWinner() == TicTacToeModel.CIRCLE) {
                        message = "O has won!";
                    } else if (TicTacToeModel.getInstance().getWinner() == TicTacToeModel.CROSS) {
                        message = "X has won!";
                    } else if (TicTacToeModel.getInstance().getWinner() == 3) {
                        message = "It's a draw!";
                    } else {
                        message = "Next is " + next;
                    }

                    message += "\n";
                    message += "Time for O: " + new DecimalFormat("##.##").format(TicTacToeModel.getInstance().getCircleTime()) + " seconds";
                    message += "\n";
                    message += "Time for X: " + new DecimalFormat("##.##").format(TicTacToeModel.getInstance().getCrossTime()) + " seconds";

                    ((MainActivity) getContext()).setMessage(message);
                }
            }
        }

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
        setMeasuredDimension(d, d);
    }

    public void resetGame() {
        TicTacToeModel.getInstance().resetModel();
        ((MainActivity) getContext()).setMessage("Touch the game area to play");
        invalidate();
    }
}
