package com.oleksii.simplechat.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.oleksii.simplechat.R;

public class LogoView extends View {

    private final float TEXT_SIZE_COEFFICIENT = 0.43f;
    private final int[] colors = getResources().getIntArray(R.array.userLogoColors);

    private Paint mCirclePaint;
    private Paint mTextPaint;
    private Drawable mDrawable;
    private String mLogoText = "";

    public LogoView(Context context) {
        super(context);

        init(null);
    }

    public LogoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public LogoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    public LogoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
        mCirclePaint = new Paint();
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        if (set == null)
            return;

        TypedArray arr = getContext().obtainStyledAttributes(set, R.styleable.LogoView);

        if (arr.getText(R.styleable.LogoView_android_text) != null)
            mLogoText = arr.getText(R.styleable.LogoView_android_text).toString();
        if (arr.getDrawable(R.styleable.LogoView_android_drawable) != null)
            mDrawable = arr.getDrawable(R.styleable.LogoView_android_drawable);

        arr.recycle();
    }

    public void setText(String text) {
        mLogoText = text;

        postInvalidate();
    }

    public void setDrawable(int drawableId) {
        mDrawable = ContextCompat.getDrawable(getContext(), drawableId);

        postInvalidate();
    }

    public void setDpHeight(float dpHeight) {
        this.getLayoutParams().height = convertDpToPixelInt(dpHeight, getContext());
    }

    public void setDpWidth(float dpWidth) {
        this.getLayoutParams().width = convertDpToPixelInt(dpWidth, getContext());
    }

    private int convertDpToPixelInt(float dp, Context context) {
        return (int) (dp * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float cx, cy;
        cx = (float) getWidth() / 2;
        cy = (float) getHeight() / 2;

        mTextPaint.setTextSize(getWidth() * TEXT_SIZE_COEFFICIENT);

        if (mDrawable == null && !mLogoText.equals("")) {
            mCirclePaint.setColor(colors[((int) mLogoText.charAt(0)) % 6]);
            canvas.drawCircle(cx, cy, cx, mCirclePaint);

            String editedText = "";
            String[] tmp = mLogoText.split(" ");
            for (String s : tmp) {
                editedText += String.valueOf(s.charAt(0)).toUpperCase();
                if (editedText.length() > 1)
                    break;
            }

            canvas.drawText(editedText, cx,
                    cy - ((mTextPaint.descent() + mTextPaint.ascent()) / 2), mTextPaint);
        } else if (mDrawable != null) {
            mCirclePaint.setColor(colors[3]);
            canvas.drawCircle(cx, cy, cx, mCirclePaint);

            mDrawable.setBounds((int) cx / 4, (int) cy / 4, (int) (cx * 1.77), (int) (cy * 1.77));
            mDrawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorWhite),
                    PorterDuff.Mode.SRC_ATOP);
            mDrawable.draw(canvas);
        }
    }
}
