package com.example.androiddemo.BeatBox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddemo.R
import com.example.androiddemo.databinding.ActivityBeatBoxMainBinding
import com.example.androiddemo.databinding.ListItemSoundBinding

/***
 * 该模型使用了jetpack中的数据绑定和MVVM模型
 */
class BeatBoxMainActivity : AppCompatActivity() {

    private lateinit var beatBox:BeatBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        beatBox = BeatBox(assets)

        val binding:ActivityBeatBoxMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_beat_box_main)

        binding.beatboxRecycleView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = SoundAdpter(beatBox.sounds)
        }
    }

    private inner class SoundHolder(private val binding: ListItemSoundBinding):
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.viewModel = SoundViewModel()
        }

        //创建并添加一个视图模型，一般情况下，不需要调用executePendingBindings()函数。然而，在这里，我们是在RecyclerView里更新绑定数据。考虑到RecyclerView刷新视图极快，我们要让布局立即刷新
        fun bind(sound:Sound){
            binding.apply {
                viewModel.apply {
                    viewModel?.sound = sound
                    executePendingBindings()
                }
            }
        }
    }

    private inner class SoundAdpter(private val sounds: List<Sound>):RecyclerView.Adapter<SoundHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
            val binding = DataBindingUtil.inflate<ListItemSoundBinding>(layoutInflater,R.layout.list_item_sound,parent,false)
            return SoundHolder(binding)
        }

        override fun onBindViewHolder(holder: SoundHolder, position: Int) {
            val sound = sounds[position]
            holder.bind(sound)
        }

        override fun getItemCount(): Int {
            return sounds.size
        }
    }
}