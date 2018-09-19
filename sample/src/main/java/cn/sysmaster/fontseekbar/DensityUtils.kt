package cn.sysmaster.fontseekbar

import android.content.Context
import android.util.TypedValue


/**
 * @author wanglibo
 * @date 2018/7/27
 * @describe
 */
object DensityUtils {

    public fun dp2px(context: Context, dpVal: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics()).toInt().toFloat()
    }


    public fun sp2px(context: Context, spVal: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.resources.displayMetrics).toInt().toFloat()
    }

}