package com.acool.zhibodianzan;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

/**
 * Author:AcooL
 *
 * 直播点赞效果
 *
 */

public class LoveLayout extends RelativeLayout {
    private Drawable mRed,mBlue,mYellow;  // 定义几张图片
    private Drawable[] mDrawables;        // 图片集合
    private Interpolator[] mInterpolators; // 插补器集合
    private int mDrawableHeight,mDrawableWidth;//点赞图片的宽高
    private int mWidth,mHeight;
    private RelativeLayout.LayoutParams params;
    private Random mRandom=new Random(); // 随机数

    public LoveLayout(Context context) {
        this(context,null);
    }

    public LoveLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initDrawable();
        initInterpolator();
        // 初始化Params
        params=new RelativeLayout.LayoutParams(mDrawableHeight,mDrawableWidth);//mDrawableHeight,mDrawableWidth
        // 父容器居中
        params.addRule(CENTER_HORIZONTAL,TRUE);
        // 父容器的底部
        params.addRule(ALIGN_PARENT_BOTTOM,TRUE);

    }

    /**
     * 初始化插补器
     */
    private void initInterpolator() {
        mInterpolators=new Interpolator[4];
        mInterpolators[0]=new LinearInterpolator();// 线性
        mInterpolators[1] = new AccelerateDecelerateInterpolator();// 先加速后减速
        mInterpolators[2] = new AccelerateInterpolator();// 加速
        mInterpolators[3] = new DecelerateInterpolator();// 减速


    }

    private void initDrawable() {
        mRed=getResources().getDrawable(R.drawable.red);
        mYellow=getResources().getDrawable(R.drawable.yellow);
        mBlue=getResources().getDrawable(R.drawable.blue);

        mDrawables=new Drawable[3];
        mDrawables[0]=mRed;
        mDrawables[1]=mYellow;
        mDrawables[2]=mBlue;

        // 得到图片的实际宽高
        mDrawableWidth=mRed.getIntrinsicWidth();
        mDrawableHeight=mRed.getIntrinsicHeight();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    /** * 添加点赞图片 **/
    public void addLove() {
        final ImageView loveIv = new ImageView(getContext());
        loveIv.setImageDrawable(mDrawables[mRandom.nextInt(mDrawables.length)]);
        loveIv.setLayoutParams(params); addView(loveIv);
        // 最终的属性动画集合
        //
        AnimatorSet finalSet = getAnimatorSet(loveIv);

        finalSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(loveIv);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        finalSet.start();

    }

     private AnimatorSet getAnimatorSet(ImageView loveIv) {
         AnimatorSet enter = new AnimatorSet();
         ObjectAnimator alpha = ObjectAnimator .ofFloat(loveIv, "alpha", 0.3f, 1f);
         ObjectAnimator scaleX = ObjectAnimator.ofFloat(loveIv, "scaleX", 0.2f, 1f);
         ObjectAnimator scaleY = ObjectAnimator.ofFloat(loveIv, "scaleY", 0.2f, 1f);
         AnimatorSet rntr=new AnimatorSet();
         enter.setDuration(10000);
         enter.playTogether(alpha, scaleX, scaleY);
         enter.setTarget(loveIv);
         enter.playSequentially(rntr,getBezierValueAnimator(loveIv));
         enter.start();
         return enter;
     }
    /**
     * 贝塞尔曲线动画(核心，不断的修改ImageView的坐标ponintF(x,y) )
     */
    public ValueAnimator getBezierValueAnimator(final ImageView loveIv){

        PointF pointF2 = getPonitF(2);
        PointF pointF1 = getPonitF(1);
        PointF pointF0 = new PointF((mWidth - mDrawableWidth) / 2, mHeight- mDrawableHeight);
        PointF pointF3 = new PointF(mRandom.nextInt(mWidth), 0);
        BezierEvaluator evaluator = new BezierEvaluator(pointF1, pointF2);
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, pointF0,
                pointF3);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                loveIv.setX(pointF.x);
                loveIv.setY(pointF.y);
                loveIv.setAlpha(1 - animation.getAnimatedFraction() + 0.1f);


            }
        });
        animator.setTarget(loveIv);
        animator.setDuration(3000);
        return animator;
    }

    private PointF getPonitF(int i) {
        return new PointF(mRandom.nextInt(mWidth),mRandom.nextInt(mHeight/2)+(i-1)*(mHeight/2));
    }


}





