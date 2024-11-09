package com.example.retrofitweatherapi

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2

class MainActivity : BaseActivity() {

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupWindowInsets(R.id.main)
        setupToolbar(R.id.toolbar, false)

        viewPager = findViewById(R.id.viewPager)

        val slides = listOf(
            Slide("Добро пожаловать", "В этом приложении можно узнать погоду", R.drawable.weather),
            Slide("Погода бывает облачной", "Дождь", R.drawable.weather2),
            Slide("Перейти к погоде", "На следующем слайде введите город", R.drawable.weather3)
        )

        viewPager.adapter = SlideAdapter(this, slides)

    }

    fun toWeather() {
        viewPager.visibility = View.GONE
        findViewById<FrameLayout>(R.id.fragmentContainer).visibility = View.VISIBLE

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, WeatherFragment())
            .commit()
    }

}