package com.example.retrofitweatherapi

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class SlideAdapter(
    fragmentActivity: FragmentActivity,
    private val slides: List<Slide>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = slides.size

    override fun createFragment(position: Int): Fragment {
        val isLastPage = position == slides.lastIndex
        return SlideFragment.newInstance(slides[position], isLastPage)
    }
}