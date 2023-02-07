# OverlapImageGalleryView

## Usage
### Dependencies
- **Step 1: Add it in your root build.gradle at the end of repositories:**
```bash
allprojects {
	    repositories {
		    ...
		    maven { url 'https://jitpack.io' }
	    }
    }
```
**or**

**If Android studio version is Arctic Fox or higher then add it in your settings.gradle**

```bash
dependencyResolutionManagement {
  		repositories {
       		...
       		maven { url 'https://jitpack.io' }
   		}
   }
``` 
- **Step 2: Add the dependency in your app module build.gradle file**
```bash
dependencies {
		    ...
	        implementation 'com.github.hassan963:overlap-images-with-total-count-view:1.0.0'
	}
```

- **Step 3: Add code in xml**
```bash
        <com.hassan.overlapimages.OverlapImagesWithTotalCountView
            android:id="@+id/overlap_images_view"
            android:layout_width="match_parent"
            android:layout_height="40dp"
            app:fillColor="#000000"
            app:circleCount="3" 
            app:strokeColor="#FFFFFF"
            app:strokeWidth="4dp" />
```

- **Step 4: Initiate view with data**
```bash
        // with image urls
	val images = mutableListOf<ResourceType>()
        images.add(UrlImageContent("https://www.gstatic.com/webp/gallery/1.jpg"))
        images.add(UrlImageContent("https://www.gstatic.com/webp/gallery/1.jpg"))
        images.add(UrlImageContent("https://www.gstatic.com/webp/gallery/1.jpg"))
        
	// with drawables like jpeg, jpg, png file format
        val drawableImages = mutableListOf<ResourceType>()
        drawableImages.add(DrawableImageContent(R.drawable.first))
        drawableImages.add(DrawableImageContent(R.drawable.first))
        drawableImages.add(DrawableImageContent(R.drawable.first))

        binding.overlapImagesView.imageList = images
        binding.overlapImagesView.circleCount = images.size

        binding.overlapImagesViewWithDrawable.imageList = drawableImages
        binding.overlapImagesViewWithDrawable.circleCount = drawableImages.size
```
