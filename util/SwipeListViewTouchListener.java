package com.moocall.moocall.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ViewFlipper;
import com.rd.animation.ScaleAnimation;

public class SwipeListViewTouchListener implements OnTouchListener {
    private boolean dismissRight;
    private int mDownPosition;
    private View mDownView;
    private float mDownX;
    private float mDownY;
    private ListView mListView;
    private long mLongAnimationTime;
    private int mMaxFlingVelocity;
    private int mMinFlingVelocity;
    private boolean mPaused;
    private long mShortAnimationTime;
    private int mSlop;
    private boolean mSwiping;
    private int mSwipingSlop;
    private VelocityTracker mVelocityTracker;
    private int mViewWidth = 1;

    class C06251 implements OnScrollListener {
        C06251() {
        }

        public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            boolean z = true;
            SwipeListViewTouchListener swipeListViewTouchListener = SwipeListViewTouchListener.this;
            if (scrollState == 1) {
                z = false;
            }
            swipeListViewTouchListener.setEnabled(z);
        }

        public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        }
    }

    public SwipeListViewTouchListener(ListView listView) {
        ViewConfiguration vc = ViewConfiguration.get(listView.getContext());
        this.mSlop = vc.getScaledTouchSlop();
        this.mMinFlingVelocity = vc.getScaledMinimumFlingVelocity() * 16;
        this.mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
        this.mShortAnimationTime = (long) listView.getContext().getResources().getInteger(17694722);
        this.mLongAnimationTime = (long) listView.getContext().getResources().getInteger(17694722);
        this.mListView = listView;
    }

    public void setEnabled(boolean enabled) {
        this.mPaused = !enabled;
    }

    public OnScrollListener makeScrollListener() {
        return new C06251();
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (this.mViewWidth < 2) {
            this.mViewWidth = this.mListView.getWidth();
        }
        float deltaX;
        switch (motionEvent.getActionMasked()) {
            case 0:
                if (this.mPaused) {
                    return false;
                }
                Rect rect = new Rect();
                int childCount = this.mListView.getChildCount();
                int[] listViewCoords = new int[2];
                this.mListView.getLocationOnScreen(listViewCoords);
                int x = ((int) motionEvent.getRawX()) - listViewCoords[0];
                int y = ((int) motionEvent.getRawY()) - listViewCoords[1];
                for (int i = 0; i < childCount; i++) {
                    View child = this.mListView.getChildAt(i);
                    child.getHitRect(rect);
                    if (rect.contains(x, y)) {
                        this.mDownView = child;
                        if (this.mDownView != null && this.mDownView.getChildCount() > 1) {
                            this.mDownX = motionEvent.getRawX();
                            this.mDownY = motionEvent.getRawY();
                            this.mDownPosition = this.mListView.getPositionForView(this.mDownView);
                            this.mVelocityTracker = VelocityTracker.obtain();
                            this.mVelocityTracker.addMovement(motionEvent);
                        }
                        return false;
                    }
                }
                this.mDownX = motionEvent.getRawX();
                this.mDownY = motionEvent.getRawY();
                this.mDownPosition = this.mListView.getPositionForView(this.mDownView);
                this.mVelocityTracker = VelocityTracker.obtain();
                this.mVelocityTracker.addMovement(motionEvent);
                return false;
            case 1:
                if (this.mVelocityTracker != null) {
                    deltaX = motionEvent.getRawX() - this.mDownX;
                    this.mVelocityTracker.addMovement(motionEvent);
                    this.mVelocityTracker.computeCurrentVelocity(1000);
                    float velocityX = this.mVelocityTracker.getXVelocity();
                    float absVelocityX = Math.abs(velocityX);
                    float absVelocityY = Math.abs(this.mVelocityTracker.getYVelocity());
                    boolean dismiss = false;
                    this.dismissRight = false;
                    if (Math.abs(deltaX) > ((float) (this.mViewWidth / 2)) && this.mSwiping) {
                        dismiss = true;
                        this.dismissRight = deltaX > 0.0f;
                    } else if (((float) this.mMinFlingVelocity) <= absVelocityX && absVelocityX <= ((float) this.mMaxFlingVelocity) && absVelocityY < absVelocityX && this.mSwiping) {
                        dismiss = ((velocityX > 0.0f ? 1 : (velocityX == 0.0f ? 0 : -1)) < 0 ? 1 : null) == ((deltaX > 0.0f ? 1 : (deltaX == 0.0f ? 0 : -1)) < 0 ? 1 : null);
                        this.dismissRight = this.mVelocityTracker.getXVelocity() > 0.0f;
                    }
                    if (dismiss) {
                        final ViewFlipper viewFlipper = (ViewFlipper) this.mDownView;
                        if (this.dismissRight) {
                            viewFlipper.animate().translationX((float) (-this.mViewWidth)).alpha(0.0f).setDuration(0).setListener(new AnimatorListenerAdapter() {

                                class C06261 extends AnimatorListenerAdapter {
                                    C06261() {
                                    }

                                    public void onAnimationStart(Animator animation) {
                                        viewFlipper.showPrevious();
                                    }
                                }

                                public void onAnimationEnd(Animator animation) {
                                    viewFlipper.animate().translationX(0.0f).alpha(ScaleAnimation.MAX_SCALE_FACTOR).setDuration(SwipeListViewTouchListener.this.mLongAnimationTime).setListener(new C06261());
                                }
                            });
                        } else {
                            viewFlipper.animate().translationX((float) this.mViewWidth).alpha(0.0f).setDuration(0).setListener(new AnimatorListenerAdapter() {

                                class C06281 extends AnimatorListenerAdapter {
                                    C06281() {
                                    }

                                    public void onAnimationStart(Animator animation) {
                                        viewFlipper.showNext();
                                    }
                                }

                                public void onAnimationEnd(Animator animation) {
                                    viewFlipper.animate().translationX(0.0f).alpha(ScaleAnimation.MAX_SCALE_FACTOR).setDuration(SwipeListViewTouchListener.this.mLongAnimationTime).setListener(new C06281());
                                }
                            });
                        }
                    } else {
                        this.mDownView.animate().translationX(0.0f).alpha(ScaleAnimation.MAX_SCALE_FACTOR).setDuration(this.mShortAnimationTime).setListener(null);
                    }
                    this.mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                    this.mDownX = 0.0f;
                    this.mDownY = 0.0f;
                    this.mDownView = null;
                    this.mDownPosition = -1;
                    this.mSwiping = false;
                    break;
                }
                break;
            case 2:
                if (!(this.mVelocityTracker == null || this.mPaused)) {
                    this.mVelocityTracker.addMovement(motionEvent);
                    deltaX = motionEvent.getRawX() - this.mDownX;
                    float deltaY = motionEvent.getRawY() - this.mDownY;
                    if (Math.abs(deltaX) > ((float) this.mSlop) && Math.abs(deltaY) < Math.abs(deltaX) / 2.0f) {
                        int i2;
                        this.mSwiping = true;
                        if (deltaX > 0.0f) {
                            i2 = this.mSlop;
                        } else {
                            i2 = -this.mSlop;
                        }
                        this.mSwipingSlop = i2;
                        this.mListView.requestDisallowInterceptTouchEvent(true);
                        MotionEvent cancelEvent = MotionEvent.obtain(motionEvent);
                        cancelEvent.setAction((motionEvent.getActionIndex() << 8) | 3);
                        this.mListView.onTouchEvent(cancelEvent);
                        cancelEvent.recycle();
                    }
                    if (this.mSwiping) {
                        this.mDownView.setTranslationX(deltaX - ((float) this.mSwipingSlop));
                        this.mDownView.setAlpha(Math.max(0.0f, Math.min(ScaleAnimation.MAX_SCALE_FACTOR, ScaleAnimation.MAX_SCALE_FACTOR - ((2.0f * Math.abs(deltaX)) / ((float) this.mViewWidth)))));
                        return true;
                    }
                }
                break;
            case 3:
                if (this.mVelocityTracker != null) {
                    if (this.mDownView != null && this.mSwiping) {
                        this.mDownView.animate().translationX(0.0f).alpha(ScaleAnimation.MAX_SCALE_FACTOR).setDuration(this.mShortAnimationTime).setListener(null);
                    }
                    this.mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                    this.mDownX = 0.0f;
                    this.mDownY = 0.0f;
                    this.mDownView = null;
                    this.mDownPosition = -1;
                    this.mSwiping = false;
                    break;
                }
                break;
        }
        return false;
    }
}
