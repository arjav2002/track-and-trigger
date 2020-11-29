package com.oopcows.trackandtrigger.dashboard.todolists;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.ItemTouchHelper;

public class DragButton extends AppCompatImageButton {

    private ItemTouchHelper touchHelper;
    private TodoAdapter.TodoHolder holder;

    public DragButton(@NonNull Context context) {
        super(context);
    }

    public DragButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DragButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTouchHelper(ItemTouchHelper touchHelper) {
        this.touchHelper = touchHelper;
    }

    public void setViewHolder(TodoAdapter.TodoHolder holder) {
        this.holder = holder;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            touchHelper.startDrag(holder);
        }
        return false;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

}
