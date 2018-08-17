package lib.kalu.bubble;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * description: 气泡
 * created by kalu on 2018/7/2 1:57
 */
public final class BubbleLinearLayout extends LinearLayout {

    private final static Paint mPaint = new Paint();
    private final Path mPath = new Path();
    private final RectF mRectF = new RectF();

    private int strokeColorNormal = Color.BLACK;
    private int strokeColorSelect = Color.BLACK;

    private float radiusSize = 5f * getResources().getDisplayMetrics().density;
    private float shadowRadiusSize = 10f * getResources().getDisplayMetrics().density;

    private float strokeSize = 1.5f * getResources().getDisplayMetrics().density;
    private float arrowMargin = 0f;
    private float arrowWidth = 8f * getResources().getDisplayMetrics().density;
    private float arrowHeight = 6f * getResources().getDisplayMetrics().density;

    private boolean isArrowRight = false, isArrowLeft = false;

    public BubbleLinearLayout(Context context) {
        this(context, null, 0);
    }

    public BubbleLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typed = null;
        try {
            typed = context.obtainStyledAttributes(attrs, R.styleable.BubbleLinearLayout);
            radiusSize = typed.getDimension(R.styleable.BubbleLinearLayout_bll_radius_size, radiusSize);
            shadowRadiusSize = typed.getDimension(R.styleable.BubbleLinearLayout_bll_shadow_radius_size, shadowRadiusSize);

            strokeSize = typed.getDimension(R.styleable.BubbleLinearLayout_bll_stroke_size, strokeSize);
            arrowWidth = typed.getDimension(R.styleable.BubbleLinearLayout_bll_arrow_width, arrowWidth);
            arrowHeight = typed.getDimension(R.styleable.BubbleLinearLayout_bll_arrow_height, arrowHeight);
            arrowMargin = typed.getDimension(R.styleable.BubbleLinearLayout_bll_arrow_margin_top, arrowMargin);

            strokeColorNormal = typed.getColor(R.styleable.BubbleLinearLayout_bll_stroke_color_normal, strokeColorNormal);
            strokeColorSelect = typed.getColor(R.styleable.BubbleLinearLayout_bll_stroke_color_press, strokeColorSelect);

            isArrowRight = typed.getBoolean(R.styleable.BubbleLinearLayout_bll_arrow_right, isArrowRight);
            isArrowLeft = typed.getBoolean(R.styleable.BubbleLinearLayout_bll_arrow_left, isArrowLeft);

        } catch (Exception e) {
            Log.e("", e.getMessage(), e);
        } finally {
            if (null != typed) {
                typed.recycle();
            }
        }

