package com.walker.bubble;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * 气泡Window
 */
public class Bubble extends PopupWindow {
    /** 气泡View */
    private BubbleLayout mBubbleView;
    /** 建造器 */
    private Builder builder;

    /**
     * constructor
     *
     * @param builder builder
     */
    public Bubble(Builder builder) {
        this.builder = builder;

        init();
    }

    /**
     * 展示气泡
     *
     * @param parent 目标View
     * @param margin 距离目标的距离
     */
    public void show(View parent, int margin) {
        if (!this.isShowing()) {
            int[] location = new int[2];
            parent.getLocationOnScreen(location);

            switch (builder.gravity) {
                case Gravity.BOTTOM:
                    showAtLocation(parent, Gravity.NO_GRAVITY,
                            location[0] - ((getMeasuredWidth() - parent.getMeasuredWidth()) / 2),
                            location[1] + parent.getHeight() + margin);
                    break;
                case Gravity.TOP:
                    showAtLocation(parent, Gravity.NO_GRAVITY,
                            location[0] - ((getMeasuredWidth() - parent.getMeasuredWidth()) / 2),
                            location[1] - getMeasureHeight() - margin);
                    break;
                case Gravity.RIGHT:
                    showAtLocation(parent, Gravity.NO_GRAVITY,
                            location[0] + parent.getWidth() + margin,
                            location[1] - ((getMeasureHeight() - parent.getHeight()) / 2));
                    break;
                case Gravity.LEFT:
                    showAtLocation(parent, Gravity.NO_GRAVITY,
                            location[0] - getMeasuredWidth() - margin,
                            location[1] - ((getMeasureHeight() - parent.getHeight()) / 2));
                    break;
                default:
                    break;
            }
        } else {
            this.dismiss();
        }
    }

    /**
     * 初始化气泡各个属性配置
     */
    private void init() {
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setFocusable(true);
        setClippingEnabled(false);
        setOutsideTouchable(true);

        ColorDrawable dw = new ColorDrawable(0);
        setBackgroundDrawable(dw);

        initBubble();
    }

    /**
     * 初始化气泡view，并设置箭头、阴影等属性
     */
    private void initBubble() {
        mBubbleView = new BubbleLayout(builder.context);
        mBubbleView.setBackgroundColor(Color.TRANSPARENT);
        mBubbleView.addView(builder.contentView);
        setContentView(mBubbleView);

        mBubbleView.setArrowConfig(getOrientation(builder.gravity), builder.arrowWidth, builder.arrowLength, builder.arrowOffset);
        mBubbleView.setBubbleConfig(builder.bubbleColor, builder.bubbleRadius);
        mBubbleView.setShadowConfig(builder.shadowEnable, builder.shadowRadius, builder.shadowColor, builder.shadowOffsetX, builder.shadowOffsetY);
        mBubbleView.invalidate();
    }

    /**
     * 根据气泡位置获取箭头方向
     *
     * @param gravity Gravity
     * @return 箭头方向
     */
    private BubbleLayout.ArrowOrientation getOrientation(int gravity) {
        switch (gravity) {
            case Gravity.BOTTOM:
                return BubbleLayout.ArrowOrientation.TOP;
            case Gravity.TOP:
                return BubbleLayout.ArrowOrientation.BOTTOM;
            case Gravity.RIGHT:
                return BubbleLayout.ArrowOrientation.LEFT;
            case Gravity.LEFT:
                return BubbleLayout.ArrowOrientation.RIGHT;
            default:
                return BubbleLayout.ArrowOrientation.TOP;
        }
    }

    /**
     * 测量高度
     *
     * @return height
     */
    private int getMeasureHeight() {
        getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popHeight = getContentView().getMeasuredHeight();
        return popHeight;
    }

    /**
     * 测量宽度
     *
     * @return width
     */
    private int getMeasuredWidth() {
        getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popWidth = getContentView().getMeasuredWidth();
        return popWidth;
    }

    /**
     * 气泡建造器
     */
    public static final class Builder {
        /** Context */
        Context context;
        /** 展示内容 */
        View contentView;
        /** 气泡位置 */
        int gravity = Gravity.TOP;
        /** 气泡颜色、圆角半径 */
        int bubbleColor = 0;
        int bubbleRadius = 0;
        /** 气泡剪头偏移、长度、宽度 */
        float arrowOffset = 0;
        int arrowLength = 0;
        int arrowWidth = 0;
        /** 气泡阴影是否展示、圆角半径、颜色、偏移量 */
        boolean shadowEnable = false;
        int shadowRadius = 0;
        int shadowColor = 0;
        int shadowOffsetX = 0;
        int shadowOffsetY = 0;

        /**
         * constructor
         *
         * @param context Activity Context
         */
        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置气泡展示内容
         *
         * @param contentView view
         * @return bubble builder
         */
        public Builder setContentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        /**
         * 设置气泡位置（四个方向）
         *
         * @param gravity
         * @return bubble builder
         */
        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        /**
         * 设置气泡颜色和圆角半径
         *
         * @param color 颜色
         * @param radius 圆角半径，-1表示使用默认值
         * @return bubble builder
         */
        public Builder setBubbleConfig(int color, int radius) {
            this.bubbleColor = color;
            this.bubbleRadius = radius;
            return this;
        }

        /**
         * 设置箭头属性宽度、长度和偏移，
         * 偏移取值范围为0~1，起点为同侧左/上角位置
         *
         * @param width 宽度
         * @param length 长度
         * @param offset 偏移，取值0~1
         * @return bubble builder
         */
        public Builder setArrowConfig(int width, int length, float offset) {
            this.arrowWidth = width;
            this.arrowLength = length;
            this.arrowOffset = offset;
            return this;
        }

        /**
         * 设置阴影属性
         * 启用了阴影属性建议设置margin，以便更好的展示阴影效果
         *
         * @param enable 是否需要阴影
         * @param radius 阴影范围
         * @param color 阴影颜色
         * @param offsetX X轴偏移量
         * @param offsetY Y周偏移量
         * @return bubble builder
         */
        public Builder setShadowConfig(boolean enable, int radius, int color, int offsetX, int offsetY) {
            this.shadowEnable = enable;
            this.shadowRadius = radius;
            this.shadowColor = color;
            this.shadowOffsetX = offsetX;
            this.shadowOffsetY = offsetY;
            return this;
        }

        /**
         * 构建Bubble对象
         *
         * @return Bubble
         */
        public Bubble build() {
            return new Bubble(this);
        }
    }
}

