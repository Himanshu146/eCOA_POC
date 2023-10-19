package com.richpath

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.appcompat.widget.AppCompatImageView
import com.example.svgpath.R
import com.example.svgpath.util.NewZoomableImageView
import com.richpath.pathparser.PathParser
import com.richpath.model.Vector
import org.xmlpull.v1.XmlPullParserException
import com.richpath.util.XmlParser
import java.io.IOException
import kotlin.math.min

class SVGPathView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : AppCompatImageView(
    context!!, attrs, defStyleAttr) {

    constructor(context: Context?, attrs: AttributeSet?): this(context, attrs, 0)

    private lateinit var vector: Vector
    private var SVGPathDrawable: SVGPathDrawable? = null
    var onPathClickListener: SVGPath.OnPathClickListener? = null

    private var mode = NewZoomableImageView.NONE
    private val matrix = Matrix()
    private val last = PointF()
    private val start = PointF()
    private val minScale = 0.5f
    private var maxScale = 4f
    private lateinit var m: FloatArray
    private var redundantXSpace = 0f
    private var redundantYSpace = 0f
    private var saveScale = 1f
    private var right = 0f
    private var bottom = 0f
    private var originalBitmapWidth = 0f
    private var originalBitmapHeight = 0f
    private var mScaleDetector: ScaleGestureDetector? = null


    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            mode = NewZoomableImageView.ZOOM
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scaleFactor = detector.scaleFactor
            val newScale = saveScale * scaleFactor
            if (newScale < maxScale && newScale > minScale) {
                saveScale = newScale
                val width = width.toFloat()
                val height = height.toFloat()
                right = originalBitmapWidth * saveScale - width
                bottom = originalBitmapHeight * saveScale - height
                val scaledBitmapWidth = originalBitmapWidth * saveScale
                val scaledBitmapHeight = originalBitmapHeight * saveScale
                if (scaledBitmapWidth <= width || scaledBitmapHeight <= height) {
                    matrix.postScale(scaleFactor, scaleFactor, width / 2, height / 2)
                } else {
                    matrix.postScale(scaleFactor, scaleFactor, detector.focusX, detector.focusY)
                }
            }
            return true
        }
    }



    init {
        init()
        setupAttributes(attrs)
    }

    private fun init() {
        //Disable hardware
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        // Obtain a typed array of attributes
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.SVGPathView, 0, 0)
        // Extract custom attributes into member variables
        val resID: Int
        mScaleDetector = ScaleGestureDetector(context, ScaleListener())
        m = FloatArray(9)
        imageMatrix = matrix
        scaleType = ScaleType.MATRIX

        try {
            resID = typedArray.getResourceId(R.styleable.SVGPathView_vector, -1)
        } finally { // TypedArray objects are shared and must be recycled.
            typedArray.recycle()
        }

        if (resID != -1) {
            setVectorDrawable(resID)
        }
    }

    /**
     * Set a VectorDrawable resource ID.
     *
     * @param resId the resource ID for VectorDrawableCompat object.
     */
    @SuppressLint("ResourceType")
    fun setVectorDrawable(@DrawableRes resId: Int) {
        val xpp = context.resources.getXml(resId)
        vector = Vector()
        try {
            XmlParser.parseVector(vector, xpp, context)
            SVGPathDrawable = SVGPathDrawable(vector, scaleType)
            setImageDrawable(SVGPathDrawable)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val vector = vector ?: return
        val desiredWidth = vector.width
        val desiredHeight = vector.height

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        //Measure Width
        val width: Int = when (widthMode) {
            MeasureSpec.EXACTLY -> {
                //Must be this size
                widthSize
            }
            MeasureSpec.AT_MOST -> {
                //Can't be bigger than...
                min(desiredWidth.toInt(), widthSize)
            }
            else -> {
                //Be whatever you want
                desiredWidth.toInt()
            }
        }

        //Measure Height
        val height: Int = when (heightMode) {
            MeasureSpec.EXACTLY -> {
                //Must be this size
                heightSize
            }
            MeasureSpec.AT_MOST -> {
                //Can't be bigger than...
                min(desiredHeight.toInt(), heightSize)
            }
            else -> {
                //Be whatever you want
                desiredHeight.toInt()
            }
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height)


//        val bmHeight = bmHeight
//        val bmWidth = bmWidth
//        val width = measuredWidth.toFloat()
//        val height = measuredHeight.toFloat()
//        //Fit to screen.
//        val scale = if (width > height) height / bmHeight else width / bmWidth
//        matrix.setScale(scale, scale)
//        saveScale = 1f
//        originalBitmapWidth = scale * bmWidth
//        originalBitmapHeight = scale * bmHeight
//
//        // Center the image
//        redundantYSpace = height - originalBitmapHeight
//        redundantXSpace = width - originalBitmapWidth
//        matrix.postTranslate(redundantXSpace / 2, redundantYSpace / 2)
//        imageMatrix = matrix
//        //MUST CALL THIS
//       // setMeasuredDimension(originalBitmapWidth, bmHeight)
    }

    fun findAllRichPaths(): Array<SVGPath> {
        return SVGPathDrawable?.findAllRichPaths() ?: arrayOf()
    }

    fun findRichPathByName(name: String): SVGPath? {
        return SVGPathDrawable?.findRichPathByName(name)
    }

    /**
     * find the first [SVGPath] or null if not found
     * <p>
     * This can be in handy if the vector consists of 1 path only
     *
     * @return the [SVGPath] object found or null
     */
    fun findFirstRichPath(): SVGPath? {
        return SVGPathDrawable?.findFirstRichPath()
    }

    /**
     * find [SVGPath] by its index or null if not found
     * <p>
     * Note that the provided index must be the flattened index of the path
     * <p>
     * example:
     * <pre>
     * {@code <vector>
     *     <path> // index = 0
     *     <path> // index = 1
     *     <group>
     *          <path> // index = 2
     *          <group>
     *              <path> // index = 3
     *          </group>
     *      </group>
     *      <path> // index = 4
     *   </vector>}
     * </pre>
     *
     * @param index the flattened index of the path
     * @return the [SVGPath] object found or null
     */
    fun findRichPathByIndex(@IntRange(from = 0) index: Int): SVGPath? {
        return SVGPathDrawable?.findRichPathByIndex(index)
    }

    fun addPath(path: String) {
        SVGPathDrawable?.addPath(PathParser.createPathFromPathData(path))
    }

    fun addPath(path: Path) {
        SVGPathDrawable?.addPath(path)
    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        when(event?.action) {
//            MotionEvent.ACTION_UP -> {
//                performClick()
//            }
//        }
//
//        SVGPathDrawable?.getTouchedPath(event)?.let { richPath ->
//            richPath.onPathClickListener?.onClick(richPath)
//            this.onPathClickListener?.onClick(richPath)
//        }
//        return true
//    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        mScaleDetector!!.onTouchEvent(event)
        matrix.getValues(m)
        val x = m[Matrix.MTRANS_X]
        val y = m[Matrix.MTRANS_Y]
        val curr = PointF(event.x, event.y)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                last[event.x] = event.y
                start.set(last)
                mode = NewZoomableImageView.DRAG
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                last[event.x] = event.y
                start.set(last)
                mode = NewZoomableImageView.ZOOM
            }

            MotionEvent.ACTION_MOVE ->                 //if the mode is ZOOM or
                //if the mode is DRAG and already zoomed
                if (mode == NewZoomableImageView.ZOOM || mode == NewZoomableImageView.DRAG && saveScale > minScale) {
                    var deltaX = curr.x - last.x // x difference
                    var deltaY = curr.y - last.y // y difference
                    val scaleWidth = Math.round(originalBitmapWidth * saveScale)
                        .toFloat() // width after applying current scale
                    val scaleHeight = Math.round(originalBitmapHeight * saveScale)
                        .toFloat() // height after applying current scale
                    var limitX = false
                    var limitY = false

                    //if scaleWidth is smaller than the views width
                    //in other words if the image width fits in the view
                    //limit left and right movement
                    if (scaleWidth < width && scaleHeight < height) {
                        // don't do anything
                        //Toast.makeText(context, "hello", Toast.LENGTH_LONG).show()
                    } else if (scaleWidth < width) {
                        deltaX = 0f
                        limitY = true
                    } else if (scaleHeight < height) {
                        deltaY = 0f
                        limitX = true
                    } else {
                        limitX = true
                        limitY = true
                    }
                    if (limitY) {
                        if (y + deltaY > 0) {
                            deltaY = -y
                        } else if (y + deltaY < -bottom) {
                            deltaY = -(y + bottom)
                        }
                    }
                    if (limitX) {
                        if (x + deltaX > 0) {
                            deltaX = -x
                        } else if (x + deltaX < -right) {
                            deltaX = -(x + right)
                        }
                    }
                    //move the image with the matrix
                    matrix.postTranslate(deltaX, deltaY)
                    //set the last touch location to the current
                    last[curr.x] = curr.y
                }

            MotionEvent.ACTION_UP -> {
                mode = NewZoomableImageView.NONE
                val xDiff = Math.abs(curr.x - start.x).toInt()
                val yDiff = Math.abs(curr.y - start.y).toInt()
                if (xDiff < NewZoomableImageView.CLICK && yDiff < NewZoomableImageView.CLICK) performClick()
            }

            MotionEvent.ACTION_POINTER_UP -> mode = NewZoomableImageView.NONE
        }

        SVGPathDrawable?.getTouchedPath(event)?.let { richPath ->
            richPath.onPathClickListener?.onClick(richPath)
            this.onPathClickListener?.onClick(richPath)
        }
        imageMatrix = matrix
        invalidate()
        return true
    }

    private val bmWidth: Int
        private get() {
            val drawable = drawable
            return drawable?.intrinsicWidth ?: 0
        }
    private val bmHeight: Int
        private get() {
            val drawable = drawable
            return drawable?.intrinsicHeight ?: 0
        }

    companion object {
        const val NONE = 0
        const val DRAG = 1
        const val ZOOM = 2
        const val CLICK = 3
    }
}