        if (isArrowRight) {
            setPadding(getPaddingLeft(), getPaddingTop(), (int) (getPaddingRight() + arrowWidth), getPaddingBottom());
        } else if (isArrowLeft) {
            setPadding((int) (getPaddingLeft() + arrowWidth), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        if (isArrowRight) {
            final float width = getWidth();
            final float height = getHeight();
            drawRight(canvas, width, height);
        } else if (isArrowLeft) {
            final float width = getWidth();
            final float height = getHeight();
            drawLeft(canvas, width, height);
        }

        super.dispatchDraw(canvas);
    }

    private boolean isTouch = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouch = true;
                postInvalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isTouch = false;
                postInvalidate();
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    private final void drawRight(Canvas canvas, float width, float height) {
        // step0
        mPath.reset();
        // step1
        mPath.moveTo(radiusSize, 0.5f * strokeSize);
        // step2
        mPath.lineTo(width - radiusSize - arrowWidth, 0.5f * strokeSize);
        // step3
        mRectF.set(width - 2 * radiusSize - arrowWidth, 0.5f * strokeSize, width - 0.5f * strokeSize - arrowWidth, 2 * radiusSize);
        mPath.arcTo(mRectF, 270, 90, false);
        // step4
        mPath.lineTo(width - 0.5f * strokeSize - arrowWidth, arrowMargin);
        // step4
        mPath.lineTo(width - 0.5f * strokeSize, arrowMargin + 0.5f * arrowHeight);
        // step5
        mPath.lineTo(width - 0.5f * strokeSize - arrowWidth, radiusSize + arrowHeight + arrowMargin);
        // step6
        mPath.lineTo(width - 0.5f * strokeSize - arrowWidth, height - radiusSize);
        // step7
        mRectF.set(width - 2 * radiusSize - arrowWidth, height - 2 * radiusSize, width - 0.5f * strokeSize - arrowWidth, height - 0.5f * strokeSize);
        mPath.arcTo(mRectF, 0, 90, false);
        // step8
        mPath.lineTo(radiusSize, height - 0.5f * strokeSize);
        // step9
        mRectF.set(0.5f * strokeSize, height - 2 * radiusSize, 2 * radiusSize, height - 0.5f * strokeSize);
        mPath.arcTo(mRectF, 90, 90, false);
        // step10
        mPath.lineTo(0.5f * strokeSize, radiusSize);
        // step11
        mRectF.set(0.5f * strokeSize, 0.5f * strokeSize, 2 * radiusSize, 2 * radiusSize);
        mPath.arcTo(mRectF, 180, 90, false);
        // step12
        mPath.close();
        // step13
        mPaint.clearShadowLayer();
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setFakeBoldText(true);
        mPaint.setColor(!isTouch ? strokeColorNormal : strokeColorSelect);
        mPaint.setStrokeWidth(strokeSize);
        mPaint.setTextSize(0f);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setMaskFilter(new BlurMaskFilter(shadowRadiusSize, BlurMaskFilter.Blur.INNER));
        canvas.drawPath(mPath, mPaint);
        // step14
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(strokeColorSelect);
        canvas.drawPath(mPath, mPaint);
    }

    private final void drawLeft(Canvas canvas, float width, float height) {
        // step0
        mPath.reset();
        // step1
        mPath.moveTo(radiusSize + arrowWidth, 0.5f * strokeSize);
        // step2
        mPath.lineTo(width - radiusSize, 0.5f * strokeSize);
        // step3
        mRectF.set(width - 2 * radiusSize, 0.5f * strokeSize, width - 0.5f * strokeSize, 2 * radiusSize);
        mPath.arcTo(mRectF, 270, 90, false);
        // step4
        mPath.lineTo(width - 0.5f * strokeSize, height - radiusSize);
        // step5
        mRectF.set(width - 2 * radiusSize, height - 2 * radiusSize, width - 0.5f * strokeSize, height - 0.5f * strokeSize);
        mPath.arcTo(mRectF, 0, 90, false);
        // step6
        mPath.lineTo(radiusSize + arrowWidth, height - 0.5f * strokeSize);
        // step7
        mRectF.set(0.5f * strokeSize + arrowWidth, height - 2 * radiusSize, 2 * radiusSize + arrowWidth, height - 0.5f * strokeSize);
        mPath.arcTo(mRectF, 90, 90, false);
        // step8
        mPath.lineTo(0.5f * strokeSize + arrowWidth, radiusSize + arrowMargin + arrowHeight);
        // step9
        mPath.lineTo(0.5f * strokeSize, radiusSize + arrowMargin + arrowHeight * 0.5f);
        // step10
        mPath.lineTo(0.5f * strokeSize + arrowWidth, radiusSize + arrowMargin);
        // step11
        mPath.lineTo(0.5f * strokeSize + arrowWidth, radiusSize);
        // step12
        mRectF.set(0.5f * strokeSize + arrowWidth, 0.5f * strokeSize, 2 * radiusSize + arrowWidth, 2 * radiusSize);
        mPath.arcTo(mRectF, 180, 90, false);
        // step13
        mPath.close();
        // step14
        mPaint.clearShadowLayer();
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setFakeBoldText(true);
        mPaint.setColor(!isTouch ? strokeColorNormal : strokeColorSelect);
        mPaint.setStrokeWidth(strokeSize);
        mPaint.setTextSize(0f);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setMaskFilter(new BlurMaskFilter(shadowRadiusSize, BlurMaskFilter.Blur.INNER));
        canvas.drawPath(mPath, mPaint);
        // step15
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(strokeColorSelect);
        canvas.drawPath(mPath, mPaint);
    }
}
