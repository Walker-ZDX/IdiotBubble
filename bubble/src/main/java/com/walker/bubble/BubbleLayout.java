package com.walker.bubble;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import com.walker.bubble.view.R;

/**
 * 气泡View
 */
public class BubbleLayout extends RelativeLayout {
    /**
     * 气泡箭头方向
     */
    public enum ArrowOrientation {
        LEFT, TOP, RIGHT, BOTTOM;

        /**
         * 获取xml配置方向
         *
         * @param orientation string
         * @return ArrowOrientation
         */
        public static ArrowOrientation getOrientation(int orientation) {
            switch (orientation) {
                case 0:
                    return ArrowOrientation.LEFT;
                case 1:
                    return ArrowOrientation.TOP;
                case 2:
                    return ArrowOrientation.RIGHT;
                case 3:
                    return BOTTOM;
                default:
                    return BOTTOM;
            }
        }
    }

    /** 气泡颜色、圆角半径、内部padding */
    private int mBubbleColor = 0;
    private int mBubbleRadius = 0;
    private int mBubblePadding = 0;
    /** 箭头宽度、长度 */
    private int mArrowWidth = 0;
    private int mArrowLength = 0;
    /** 箭头相对于矩形邻侧宽度的偏移度（偏移从左、上边角开始）取值区间为0~1 */
    private float mArrowOffset = 0;
    /** 箭头方向 */
    private ArrowOrientation mArrowOrientation = ArrowOrientation.BOTTOM;
    /** 气泡阴影颜色、范围、偏移 */
    private boolean mShadowEnable = false;
    private int mShadowColor = 0;
    private int mShadowRadius = 0;
    private int mShadowOffsetX = 0;
    private int mShadowOffsetY = 0;

    /** 气泡View的宽、高 */
    private int mWidth, mHeight;
    /** 气泡矩形的四边位置 */
    private int mLeft, mTop, mRight, mBottom;

    /** 画笔 */
    private Paint mPaint;
    /** 绘制路径 */
    private Path mPath;

    public BubbleLayout(Context context) {
        this(context, null);
    }

    public BubbleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public void setBubbleConfig(int color, int radius) {
        mBubbleColor = color;
        mBubbleRadius = radius >= 0 ? radius : 0;
        configPadding();
    }

    public void setArrowConfig(ArrowOrientation orientation, int width, int length, float offset) {
        mArrowOrientation = orientation;
        mArrowWidth = width > 0 ? width : 0;
        mArrowLength = length > 0 ? length : 0;
        mArrowOffset = offset > 0 ? offset : 0;
        configPadding();
    }

    public void setShadowConfig(boolean enable, int radius, int color, int offsetX, int offsetY) {
        mShadowEnable = enable;
        mShadowColor = color;
        mShadowRadius = radius;
        mShadowOffsetX = offsetX;
        mShadowOffsetY = offsetY;
        configPadding();
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setWillNotDraw(false);


        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPath = new Path();

        initAttrs(attrs);
        configPadding();
    }

