package com.example.svgpath.util

import android.content.Context
import android.widget.RemoteViews.RemoteView
import androidx.appcompat.widget.AppCompatImageView
import android.graphics.PointF
import kotlin.jvm.JvmOverloads
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.MotionEvent
import com.example.svgpath.R
import java.lang.AssertionError

/**
 * @author nuclearfog
 */
@RemoteView
class NewZoomView : AppCompatImageView {
    // Layout Attributes
    private var max_zoom_in: Float = NewZoomView.Companion.DEF_MAX_ZOOM_IN
    private var max_zoom_out: Float = NewZoomView.Companion.DEF_MAX_ZOOM_OUT
    /**
     * return if image is movable
     *
     * @return if image is movable
     */
    /**
     * set Image movable
     *
     * @param enableMove set image movable
     */
    var isMovable: Boolean = NewZoomView.Companion.DEF_ENABLE_MOVE
    private var scaleType: ScaleType = NewZoomView.Companion.DEF_SCALE_TYPE

    // intern flags
    private val pos = PointF(0.0f, 0.0f)
    private val dist = PointF(0.0f, 0.0f)
    private var moveLock = false

    constructor(context: Context?) : super(context!!) {}

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int = 0) : super(
        context,
        attrs,
        defStyle
    ) {
        scaleType = getScaleType()
        if (attrs != null) {
            val attrArray = context.obtainStyledAttributes(attrs, R.styleable.ZoomView)
            maxZoomIn = attrArray.getFloat(
                R.styleable.ZoomView_max_zoom_in,
                NewZoomView.Companion.DEF_MAX_ZOOM_IN
            )
            maxZoomOut = attrArray.getFloat(
                R.styleable.ZoomView_max_zoom_out,
                NewZoomView.Companion.DEF_MAX_ZOOM_OUT
            )
            isMovable = attrArray.getBoolean(
                R.styleable.ZoomView_enable_move,
                NewZoomView.Companion.DEF_ENABLE_MOVE
            )
            attrArray.recycle()
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (getScaleType() != ScaleType.MATRIX) setScaleType(ScaleType.MATRIX)
        if (event.pointerCount == 1) {
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    pos[event.x] = event.y
                    moveLock = false
                }
                MotionEvent.ACTION_DOWN -> pos[event.x] = event.y
                MotionEvent.ACTION_MOVE -> {
                    if (moveLock || !isMovable) return super.performClick()
                    val posX = event.x - pos.x
                    val posY = event.y - pos.y
                    pos[event.x] = event.y
                    val m = Matrix(imageMatrix)
                    m.postTranslate(posX, posY)
                    apply(m)
                }
            }
        } else if (event.pointerCount == 2) {
            val distX: Float
            val distY: Float
            val scale: Float
            when (event.actionMasked) {
                MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_POINTER_DOWN -> {
                    distX = event.getX(0) - event.getX(1)
                    distY = event.getY(0) - event.getY(1)
                    dist[distX] = distY // Distance vector
                    moveLock = true
                }
                MotionEvent.ACTION_MOVE -> {
                    distX = event.getX(0) - event.getX(1)
                    distY = event.getY(0) - event.getY(1)
                    val current = PointF(distX, distY)
                    scale = current.length() / dist.length()
                    val m = Matrix(imageMatrix)
                    m.postScale(scale, scale, width / 2.0f, height / 2.0f)
                    dist[distX] = distY
                    apply(m)
                }
            }
        }
        return true
    }

    /**
     * Reset Image position/zoom to default
     */
    fun reset() {
        setScaleType(scaleType)
    }
    /**
     * get maximum zoom in
     *
     * @return maximum zoom value
     */
    /**
     * set maximum zoom in
     *
     * @param max_zoom_in maximum zoom value
     */
    var maxZoomIn: Float
        get() = max_zoom_in
        set(max_zoom_in) {
            if (max_zoom_in < 1.0f) throw AssertionError("value should be more 1.0!")
            this.max_zoom_in = max_zoom_in
        }
    /**
     * get maximum zoom in
     *
     * @return maximum zoom value
     */
    /**
     * set maximum zoom in
     *
     * @param max_zoom_out maximum zoom value
     */
    var maxZoomOut: Float
        get() = max_zoom_out
        set(max_zoom_out) {
            if (max_zoom_out > 1.0f) throw AssertionError("value should be less 1.0!")
            this.max_zoom_out = max_zoom_out
        }

    private fun apply(m: Matrix) {
        val d = drawable ?: return
        val `val` = FloatArray(9)
        m.getValues(`val`)
        val scale = (`val`[Matrix.MSCALE_X] + `val`[Matrix.MSCALE_Y]) / 2 // Scale factor
        val width = d.intrinsicWidth * scale // image width
        val height = d.intrinsicHeight * scale // image height
        val leftBorder = `val`[Matrix.MTRANS_X] // distance to left border
        val rightBorder = -(`val`[Matrix.MTRANS_X] + width - getWidth()) // distance to right border
        val bottomBorder = `val`[Matrix.MTRANS_Y] // distance to bottom border
        val topBorder = -(`val`[Matrix.MTRANS_Y] + height - getHeight()) // distance to top border
        if (width > getWidth()) {                       // is image width bigger than screen width?
            if (rightBorder > 0) // is image on the right border?
                m.postTranslate(rightBorder, 0f) // clamp to right border
            else if (leftBorder > 0) m.postTranslate(-leftBorder, 0f) // clamp to left order
        }
        else if (leftBorder < 0 xor rightBorder.toInt()) {  // does image clash with one border?
            if (rightBorder < 0) m.postTranslate(rightBorder, 0f) // clamp to right border
            else m.postTranslate(-leftBorder, 0f) // clamp to left border
        }
        if (height > getHeight()) {                     // is image height bigger than screen height?
            if (bottomBorder > 0) // is image on the bottom border?
                m.postTranslate(0f, -bottomBorder) // clamp to bottom border
            else if (topBorder > 0) // is image on the top border?
                m.postTranslate(0f, topBorder) // clamp to top border
        }
        else if (topBorder < 0 xor bottomBorder.toInt()) {  // does image clash with one border?
            if (bottomBorder < 0) m.postTranslate(0f, -bottomBorder) // clamp to bottom border
            else m.postTranslate(0f, topBorder) // clamp to top border
        }
        if (scale > max_zoom_in) {                      // scale limit exceeded?
            val undoScale = max_zoom_in / scale // undo scale setting
            m.postScale(undoScale, undoScale, getWidth() / 2.0f, getHeight() / 2.0f)
        } else if (scale < max_zoom_out) {              // scale limit exceeded?
            val undoScale = max_zoom_out / scale // undo scale setting
            m.postScale(undoScale, undoScale, getWidth() / 2.0f, getHeight() / 2.0f)
        }
        imageMatrix = m // set Image matrix
    }

    companion object {
        // Default values
        private const val DEF_MAX_ZOOM_IN = 3.0f
        private const val DEF_MAX_ZOOM_OUT = 0.5f
        private const val DEF_ENABLE_MOVE = true
        private val DEF_SCALE_TYPE = ScaleType.FIT_CENTER
    }
}