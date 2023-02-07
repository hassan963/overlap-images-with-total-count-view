package com.hassan.overlapimages

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlin.math.min
import kotlin.math.roundToInt


class OverlapImagesWithTotalCountView(context: Context, attrs: AttributeSet) :
    View(context, attrs) {

    companion object {
        const val DEFAULT_FILL_COLOR = Color.GRAY
        const val DEFAULT_STROKE_COLOR = Color.WHITE
        const val DEFAULT_STROKE_WIDTH = 2F
        const val DEFAULT_CIRCLE_COUNT = 1

        const val DEFAULT_MIN_CIRCLE_COUNT = 1
        const val DEFAULT_MAX_CIRCLE_COUNT = 50
    }

    var fillColor = DEFAULT_FILL_COLOR
        set(value) {
            field = value
            invalidate()
        }

    var strokeColor = DEFAULT_STROKE_COLOR
        set(value) {
            field = value
            invalidate()
        }

    var strokeWidth = DEFAULT_STROKE_WIDTH
        set(value) {
            field = value
            invalidate()
        }

    var circleCount = DEFAULT_CIRCLE_COUNT
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

    var size: Float? = null
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

    private var bitMapList: ArrayList<Bitmap> = ArrayList()

    var imageList: List<ResourceType>? = null
        set(value) {
            field = value
            loadBitmapList()
            invalidate()
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val circleTransform = CircleTransform()

    private val picasso = Picasso.get()

    init {
        paint.isAntiAlias = true

        if (circleCount > DEFAULT_MAX_CIRCLE_COUNT) {
            circleCount = DEFAULT_MAX_CIRCLE_COUNT
        }

        if (circleCount < DEFAULT_MIN_CIRCLE_COUNT) {
            circleCount = DEFAULT_MIN_CIRCLE_COUNT
        }
        picasso.setIndicatorsEnabled(true)


        setupAttributes(attrs)
    }

    private fun setupAttributes(attrs: AttributeSet) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.OverlapImagesWithTotalCountView,
            0,
            0
        )

        fillColor = typedArray.getColor(
            R.styleable.OverlapImagesWithTotalCountView_fillColor,
            DEFAULT_FILL_COLOR
        )
        strokeColor = typedArray.getColor(
            R.styleable.OverlapImagesWithTotalCountView_strokeColor,
            DEFAULT_STROKE_COLOR
        )
        strokeWidth = typedArray.getDimension(
            R.styleable.OverlapImagesWithTotalCountView_strokeWidth,
            DEFAULT_STROKE_WIDTH
        )
        circleCount = typedArray.getInt(
            R.styleable.OverlapImagesWithTotalCountView_circleCount,
            DEFAULT_CIRCLE_COUNT
        )

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizeIfFollowWidth = calculateCircleSize(measuredWidth.toFloat(), circleCount)
        val sizeIfFollowHeight = measuredHeight.toFloat()
        val chosenSize = min(sizeIfFollowWidth, sizeIfFollowHeight).roundToInt().toFloat()

        if (size == null || size!! > chosenSize) {
            size = chosenSize
        }

        setMeasuredDimension(calculateMaxWidth(size!!, circleCount).roundToInt(), size!!.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        try {
            val radius = size!! / 2F
            var counter = 0

            while (counter < circleCount) {

                paint.color = fillColor
                paint.style = Paint.Style.FILL
                canvas.drawCircle(radius * (counter + 1), radius, radius, paint)


                if (bitMapList.isNotEmpty()) {

                    val bitmap = bitMapList[counter % bitMapList.size]

                    canvas.drawBitmap(
                        bitmap,
                        null,
                        getImageRectF(radius, counter),
                        null
                    )
                }

                if (strokeWidth > size!! / 3) {
                    strokeWidth = size!! / 3
                }

                if (strokeWidth < DEFAULT_STROKE_WIDTH) {
                    strokeWidth = DEFAULT_STROKE_WIDTH
                }

                paint.color = strokeColor
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = strokeWidth

                canvas.drawCircle(radius * (counter + 1), radius, radius - strokeWidth / 4F, paint)

                counter += 1
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadBitmapList() {
        imageList?.forEachIndexed { index, resourceType ->

            when (resourceType) {
                is UrlImageContent -> {
                    picasso.load(
                        resourceType.imageUrl
                    ).transform(circleTransform)
                        .into(generateBitMap(index))
                }
                is DrawableImageContent -> {
                    val drawable = ContextCompat.getDrawable(context, resourceType.resourceId)
                    val name = drawable?.javaClass?.name


                    if (name?.contains("VectorDrawable") == true) {
                        val bitmap = loadDrawable(resourceType.resourceId)
                        bitMapList.add(bitmap)

                        if (bitMapList.size - 1 == index) {
                            requestLayout()
                            invalidate()
                        }

                    } else {
                        picasso.load(
                            resourceType.resourceId
                        ).transform(circleTransform)
                            .into(generateBitMap(index))
                    }

                }
            }
        }
    }

    private fun generateBitMap(index: Int): Target {
        return object : Target {
            override fun onBitmapLoaded(
                bitmap: Bitmap?,
                from: Picasso.LoadedFrom?
            ) {
                if (bitmap != null) {
                    bitMapList.add(bitmap)
                }

                if ((bitMapList.size - 1) == index) {
                    requestLayout()
                    invalidate()
                }
            }

            override fun onBitmapFailed(
                e: java.lang.Exception?,
                errorDrawable: Drawable?
            ) {
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

            }
        }
    }

    private fun loadDrawable(@DrawableRes drawableRes: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableRes);
        drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight);

        val bitmap = Bitmap.createBitmap(
            drawable?.intrinsicWidth!!,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        );
        val canvas = Canvas(bitmap);
        draw(canvas)

        return bitmap
    }


    private fun calculateMaxWidth(circleSize: Float, circleCount: Int): Float {
        return circleSize * circleCount - ((circleCount - 1) * circleSize / 2)
    }

    private fun calculateCircleSize(maxWidth: Float, circleCount: Int): Float {
        return 2 * maxWidth / (circleCount + 1)
    }

    private fun getImageRectF(radius: Float, counter: Int): RectF {
        return RectF(radius * counter, 0F, radius * counter + radius * 2, radius * 2)
    }
}