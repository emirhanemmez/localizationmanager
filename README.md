# localizationmanager
[![](https://jitpack.io/v/emirhanemmez/localizationmanager.svg)](https://jitpack.io/#emirhanemmez/localizationmanager/1.0)

A library that you can use for **generic**, **clickable** and **bold** string localization with **Markdown** parsing.

## Teck Stack
- Kotlin

## How to install ? 
You can add the library to your project using jitpack.io.

Add the code below to your project's settings.gradle file.

```
 allprojects {
        repositories {
            jcenter()
            maven { url "https://jitpack.io" }
        }
   }
```

Add the code below to your app's gradle file.
```
implementation 'com.github.emirhanemmez:localizationmanager:$latest_version'
```

## Usage

Getting string
```
localizationManager.getString(R.string.my_string) // My String
```

Getting string with generic id
```
localizationManager.getStringByGenericId("generic_id_string", R.string::class.java) // My String with generic id
```

Getting bold string as **Spannable**
```
localizationManager.getSpannable(R.string.bold_string) // My **String**
```

Getting generic string (key-value)
```
localizationManager.getGenericString(
                resId = R.string.generic_string_with_multiple_parameter,
                "number" to "0123456789",
                "name" to "Emirhan"
            ) // {number] {name}
```

Getting generic string as **Spannable**
```
localizationManager.getGenericSpannable(
                resId = R.string.generic_string_with_multiple_parameter,
                "number" to "0123456789",
                "name" to "Emirhan"
            ) // {number] **{name}**
```

Getting clickable string as **Spannable**
```
localizationManager.getClickableText(
                resId = R.string.linked_string,
                clickableTextColor = android.R.color.holo_red_dark,
                textView = this,
                "number" to {
                    Toast.makeText(this@MainActivity, "Number text clicked!", Toast.LENGTH_SHORT)
                        .show()
                },
                "name" to {
                    Toast.makeText(this@MainActivity, "Name text clicked!", Toast.LENGTH_SHORT)
                        .show()
                } // Some clickable string: [012345](number) [Emirhan](name)
```

## Sample
[Click here for sample usage](https://github.com/emirhanemmez/localizationmanager/blob/master/app/src/main/java/com/eemmez/localizationmanagersample/MainActivity.kt)
