package com.silencedut.dogearedtextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;

/**
 * @author SilenceDut
 * @date 2018/9/17
 * A TextView that provides dog eared effect
 */
public class DogEaredView extends AppCompatTextView {

    private static final String TAG = "DogEaredView";
    private static final int ANGLE_THRESHOLD = 90;
    private static final int DEFAULT_ANGLE = 45;

    private int mTriangleBottomLength;

    /**
     * 弧度值，Math函数角函数中的参数一般是弧度，内部换算得来。
     *  angelRad = angelDeg * Math.PI/180;
     */
    private double mAngelRad;

    /**
     * 外围框的颜色，默认同mFoldTriangleColor
     */
    private int mFrameStokeColor =-1;
    private int mFrameStokeWidth = 0 ;

    /**
     * 折叠角的背景颜色
     */
    private int mFoldTriangleColor = -1 ;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mFramePath = new Path();
    private Path mTrianglePath = new Path();
    private Drawable mBackgroundDrawable;
    private Bitmap mBackgroundBitmap;


    public DogEaredView(Context context) {
        this(context,null);
    }

    public DogEaredView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DogEaredView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        hardwareEnable(LAYER_TYPE_HARDWARE);
        mPaint.setAntiAlias(true);

        //default
        setEllipsize(TextUtils.TruncateAt.END);
        setGravity(Gravity.LEFT);

        if(attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DogEaredView);
            mTriangleBottomLength = typedArray.getDimensionPixelSize(R.styleable.DogEaredView_de_triangle_length,0);
            float triangleAngleDegWithBottom = typedArray.getInteger(R.styleable.DogEaredView_de_angle,DEFAULT_ANGLE);
            conversionAngle(triangleAngleDegWithBottom);
            mFoldTriangleColor = typedArray.getColor(R.styleable.DogEaredView_de_foldColor, Color.RED);
            mFrameStokeColor = typedArray.getColor(R.styleable.DogEaredView_de_stokeColor, mFoldTriangleColor);
            mFrameStokeWidth = typedArray.getDimensionPixelSize(R.styleable.DogEaredView_de_stoke_width, 0);
            typedArray.recycle();
        }
    }


    private void conversionAngle(float angel) {
        if(angel >= ANGLE_THRESHOLD) {
            throw new IllegalArgumentException("angel must less than 90");
        }
        mAngelRad =  angel * Math.PI/180;
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        mBackgroundDrawable = background;
        cacheBackground();
    }


    private void cacheBackground() {
        if(getMeasuredWidth() > 0 && getMeasuredHeight() > 0 && mBackgroundDrawable!=null ) {
            if(mBackgroundDrawable instanceof BitmapDrawable) {
                mBackgroundBitmap = ((BitmapDrawable) mBackgroundDrawable).getBitmap();
            }else {
                mBackgroundBitmap = DogEaredHelper.drawableToBitmap(mBackgroundDrawable, getMeasuredWidth(), getMeasuredHeight());
            }
        }
    }

    public int getTriangleBottomLength() {
        return mTriangleBottomLength;
    }

    public DogEaredView setTriangleBottomLength(int triangleBottomLength) {
        this.mTriangleBottomLength = triangleBottomLength;
        return this;
    }

    public double getAngelRad() {
        return mAngelRad;
    }

    public DogEaredView setAngelRad(float angel) {
        conversionAngle(angel);
        return this;
    }

    public int getFrameStokeColor() {
        return mFrameStokeColor;
    }

    public DogEaredView setFrameStokeColor(int frameStokeColor) {
        this.mFrameStokeColor = frameStokeColor;
        if(mFoldTriangleColor == -1) {
            this.mFoldTriangleColor = mFrameStokeColor;
        }
        return this;
    }

    public DogEaredView setContent(String content){
        if(content!=null) {
            super.setText(Html.fromHtml(content.replace("\n","<br>")));

        }

        return this;

    }

    public int getFrameStokeWidth() {
        return mFrameStokeWidth;
    }

    public DogEaredView setFrameStokeWidth(int frameStokeWidth) {
        this.mFrameStokeWidth = frameStokeWidth;
        return this;
    }

    public int getFoldTriangleColor() {
        return mFoldTriangleColor;
    }

    public DogEaredView setFoodTriangleColor(int foldTriangleColor) {

        this.mFoldTriangleColor = foldTriangleColor;
        if(mFrameStokeColor == -1) {
            this.mFrameStokeColor = foldTriangleColor;
        }
        return this;
    }

    public DogEaredView setMaximalLines(int lines) {
        super.setMaxLines(lines);
        return this;
    }


    public DogEaredView setBackground(@ColorInt int color) {
        super.setBackgroundColor(color);
        return this;
    }

    public DogEaredView setDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
        return this;
    }

    public DogEaredView hardwareEnable(int layType) {
        setLayerType(layType, mPaint);
        return this;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // only create when size real changed
        if(w!=oldw && h != oldh ) {
            cacheBackground();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        mPaint.reset();
        //build frame path
        mFramePath.moveTo(0,0);
        int bottomPointX = width - mTriangleBottomLength;
        int rightPointY = height - (int) (mTriangleBottomLength * Math.tan(mAngelRad));

        if(bottomPointX<0) {
            bottomPointX = 0;
        }

        if(rightPointY < 0) {
            rightPointY = 0;
        }

        mFramePath.lineTo(width,0);
        mFramePath.lineTo(width, rightPointY);
        mFramePath.lineTo(bottomPointX,height);
        mFramePath.lineTo(0,height);
        mFramePath.close();

        mPaint.setColor(mFrameStokeColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mFrameStokeWidth);

        //draw background
        if(mBackgroundBitmap != null) {
            canvas.clipPath(mFramePath);
            canvas.drawBitmap(mBackgroundBitmap, 0, 0, mPaint);
        } else {
            mPaint.setStyle(Paint.Style.FILL);
        }

        //draw frame
        if(mFrameStokeWidth!=0) {
            canvas.drawPath(mFramePath,mPaint);
        }

        //draw Triangle
        mPaint.setStyle(Paint.Style.FILL);
        int trianglePointX = width - (int) (mTriangleBottomLength * (1 - Math.cos(mAngelRad * 2)));
        int trianglePointY = height - (int) (mTriangleBottomLength * Math.sin(mAngelRad * 2));

        mTrianglePath.moveTo(trianglePointX, trianglePointY);
        mTrianglePath.lineTo(width, rightPointY);
        mTrianglePath.lineTo(bottomPointX,height);
        mTrianglePath.close();
        canvas.drawPath(mTrianglePath,mPaint);
        super.onDraw(canvas);
    }


}
