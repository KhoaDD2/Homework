package android.support.v13.view;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

public class DragStartHelper {
    private boolean mDragging;
    private int mLastTouchX;
    private int mLastTouchY;
    private final OnDragStartListener mListener;
    private final View.OnLongClickListener mLongClickListener = new View.OnLongClickListener() {
        public boolean onLongClick(View v) {
            return DragStartHelper.this.onLongClick(v);
        }
    };
    private final View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            return DragStartHelper.this.onTouch(v, event);
        }
    };
    private final View mView;

    public interface OnDragStartListener {
        boolean onDragStart(View view, DragStartHelper dragStartHelper);
    }

    public DragStartHelper(View view, OnDragStartListener listener) {
        this.mView = view;
        this.mListener = listener;
    }

    public void attach() {
        this.mView.setOnLongClickListener(this.mLongClickListener);
        this.mView.setOnTouchListener(this.mTouchListener);
    }

    public void detach() {
        this.mView.setOnLongClickListener((View.OnLongClickListener) null);
        this.mView.setOnTouchListener((View.OnTouchListener) null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0018, code lost:
        if (r2 != 3) goto L_0x0050;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouch(android.view.View r7, android.view.MotionEvent r8) {
        /*
            r6 = this;
            float r0 = r8.getX()
            int r0 = (int) r0
            float r1 = r8.getY()
            int r1 = (int) r1
            int r2 = r8.getAction()
            r3 = 0
            if (r2 == 0) goto L_0x004b
            r4 = 1
            if (r2 == r4) goto L_0x0048
            r5 = 2
            if (r2 == r5) goto L_0x001b
            r4 = 3
            if (r2 == r4) goto L_0x0048
            goto L_0x0050
        L_0x001b:
            r2 = 8194(0x2002, float:1.1482E-41)
            boolean r2 = android.support.p000v4.view.MotionEventCompat.isFromSource(r8, r2)
            if (r2 == 0) goto L_0x0050
            int r2 = r8.getButtonState()
            r2 = r2 & r4
            if (r2 != 0) goto L_0x002b
            goto L_0x0050
        L_0x002b:
            boolean r2 = r6.mDragging
            if (r2 == 0) goto L_0x0030
            goto L_0x0050
        L_0x0030:
            int r2 = r6.mLastTouchX
            if (r2 != r0) goto L_0x0039
            int r2 = r6.mLastTouchY
            if (r2 != r1) goto L_0x0039
            goto L_0x0050
        L_0x0039:
            r6.mLastTouchX = r0
            r6.mLastTouchY = r1
            android.support.v13.view.DragStartHelper$OnDragStartListener r2 = r6.mListener
            boolean r2 = r2.onDragStart(r7, r6)
            r6.mDragging = r2
            boolean r2 = r6.mDragging
            return r2
        L_0x0048:
            r6.mDragging = r3
            goto L_0x0050
        L_0x004b:
            r6.mLastTouchX = r0
            r6.mLastTouchY = r1
        L_0x0050:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v13.view.DragStartHelper.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }

    public boolean onLongClick(View v) {
        return this.mListener.onDragStart(v, this);
    }

    public void getTouchPosition(Point point) {
        point.set(this.mLastTouchX, this.mLastTouchY);
    }
}
