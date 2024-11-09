package com.example.retrofitweatherapi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class SlideFragment : Fragment() {

    private var title: String? = null
    private var description: String? = null
    private var imageResId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            description = it.getString(ARG_DESCRIPTION)
            imageResId = it.getInt(ARG_IMAGE_RES_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_slide, container, false)
        view.findViewById<TextView>(R.id.titleTV).text = title
        view.findViewById<TextView>(R.id.descriptionTV).text = description
        view.findViewById<ImageView>(R.id.imageView).setImageResource(imageResId)

        val startButton = view.findViewById<Button>(R.id.startBTN)
        startButton.visibility = if (arguments?.getBoolean(ARG_IS_LAST_PAGE) == true) View.VISIBLE else View.GONE

       startButton.setOnClickListener {
            (activity as? MainActivity)?.toWeather()
        }
        return view
    }

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_DESCRIPTION = "description"
        private const val ARG_IMAGE_RES_ID = "imageResId"
        private const val ARG_IS_LAST_PAGE = "isLastPage"

        fun newInstance(slide: Slide, isLastPage: Boolean): SlideFragment {
            val fragment = SlideFragment()
            val args = Bundle().apply {
                putString(ARG_TITLE, slide.title)
                putString(ARG_DESCRIPTION, slide.description)
                putInt(ARG_IMAGE_RES_ID, slide.imageResId)
                putBoolean(ARG_IS_LAST_PAGE, isLastPage)
            }
            fragment.arguments = args
            return fragment
        }
    }
}