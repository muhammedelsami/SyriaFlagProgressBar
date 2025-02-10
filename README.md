# SyriaFlagProgressBar

SyriaFlagProgressBar is a custom progress bar component that represents the Syrian flag. This library is written in Kotlin for use in Android applications.

## Features

- Three different progress bar components:
    - `CircleFlagProgressBar`
    - `StarLineProgressBar`
    - `FlagLineProgressBar`
- Animated progress bars
- Customizable colors and sizes

## Installation

Add the following repository to your root `build.gradle` (or `settings.gradle`):

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```

Then, add the dependency to your project's `build.gradle` file:

```gradle
dependencies {
    implementation 'com.muhammed:syriaflagprogressbar:TAG'
}
```

## Usage

### XML Implementation

Below is an example of how to use SyriaFlagProgressBar components in `activity_main.xml`:

```xml
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#067192"
    tools:context=".MainActivity">

    <com.muhammed.syriaflagprogressbar.CircleFlagProgressBar
        android:id="@+id/circleProgressBar"
        android:layout_width="70dp"
        android:layout_height="70dp"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toTopOf="@id/starLineProgressBar"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"/>

    <com.muhammed.syriaflagprogressbar.StarLineProgressBar
        android:id="@+id/starLineProgressBar"
        android:layout_width="100dp"
        android:layout_height="100dp"
        app:layout_constraintTop_toBottomOf="@+id/circleProgressBar"
        app:layout_constraintBottom_toTopOf="@id/flagLineProgressBar"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"/>

    <com.muhammed.syriaflagprogressbar.FlagLineProgressBar
        android:id="@+id/flagLineProgressBar"
        android:layout_width="150dp"
        android:layout_height="50dp"
        app:layout_constraintTop_toBottomOf="@id/starLineProgressBar"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"/>
</androidx.constraintlayout.widget.ConstraintLayout>
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

