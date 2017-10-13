package ksmori.hu.ait.minesweeper.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ksmori.hu.ait.minesweeper.GameActivity;
import ksmori.hu.ait.minesweeper.R;
import ksmori.hu.ait.minesweeper.model.MinesweeperModel;
import ksmori.hu.ait.minesweeper.model.MinesweeperModel.State;

public class MinesweeperView extends View {

    private Paint paintBackground;
    private Paint paintDugBackground;
    private Paint paintLine;
    private Paint paintMine;
    private Paint paintFlag;
    private Paint paintNumber;
    private boolean digMode = true;
    private static final int DRAWING_OFFSET = 20;

    public MinesweeperView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintBackground = new Paint();
        paintBackground.setColor(Color.GRAY);
        paintBackground.setStyle(Paint.Style.FILL);

        paintDugBackground = new Paint();
        paintDugBackground.setColor(Color.LTGRAY);
        paintDugBackground.setStyle(Paint.Style.FILL);

        paintLine = new Paint();
        paintLine.setColor(Color.WHITE);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(5);

        paintMine = new Paint();
        paintMine.setColor(Color.BLACK);
        paintMine.setStyle(Paint.Style.STROKE);
        paintMine.setStrokeWidth(10);

        paintFlag = new Paint();
        paintFlag.setColor(Color.RED);
        paintFlag.setStyle(Paint.Style.STROKE);
        paintFlag.setStrokeWidth(10);

        paintNumber = new Paint();
        paintNumber.setColor(Color.BLUE);
        paintNumber.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBackground);
        paintNumber.setTextSize(getHeight() / MinesweeperModel.getInstance().getSize() / 1.5f);
        int minesLeft = MinesweeperModel.getInstance().getNumMines() - MinesweeperModel.getInstance().getNumFlags();
        ((GameActivity) getContext()).updateMinesLeft(minesLeft);
        drawCurrentState(canvas);
        drawGameGrid(canvas);
    }

    private void drawCurrentState(Canvas canvas) {
        int size = MinesweeperModel.getInstance().getSize();
        float cellWidth = 1f * getWidth() / size;
        float cellHeight = 1f * getHeight() / size;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (MinesweeperModel.getInstance().getCellState(x, y) == State.DUG) {
                    canvas.drawRect(x * cellWidth, y * cellHeight, (x + 1) * cellWidth, (y + 1) * cellHeight, paintDugBackground);
                    int cellValue = MinesweeperModel.getInstance().getCellValue(x, y);
                    if (cellValue == MinesweeperModel.MINE) {
                        // DRAW A MINE
                        float centerX = x * cellWidth + cellWidth / 2;
                        float centerY = y * cellHeight + cellHeight / 2;
                        float radius = cellHeight / 2f - DRAWING_OFFSET;
                        canvas.drawCircle(centerX, centerY, radius, paintMine);
                    } else if (cellValue >= 1){
                        // DRAW A NUMBER
                        Rect bounds = new Rect();
                        paintNumber.getTextBounds(String.valueOf(cellValue), 0, 1, bounds);
                        float textX = (2 * x + 1) * cellWidth / 2f - bounds.width() / 2f - bounds.left;
                        float textY = (2 * y + 1) * cellHeight / 2f + bounds.height() / 2f - bounds.bottom;
                        canvas.drawText(String.valueOf(cellValue), textX, textY, paintNumber);
                    }

                } else if (MinesweeperModel.getInstance().getCellState(x, y) == State.FLAG) {
                    // DRAW A FLAG (X)
                    canvas.drawLine(
                            x * cellWidth + DRAWING_OFFSET, y * cellHeight + DRAWING_OFFSET,
                            (x + 1) * cellWidth - DRAWING_OFFSET, (y + 1) * cellHeight - DRAWING_OFFSET,
                            paintFlag);
                    canvas.drawLine(
                            (x + 1) * cellWidth - DRAWING_OFFSET, y * cellHeight + DRAWING_OFFSET,
                            x * cellWidth + DRAWING_OFFSET, (y + 1) * cellHeight - DRAWING_OFFSET,
                            paintFlag);
                }
            }
        }
    }

    private void drawGameGrid(Canvas canvas) {
        int size = MinesweeperModel.getInstance().getSize();
        int width = getWidth();
        int height = getHeight();

        canvas.drawRect(0, 0, width, height, paintLine);
        for (int i = 1; i < size; i++) {
            canvas.drawLine(0, i * height / size, width, i * height / size, paintLine);
            canvas.drawLine(i * width / size, 0, i * width / size, height, paintLine);
        }
    }

    public void setDigMode(boolean digMode) {
        this.digMode = digMode;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && MinesweeperModel.getInstance().ongoing()) {
            int x = (int) (event.getX()) / (getWidth() / MinesweeperModel.getInstance().getSize());
            int y = (int) (event.getY()) / (getHeight() / MinesweeperModel.getInstance().getSize());
            if (x >= MinesweeperModel.getInstance().getSize()) {
                x = MinesweeperModel.getInstance().getSize() - 1;
            }
            if (y >= MinesweeperModel.getInstance().getSize()) {
                y = MinesweeperModel.getInstance().getSize() - 1;
            }

            boolean gameChanged = false;
            if (digMode) {
                gameChanged = MinesweeperModel.getInstance().digCell(x, y);
            } else {
                gameChanged = MinesweeperModel.getInstance().toggleCellFlag(x, y);
            }

            if (gameChanged) {
                invalidate();
                if (!MinesweeperModel.getInstance().checkMoveOkay(x, y)) {
                    MinesweeperModel.getInstance().revealAllMines();
                    showEndMessage(R.string.loseMsg);
                    MinesweeperModel.getInstance().setOngoing(false);
                } else if (MinesweeperModel.getInstance().checkWin()) {
                    showEndMessage(R.string.winMsg);
                    MinesweeperModel.getInstance().setOngoing(false);
                }
            }
        }
        return true;
    }

    private void showEndMessage(int messageID) {
        Toast toast = Toast.makeText(getContext(), messageID, Toast.LENGTH_SHORT);
        ViewGroup toastLayout = (ViewGroup) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTextSize(50);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
        setMeasuredDimension(d, d);
    }

    public void restart(int size, int numMines) {
        digMode = true;
        MinesweeperModel.resetModel(size, numMines);
        ((GameActivity) getContext()).updateMinesLeft(MinesweeperModel.getInstance().getNumMines());
        invalidate();
    }
}
