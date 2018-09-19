package cn.sysmaster.fontseekbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * @author wanglibo
 * @date 2018/7/27
 * @describe
 */
class FontSeekBar : View {

    private val mFontHints: Array<String> = arrayOf("小", "中", "大", "超大")
    private lateinit var mThumb: Thumb
    private lateinit var mBar: Bar

    /**
     * 小球半径
     */
    private var mThumbRaidus = 0f
    /**
     * Bar高度
     */
    private var mBarHeight = 0f
    /**
     * 文字边距
     */
    private var mTextPadding = 0f

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        mBarHeight = DensityUtils.dp2px(context, 20f)
        mThumbRaidus = DensityUtils.dp2px(context, 10f)
        mTextPadding = DensityUtils.dp2px(context, 10f)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var height: Float = mBarHeight + mTextPadding + getFontHeight()

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height.toInt())
    }

    /**
     * 获取文字高度
     */
    private fun getFontHeight(): Float {
        var paint = Paint()
        paint.textSize = DensityUtils.sp2px(context, 14f)
        paint.measureText(mFontHints[mFontHints.size - 1])
        return paint.descent() - paint.ascent()
    }

    /**
     * 获取文字宽度
     */
    private fun getFontWidth(): Float {
        var paint = Paint()
        paint.textSize = DensityUtils.sp2px(context, 14f)
        return paint.measureText(mFontHints[mFontHints.size - 1])
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
//        mThumb = Thumb()
        var marginX = Math.max(getFontWidth()/2,mThumbRaidus)
        mBar = Bar(context, mFontHints, w.toFloat(), mBarHeight, h.toFloat(), marginX, mTextPadding)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mBar.draw(canvas)
    }

    class Thumb {
        private var mContext: Context
        private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        private var mX = 0f
        private var mY = 0f
        private var mRadius = 0f

        constructor(context: Context, x: Float, y: Float) {
            mContext = context
            mX = x
            mY = y
        }


        public fun draw(canvas: Canvas) {
            canvas.drawCircle(mX, mY, mRadius, mPaint)
        }
    }

    class Bar {
        private var mContext: Context
        private var mFontHints: Array<String>

        private var barHeight = 0f
        private var width = 0f
        private var mY = 0f
        private var mPaint: Paint = Paint()
        private var mTextPaint: Paint = Paint()
        private var avg = 0f
        private var halfHeight = 0f
        /**
         * 边距，同样也是小球的半径
         */
        private var marginX = 0f
        private var textPadding = 0f

        constructor(context: Context, fontHints: Array<String>, width: Float, barHeight: Float, height: Float, marginX: Float, textPadding: Float) {
            this.mTextPaint.textSize = DensityUtils.sp2px(context, 14f)
            this.mContext = context
            this.mFontHints = fontHints
            this.barHeight = barHeight
            this.width = width
            this.avg = (width - marginX * 2) / (fontHints.size - 1)
            this.halfHeight = barHeight / 2
            this.mY = height - halfHeight
            this.marginX = marginX
            this.textPadding = textPadding
        }

        fun draw(canvas: Canvas) {
            canvas.drawLine(marginX, mY, width - marginX, mY, mPaint)
            for (i in 0 until mFontHints.size) {
                val x = avg * i.toFloat() + marginX
                canvas.drawLine(x, mY + halfHeight, x, mY - halfHeight, mPaint)
                val text = mFontHints[i]
                canvas.drawText(text, x - mTextPaint.measureText(text) / 2f, mY - halfHeight - textPadding, mTextPaint)
            }
        }

    }
}