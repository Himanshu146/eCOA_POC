package com.richpath

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.appcompat.widget.AppCompatImageView
import com.example.svgpath.R
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_UP -> {
                performClick()
            }
        }

        SVGPathDrawable?.getTouchedPath(event)?.let { richPath ->
            richPath.onPathClickListener?.onClick(richPath)
            this.onPathClickListener?.onClick(richPath)
        }
        return true
    }
}