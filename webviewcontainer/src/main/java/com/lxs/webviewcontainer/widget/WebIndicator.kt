package com.lxs.webviewcontainer.widget

import android.animation.*
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout

/**
 * @author liuxiaoshuai
 * @date 2019-07-10
 * @desc
 * @email liulingfeng@mistong.com
 */
class WebIndicator : FrameLayout, BaseIndicatorSpec {
    /**
     * 进度条颜色
     */
    private var mColor: Int = Color.parseColor("#1aad19")
    /**
     * 进度条的画笔
     */
    private lateinit var mPaint: Paint
    private var mAnimator: Animator? = null
    /**
     * 控件的宽度
     */
    private var mTargetWidth = 0
    /**
     * 默认匀速动画最大的时长
     */
    private val MAX_UNIFORM_SPEED_DURATION = 8 * 1000
    /**
     * 默认加速后减速动画最大时长
     */
    private val MAX_DECELERATE_SPEED_DURATION = 450
    /**
     * 结束动画时长 ， Fade out 。
     */
    private val DO_END_ANIMATION_DURATION = 600
    /**
     * 当前匀速动画最大的时长
     */
    private var CURRENT_MAX_UNIFORM_SPEED_DURATION = MAX_UNIFORM_SPEED_DURATION
    /**
     * 当前加速后减速动画最大时长
     */
    private var CURRENT_MAX_DECELERATE_SPEED_DURATION = MAX_DECELERATE_SPEED_DURATION
    /**
     * 标志当前进度条的状态
     */
    private var TAG = 0
    val UN_START = 0
    val STARTED = 1
    val FINISH = 2
    private var mTarget = 0f
    private var mCurrentProgress = 0f
    /**
     * 默认的高度
     */
    var WEB_INDICATOR_DEFAULT_HEIGHT = 3

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.color = mColor
        mPaint.isDither = true
        mPaint.strokeCap = Paint.Cap.SQUARE
        mTargetWidth = context.resources.displayMetrics.widthPixels
    }

    fun setColor(color: Int) {
        this.mColor = color
        mPaint.color = color
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val wMode = MeasureSpec.getMode(widthMeasureSpec)
        var w = MeasureSpec.getSize(widthMeasureSpec)
        val hMode = MeasureSpec.getMode(heightMeasureSpec)
        var h = MeasureSpec.getSize(heightMeasureSpec)

        if (wMode == MeasureSpec.AT_MOST) {
            w =
                if (w <= context.resources.displayMetrics.widthPixels) w else context.resources.displayMetrics.widthPixels
        }
        if (hMode == MeasureSpec.AT_MOST) {
            h = WEB_INDICATOR_DEFAULT_HEIGHT
        }
        this.setMeasuredDimension(w, h)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        canvas?.drawRect(0f, 0f, mCurrentProgress / 100 * this.width, height.toFloat(), mPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.mTargetWidth = measuredWidth
        val screenWidth = context.resources.displayMetrics.widthPixels
        if (mTargetWidth >= screenWidth) {
            CURRENT_MAX_DECELERATE_SPEED_DURATION = MAX_DECELERATE_SPEED_DURATION
            CURRENT_MAX_UNIFORM_SPEED_DURATION = MAX_UNIFORM_SPEED_DURATION
        } else {
            //取比值
            val rate = this.mTargetWidth / java.lang.Float.valueOf(screenWidth.toFloat())
            CURRENT_MAX_UNIFORM_SPEED_DURATION = (MAX_UNIFORM_SPEED_DURATION * rate).toInt()
            CURRENT_MAX_DECELERATE_SPEED_DURATION = (MAX_DECELERATE_SPEED_DURATION * rate).toInt()
        }
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        mAnimator?.let {
            if (it.isStarted) {
                it.cancel()
            }
        }
    }

    private fun startAnim(isFinished: Boolean) {
        val v = (if (isFinished) 100 else 95).toFloat()
        mAnimator?.let {
            if (it.isStarted) {
                it.cancel()
            }
        }

        mCurrentProgress = if (mCurrentProgress == 0f) 0.00000001f else mCurrentProgress
        if (!isFinished) {
            val mAnimator = ValueAnimator.ofFloat(mCurrentProgress, v)
            val residue = 1f - mCurrentProgress / 100 - 0.05f
            mAnimator.interpolator = LinearInterpolator()
            mAnimator.duration = (residue * CURRENT_MAX_UNIFORM_SPEED_DURATION).toLong()
            mAnimator.addUpdateListener(mAnimatorUpdateListener)
            mAnimator.start()
            this.mAnimator = mAnimator
        } else {
            var segment95Animator: ValueAnimator? = null
            if (mCurrentProgress < 95f) {
                segment95Animator = ValueAnimator.ofFloat(mCurrentProgress, 95f)
                val residue = 1f - mCurrentProgress / 100f - 0.05f
                segment95Animator!!.interpolator = LinearInterpolator()
                segment95Animator.duration = (residue * CURRENT_MAX_DECELERATE_SPEED_DURATION).toLong()
                segment95Animator.interpolator = DecelerateInterpolator()
                segment95Animator.addUpdateListener(mAnimatorUpdateListener)
            }
            val mObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
            mObjectAnimator.duration = DO_END_ANIMATION_DURATION.toLong()
            val mValueAnimatorEnd = ValueAnimator.ofFloat(95f, 100f)
            mValueAnimatorEnd.duration = DO_END_ANIMATION_DURATION.toLong()
            mValueAnimatorEnd.addUpdateListener(mAnimatorUpdateListener)
            var mAnimatorSet = AnimatorSet()
            mAnimatorSet.playTogether(mObjectAnimator, mValueAnimatorEnd)
            if (segment95Animator != null) {
                val mAnimatorSet1 = AnimatorSet()
                mAnimatorSet1.play(mAnimatorSet).after(segment95Animator)
                mAnimatorSet = mAnimatorSet1
            }
            mAnimatorSet.addListener(mAnimatorListenerAdapter)
            mAnimatorSet.start()
            mAnimator = mAnimatorSet
        }
        TAG = STARTED
        mTarget = v
    }

    private val mAnimatorUpdateListener = ValueAnimator.AnimatorUpdateListener { animation ->
        val t = animation.animatedValue as Float
        this@WebIndicator.mCurrentProgress = t
        this@WebIndicator.invalidate()
    }

    private val mAnimatorListenerAdapter = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            doEnd()
        }
    }

    private fun doEnd() {
        if (TAG == FINISH && mCurrentProgress == 100f) {
            visibility = GONE
            mCurrentProgress = 0f
            this.alpha = 1f
        }
        TAG = UN_START
    }

    override fun show() {
        if (visibility == View.GONE) {
            this.visibility = View.VISIBLE
            mCurrentProgress = 0f
            startAnim(false)
        }
    }

    override fun hide() {
        TAG = FINISH
    }

    override fun reset() {
        mCurrentProgress = 0f
        mAnimator?.let {
            if (it.isStarted) {
                it.cancel()
            }
        }
    }

    override fun setProgress(newProgress: Int) {
        if (visibility == View.GONE) {
            visibility = View.VISIBLE
        }
        if (newProgress < 95f) {
            return
        }
        if (TAG != FINISH) {
            startAnim(true)
        }
    }

    fun offerLayoutParams(): FrameLayout.LayoutParams {
        return FrameLayout.LayoutParams(-1, WEB_INDICATOR_DEFAULT_HEIGHT)
    }
}