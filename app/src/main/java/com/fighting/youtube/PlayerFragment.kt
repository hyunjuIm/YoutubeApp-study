package com.fighting.youtube

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fighting.youtube.adapter.VideoAdapter
import com.fighting.youtube.databinding.FragmentPlayerBinding
import com.fighting.youtube.dto.VideoDto
import com.fighting.youtube.service.VideoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.abs

class PlayerFragment : Fragment(R.layout.fragment_player) {

    private var binding: FragmentPlayerBinding? = null

    private lateinit var videoAdapter: VideoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentPlayerBinding = FragmentPlayerBinding.bind(view)
        binding = fragmentPlayerBinding

        initMotionLayoutEvent(fragmentPlayerBinding)
        initRecyclerView(fragmentPlayerBinding)

        getVideoList()
    }

    private fun initMotionLayoutEvent(fragmentPlayerBinding: FragmentPlayerBinding) {
        fragmentPlayerBinding.playerMotionLayout.setTransitionListener(object :
            MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                binding?.let {
                    (activity as MainActivity).also { mainActivity ->
                        mainActivity.findViewById<MotionLayout>(R.id.mainMotionLayout).progress =
                            abs(progress)

                    }
                }
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }

        })
    }

    private fun initRecyclerView(fragmentPlayerBinding: FragmentPlayerBinding){

        videoAdapter = VideoAdapter()

        fragmentPlayerBinding.fragmentRecyclerView.apply {
            adapter = videoAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun getVideoList() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(VideoService::class.java).also {
            it.listVideos()
                .enqueue(object : Callback<VideoDto> {
                    override fun onResponse(call: Call<VideoDto>, response: Response<VideoDto>) {
                        if (response.isSuccessful.not()) {
                            return
                        }

                        response.body()?.let { videoDto ->
                            Log.d(ContentValues.TAG, "MainActivity - $it")
                            videoAdapter.submitList(videoDto.videos)
                        }
                    }

                    override fun onFailure(call: Call<VideoDto>, t: Throwable) {

                    }
                })
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}