package com.oleksii.simplechat.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.oleksii.simplechat.R;

import java.util.Random;

public class LogoView extends View {

    private final float TEXT_SIZE_COEFFICIENT = 0.43f;
    private final int[] colors = getResources().getIntArray(R.array.userLogoColors);

    private Paint mCirclePaint;
    private Paint mTextPaint;
    private Bitmap bitmap;
    private String mTextLogo = "";

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
            mTextLogo = arr.getText(R.styleable.LogoView_android_text).toString();

        arr.recycle();
    }

    public void addProfileImage(Bitmap img) {
        bitmap = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, img.getWidth(), img.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(img.getWidth() / 2, img.getHeight() / 2, img.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(img, rect, rect, paint);

        postInvalidate();
    }

    public void addText(String text) {
        mTextLogo = text;
        mCirclePaint.setColor(colors[((int) text.charAt(0)) % 6]);

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

        if (bitmap == null && !mTextLogo.equals("")) {
            String editedText = "";
            String[] tmp = mTextLogo.split(" ");
            for (String s : tmp) {
                editedText += String.valueOf(s.charAt(0)).toUpperCase();
                if (editedText.length() > 1)
                    break;
            }

            canvas.drawCircle(cx, cy, cx, mCirclePaint);
            canvas.drawText(editedText, cx,
                    cy - ((mTextPaint.descent() + mTextPaint.ascent()) / 2), mTextPaint);
        } else if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }
}
