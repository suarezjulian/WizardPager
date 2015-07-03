Wizard Pager
=================

![Wizard Pager Screenshot](https://lh3.googleusercontent.com/-_-Sv3J3bdcc/UdeUUDd1TjI/AAAAAAAAEEo/yproJ-EbCJg/w412-h716-no/wizardPager.png)

Wizard Pager is a library that provides an example implementation of a Wizard UI on Android, it's based of Roman Nurik's wizard pager (https://github.com/romannurik/android-wizardpager)

I've updated Roman's code to use the latest support library, it is now structured as a library project, and it's backwards compatible with Android 2.2

Download
============

WizardPager is ready to be used via [jitpack.io](https://jitpack.io/#TechFreak/WizardPager).
Simply add the following code to your root `build.gradle`:

```groovy
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

Now add the gradle dependency in your application's `build.gradle`:

```groovy
dependencies {
    compile 'com.github.TechFreak:WizardPager:{latest_version}'
}
```

Usage
============

There is a sample implementation, so you can see how to add this library to your project. The example uses ActionBarCompat.



Developed By
============

* Tech Freak - <tech.freak.blog@gmail.com>


License
=======

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
