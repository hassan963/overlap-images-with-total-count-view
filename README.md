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
	        implementation 'com.github.hassan963:overlap-images-with-total-count-view:0.1.0'
	}
```