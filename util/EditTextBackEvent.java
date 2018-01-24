package com.moocall.moocall.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.AutoCompleteTextView;

public class EditTextBackEvent extends AutoCompleteTextView {
    private EditTextImeBackListener mOnImeBack;

    public interface EditTextImeBackListener {
        void onImeBack(EditTextBackEvent editTextBackEvent, String str);
    }

    public EditTextBackEvent(Context context) {
        super(context);
    }

    public EditTextBackEvent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextBackEvent(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == 4 && event.getAction() == 1 && this.mOnImeBack != null) {
            this.mOnImeBack.onImeBack(this, getText().toString());
        }
        return super.dispatchKeyEvent(event);
    }

    public void setOnEditTextImeBackListener(EditTextImeBackListener listener) {
        this.mOnImeBack = listener;
    }
}
