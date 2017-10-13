package ksmori.hu.ait.recycleviewdemo2.touch;

/**
 * Created by Sophie on 20-Mar-17.
 */

public interface TodoTouchHelperAdapter {
    void onItemDismiss(int position);
    void onItemMove(int fromPosition, int toPosition);
}
