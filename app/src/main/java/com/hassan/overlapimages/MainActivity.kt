package com.hassan.overlapimages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hassan.overlapimages.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupOverlapImageView()
    }

    private fun setupOverlapImageView() {
        val images = mutableListOf<ResourceType>()
        images.add(UrlImageContent("https://www.gstatic.com/webp/gallery/1.jpg"))
        images.add(UrlImageContent("https://www.gstatic.com/webp/gallery/1.jpg"))
        images.add(UrlImageContent("https://www.gstatic.com/webp/gallery/1.jpg"))

        val drawableImages = mutableListOf<ResourceType>()
        drawableImages.add(DrawableImageContent(R.drawable.first))
        drawableImages.add(DrawableImageContent(R.drawable.first))
        drawableImages.add(DrawableImageContent(R.drawable.first))

        binding.overlapImagesView.imageList = images
        binding.overlapImagesView.circleCount = images.size

        binding.overlapImagesViewWithDrawable.imageList = drawableImages
        binding.overlapImagesViewWithDrawable.circleCount = drawableImages.size
    }
}