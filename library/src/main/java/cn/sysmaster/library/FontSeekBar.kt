package cn.sysmaster.library

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

/**
 * @author wanglibo
 * @date 2018/7/31
 * @describe
 */
class FontSeekBar : View {

    private val mFonts = arrayOf("小", "中", "大", "超大")
    /**
     * 默认值
     */
    private val DEFAULT_LINE_COLOR = Color.parseColor("#333333")
    private val DEFAULT_THEME_COLOR = Color.parseColor("#F39800")
    private val DEFAULT_LINE_HEIGHT = dp2px(20)
    private val DEFAULT_TEXT_PADDING = dp2px(10)
    private val DEFAULT_TEXT_SIZE = sp2px(14)


    private var mLinePaint = Paint()
    private var mTextPaint = Paint()
    private var mCirclePaint = Paint()

    /**
     * 圆颜色值
     */
    private var mCircleColor = DEFAULT_THEME_COLOR
    /**
     * 半径
     */
    private var mCircleRadius = DEFAULT_LINE_HEIGHT / 2f
    /**
     * 线条颜色
     */
    private var mLineColor = DEFAULT_LINE_COLOR
    /**
     * 线条高度
     */
    private var mLineHeight = DEFAULT_LINE_HEIGHT
    /**
     * 文字颜色
     */
    private var mTextColor = DEFAULT_THEME_COLOR
    /**
     * 文字与线的间距
     */
    private var mTextPadding = DEFAULT_TEXT_PADDING
    /**
     * 字体大小
     */
    private var mTextSize = DEFAULT_TEXT_SIZE
    /**
     * 字体最大宽度
     */
//    private var mTextMaxWidth = 0f
    /**
     * 字体最大高度
     */
    private var mTextMaxHeight = 0f
    /**
     * 左右边距
     */
    private var mMarginX = 0f
    /**
     * 直线Y坐标
     */
    private var mLineY = 0f
    /**
     * 每条线段宽度
     */
    private var mItemWidth = 0f
    /**
     * 每条竖向线段中间点
     */
    private lateinit var mPoints: ArrayList<PointF>

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(attrs)
        init()
    }

    /**
     * 初始化属性
     */
    private fun initAttrs(attrs: AttributeSet?) {
        var array = context.obtainStyledAttributes(attrs, R.styleable.FontSeekBar)
        (0 until array.indexCount)
                .map { array.getIndex(it) }
                .forEach {
                    when (it) {
                        R.styleable.FontSeekBar_seekbar_circle_color ->
                            mCircleColor = array.getColor(it, DEFAULT_THEME_COLOR)
                        R.styleable.FontSeekBar_seekbar_circle_radius ->
                            mCircleRadius = array.getDimension(it, DEFAULT_LINE_HEIGHT / 2)
                        R.styleable.FontSeekBar_seekbar_line_color ->
                            mLineColor = array.getColor(it, DEFAULT_LINE_COLOR)
                        R.styleable.FontSeekBar_seekbar_text_color ->
                            mTextColor = array.getColor(it, DEFAULT_THEME_COLOR)
                        R.styleable.FontSeekBar_seekbar_text_padding ->
                            mTextPadding = array.getDimension(it, DEFAULT_TEXT_PADDING)
                        R.styleable.FontSeekBar_seekbar_text_size ->
                            mTextSize = array.getDimension(it, DEFAULT_TEXT_SIZE)
                        R.styleable.FontSeekBar_seekbar_line_height ->
                            mTextSize = array.getDimension(it, DEFAULT_LINE_HEIGHT)
                    }
                }
        array.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var height = Math.max(mLineHeight, mCircleRadius * 2) + mTextPadding + mTextMaxHeight
        setMeasuredDimension(widthMeasureSpec, height.toInt())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initSize()
    }


    private fun init() {
        // 设置画笔
        this.mTextPaint.isAntiAlias = true
        this.mTextPaint.color = mTextColor
        this.mTextPaint.textSize = mTextSize

        this.mCirclePaint.isAntiAlias = true
        this.mCirclePaint.color = mCircleColor

        this.mLinePaint.color = mLineColor

        // 设置字体最大高度
        var textWidth = mTextPaint.measureText(mFonts[mFonts.size - 1])
        this.mTextMaxHeight = mTextPaint.descent() - mTextPaint.ascent()
        // 边距是采取文字或圆的一半的最大值
        this.mMarginX = Math.max(textWidth / 2, mCircleRadius)
    }

    private fun initSize() {
        // 直线y坐标
        this.mLineY = height - mCircleRadius
        // 每条线段宽度
        this.mItemWidth = (width - mMarginX * 2) / (mFonts.size - 1)
        // 初始化每个线段的数据点
        mPoints = arrayListOf()
        var point: PointF
        for (i in 0 until mFonts.size) {
            point = PointF(i * mItemWidth + mMarginX, mLineY)
            mPoints.add(point)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawBar(canvas)

    }

    private fun drawBar(canvas: Canvas) {
        // x 在文字中间开始
        var startX = mMarginX
        var startY = mLineY
        var stopX = width - startX
        // 画直线
        canvas.drawLine(startX, startY, stopX, startY, mLinePaint)
        // 画竖向线段
        var halfHeight = mLineHeight / 2
        for (i in 0 until mPoints.size) {
            var point = mPoints[i]
            // 竖向线段
            canvas.drawLine(point.x, point.y + halfHeight, point.x, point.y - halfHeight, mLinePaint)
            // 文字
            var startX = mTextPaint.measureText(mFonts[i]) / 2
            canvas.drawText(mFonts[i], point.x - startX, point.y - halfHeight - mTextPadding, mTextPaint)
//            mTextPaint.textSize = mTextPaint.textSize + sp2px(1)
        }
    }

    fun dp2px(dpVal: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal.toFloat(), context.getResources().getDisplayMetrics()).toInt().toFloat()
    }


    fun sp2px(spVal: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal.toFloat(), context.resources.displayMetrics).toInt().toFloat()
    }
}