    private void initAttrs(AttributeSet attrs) {
        if (null == attrs) {
            return;
        }

        // 根据属性配置设置相应值
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.bubble);
        try {
            mBubbleColor = a.getColor(R.styleable.bubble_bubbleColor, 0);
            mBubbleRadius = a.getInt(R.styleable.bubble_bubbleRadius, 0);
            mArrowWidth = a.getDimensionPixelSize(R.styleable.bubble_arrowWidth, 0);
            mArrowLength = a.getDimensionPixelSize(R.styleable.bubble_arrowLength, 0);
            mArrowOrientation = ArrowOrientation.getOrientation(a.getInt(R.styleable.bubble_arrowOrientation, 3));
            mShadowColor = a.getColor(R.styleable.bubble_shadowColor, 0);
            mShadowRadius = a.getDimensionPixelSize(R.styleable.bubble_shadowRadius, 0);
            mShadowOffsetX = a.getDimensionPixelSize(R.styleable.bubble_shadowOffsetX, 0);
            mShadowOffsetY = a.getDimensionPixelSize(R.styleable.bubble_shadowOffsetY, 0);
        } finally {
            if (null != a) {
                a.recycle();
            }
        }
    }

    /**
     * 设置气泡View内的padding
     */
    private void configPadding() {
        int shadowOffset = Math.max(Math.abs(mShadowOffsetX), Math.abs(mShadowOffsetY));
        mBubblePadding = shadowOffset + mShadowRadius * 2;
        int p = mBubblePadding;
        switch (mArrowOrientation)
        {
            case BOTTOM:
                setPadding(p, p , p, mArrowLength + p);
                break;
            case TOP:
                setPadding(p, p + mArrowLength, p, p);
                break;
            case LEFT:
                setPadding(p + mArrowLength, p, p, p);
                break;
            case RIGHT:
                setPadding(p, p, p + mArrowLength, p);
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        configAll();
    }


    @Override
    public void invalidate()
    {
        configAll();
        super.invalidate();
    }

    @Override
    public void postInvalidate()
    {
        configAll();
        super.postInvalidate();
    }

    /**
     * 根据各个属性设置画笔和绘制路径
     */
    private void configAll() {
        mPaint.setPathEffect(new CornerPathEffect(mBubbleRadius));
        if (mShadowEnable) {
            mPaint.setShadowLayer(mShadowRadius, mShadowOffsetX, mShadowOffsetY, mShadowColor);
        }

        mLeft = mBubblePadding + (mArrowOrientation == ArrowOrientation.LEFT ? mArrowLength : 0);
        mTop = mBubblePadding + (mArrowOrientation == ArrowOrientation.TOP ? mArrowLength : 0);
        mRight = mWidth - mBubblePadding - (mArrowOrientation == ArrowOrientation.RIGHT ? mArrowLength : 0);
        mBottom = mHeight - mBubblePadding - (mArrowOrientation == ArrowOrientation.BOTTOM ? mArrowLength : 0);
        mPaint.setColor(mBubbleColor);

        // reset lines and curves
        mPath.reset();

        int topOffset = mTop + ((int)((mBottom - mTop) * mArrowOffset) - mArrowWidth / 2);
        int leftOffset = mLeft + ((int)((mRight - mLeft) * mArrowOffset) - mArrowWidth / 2);

        switch (mArrowOrientation)
        {
            case LEFT:
                mPath.moveTo(mLeft, topOffset);
                mPath.rLineTo(-mArrowLength, mArrowWidth / 2);
                mPath.rLineTo(mArrowLength, mArrowWidth / 2);
                mPath.lineTo(mLeft, mBottom);
                mPath.lineTo(mRight, mBottom);
                mPath.lineTo(mRight, mTop);
                mPath.lineTo(mLeft, mTop);
                break;
            case TOP:
                mPath.moveTo(leftOffset, mTop);
                mPath.rLineTo(mArrowWidth / 2, -mArrowLength);
                mPath.rLineTo(mArrowWidth / 2, mArrowLength);
                mPath.lineTo(mRight, mTop);
                mPath.lineTo(mRight, mBottom);
                mPath.lineTo(mLeft, mBottom);
                mPath.lineTo(mLeft, mTop);
                break;
            case RIGHT:
                mPath.moveTo(mRight, topOffset);
                mPath.rLineTo(mArrowLength, mArrowWidth / 2);
                mPath.rLineTo(-mArrowLength, mArrowWidth / 2);
                mPath.lineTo(mRight, mBottom);
                mPath.lineTo(mLeft, mBottom);
                mPath.lineTo(mLeft, mTop);
                mPath.lineTo(mRight, mTop);
                break;
            case BOTTOM:
                mPath.moveTo(leftOffset, mBottom);
                mPath.rLineTo(mArrowWidth / 2, mArrowLength);
                mPath.rLineTo(mArrowWidth / 2, -mArrowLength);
                mPath.lineTo(mRight, mBottom);
                mPath.lineTo(mRight, mTop);
                mPath.lineTo(mLeft, mTop);
                mPath.lineTo(mLeft, mBottom);
                break;
        }

        mPath.close();

    }
}

