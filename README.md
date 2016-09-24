# Color Preference

A custom preference item for easy implementation of a color picker in the preference screen. You can use the inbuilt picker or any other color picker of your choice.

[![](https://jitpack.io/v/kizitonwose/colorpreference.svg)](https://jitpack.io/#kizitonwose/colorpreference) 
[![Platform](http://img.shields.io/badge/platform-android-brightgreen.svg?style=flat)](http://developer.android.com/index.html) 
[![License](http://img.shields.io/badge/license-apache2.0-lightgrey.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)


## Preview

![ExampleMain][ExampleMain] ![ExampleDefault][ExampleDefault]
![ExampleDefault2][ExampleDefault2]

Get the sample apk [here](https://github.com/kizitonwose/colorpreference/releases/download/1.0.0/sample-1.0.0.apk) 
Also checkout the sample module [here](/sample/)

## Setup

### Gradle

Add this to your project level `build.gradle`:

```groovy
allprojects {
 repositories {
    jcenter()
    maven { url "https://jitpack.io" }
 }
}
```

Add this to your app `build.gradle`:

```groovy
dependencies {
	compile 'com.github.kizitonwose:colorpreference:1.0.0'
}
```

## Usage


Just like every other preference object, you add it to the XML file of your Preference screen.


```xml
<com.kizitonwose.colorpreference.ColorPreference
            android:defaultValue="@color/color_default"
            android:key="color_pref" />

<CheckBoxPreference
            ... />

<SwitchPreference
            ... />

```

The default implementation is the circle color view. For custom settings, add the app namespace to your XML file:

`xmlns:app="http://schemas.android.com/apk/res-auto"`

Now you can use the custom attributes.

```xml
<com.kizitonwose.colorpreference.ColorPreference
            android:defaultValue="@color/color_default"
            android:key="color_pref"
            android:summary="@string/pref_summary"
            android:title="@string/pref_title"
            app:colorShape="square"
            app:colorChoices="@array/color_choices"
            app:viewSize="large"
            app:numColumns="5
            app:showDialog="false" />
```


### Attributes

|attribute name|description|default value|
|:-:|:-:|:-:|
|colorShape|The shape of the color view(`circle` or `square`)| `circle`|
|colorChoices|An array of colors to show on the dialog| An internal array |
|viewSize|The size of the color view(`normal` or `large`) |`large`|
|numColumns|The number of columns for the colors on the dialog| 5 |
|showDialog|If `false`, the user can suppress the in-built dialog and then show a custom color picker. To save the color from the custom picker, just call `setValue(int newColor)`| `true` |


### Custom Picker sample

You can find a working example of the custom picker option in the included [sample module](/sample/com.kizitonwose.colorpickerpreferencesample.MainActivity.java). 
Actually, all you have to do is include the `app:showDialog="false"` in the preference item to suppress the inbuilt picker, then call the `setValue(int newColor)` method of the `ColorPreference` class and pass in the color int from the custom color picker.

The custom picker in the sample uses the [Lobster Color Picker](https://github.com/LarsWerkman/Lobsterpicker) Library. You can use any color picker that is available on Android.


## Credits

Original code belongs to [Roman Nurik](https://github.com/romannurik) of [Google](https://github.com/google), I did some additions like the view size, color shape and the ability to use a custom color picker(more to come). It's also now available as a Gradle dependency for easy usage.

## License

```
Copyright (C) 2016 Roman Nurik
Copyright (C) 2016 Kizito Nwose

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[ExampleMain]: /art/screenshot1.png
[ExampleDefault]: /art/screenshot2.png
[ExampleDefault2]: /art/screenshot3.png

