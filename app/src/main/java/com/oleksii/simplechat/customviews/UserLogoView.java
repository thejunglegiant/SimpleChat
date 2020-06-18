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

public class UserLogoView extends View {
    private int mCircleColor;
    private int[] mRandomColors;
    private Paint mCirclePaint;
    private Paint mTextPaint;
    private Bitmap bitmap;
    private String mTextLogo = "";

    public UserLogoView(Context context) {
        super(context);

        init(null);
    }

    public UserLogoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public UserLogoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    public UserLogoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
        mRandomColors = getResources().getIntArray(R.array.userLogoColors);
        mCirclePaint = new Paint();
        mCirclePaint.setColor(mRandomColors[new Random().nextInt(mRandomColors.length)]);
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(100f);

        if (set == null)
            return;

        TypedArray arr = getContext().obtainStyledAttributes(set, R.styleable.UserLogoView);

        mCircleColor = arr.getColor(R.styleable.UserLogoView_circle_color,
                mRandomColors[new Random().nextInt(mRandomColors.length)]);
        if (arr.getText(R.styleable.UserLogoView_android_text) != null)
            mTextLogo = arr.getText(R.styleable.UserLogoView_android_text).toString();

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

        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float cx, cy;

        cx = (float) getWidth() / 2;
        cy = (float) getHeight() / 2;

        if (bitmap == null) {
            canvas.drawCircle(cx, cy, cx, mCirclePaint);
            canvas.drawText(mTextLogo, cx, cy - ((mTextPaint.descent() + mTextPaint.ascent()) / 2), mTextPaint);
        } else {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }
}
