package com.oleksii.simplechat.customviews;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.oleksii.simplechat.R;

public class EasyCopyEditText extends androidx.appcompat.widget.AppCompatEditText {

    Drawable mCopyButtonImage;

    public EasyCopyEditText(Context context) {
        super(context);

        init();
    }

    public EasyCopyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public EasyCopyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mCopyButtonImage = getResources().getDrawable(R.drawable.ic_copy_button, null);
        showButton();
        setInputType(InputType.TYPE_NULL);
        setBackground(getResources().getDrawable(R.drawable.easycopy_edittext_background, null));
        setTextColor(getResources().getColor(R.color.colorGray, null));
        setCompoundDrawablePadding(48);
        setTextSize(18f);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float clearButtonStart;
                float clearButtonEnd;
                boolean isButtonClicked = false;

                if (getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
                    clearButtonEnd = mCopyButtonImage
                            .getIntrinsicWidth() + getPaddingStart();

                    if (event.getX() < clearButtonEnd) {
                        isButtonClicked = true;
                    }
                } else {
                    clearButtonStart = (getWidth() - getPaddingEnd()
                            - mCopyButtonImage.getIntrinsicWidth());

                    if (event.getX() > clearButtonStart) {
                        isButtonClicked = true;
                    }
                }

                if (isButtonClicked) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        mCopyButtonImage = getResources().getDrawable(R.drawable.ic_copy_button_dark, null);
                        showButton();
                        if (getText() != null) {
                            selectAll();
                            ClipboardManager clipboardManager = (ClipboardManager)
                                    getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("ShareUrl", getText().toString());
                            clipboardManager.setPrimaryClip(clip);
                        }
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        mCopyButtonImage = getResources().getDrawable(R.drawable.ic_copy_button, null);
                        showButton();
                        return true;
                    }
                }

                return false;
            }
        });
    }

    private void showButton() {
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, mCopyButtonImage, null);
    }
}
