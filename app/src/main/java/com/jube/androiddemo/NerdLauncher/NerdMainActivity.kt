package com.jube.androiddemo.NerdLauncher

import android.content.Intent
import android.content.pm.ResolveInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jube.androiddemo.R

class NerdMainActivity : AppCompatActivity() {
    companion object{
        const val TAG = "NerdMainActivity_Msg"
    }

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var activities:MutableList<ResolveInfo>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nerd_main)
        mRecyclerView = findViewById(R.id.nerd_recycleview)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        setAdapter()
    }
    private fun setAdapter(){
        val startupIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        activities = packageManager.queryIntentActivities(startupIntent,0)

        activities.sortWith(Comparator{a,b ->
            String.CASE_INSENSITIVE_ORDER.compare(
                a.loadLabel(packageManager).toString(),
                b.loadLabel(packageManager).toString()
            )
        })
        mRecyclerView.adapter = NerdActivityAdapter(activities)
        Log.i(TAG,"Found ${activities.size} activities")
    }

    private class NerdActivityViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private val nameTextView = itemView as TextView
        private lateinit var resolveInfo:ResolveInfo

        init {
            nameTextView.setOnClickListener(this)
        }

        fun bindActivity(resolveInfo: ResolveInfo){
            this.resolveInfo = resolveInfo
            val packageManager = itemView.context.packageManager
            val appName = resolveInfo.loadLabel(packageManager).toString()
            nameTextView.text = appName
        }

        override fun onClick(v: View?) {
            val activityInfo = resolveInfo.activityInfo

            /**
             * 使用隐式intent启动activity并使其位于一个新的任务栈中
             */
            val intent = Intent(Intent.ACTION_MAIN).apply {
                setClassName(activityInfo.applicationInfo.packageName,activityInfo.name)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val context = v?.context
            context?.startActivity(intent)
        }
    }

    private class NerdActivityAdapter(val activities:List<ResolveInfo>):RecyclerView.Adapter<NerdActivityViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NerdActivityViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(android.R.layout.simple_list_item_1,parent,false)
            return NerdActivityViewHolder(view)
        }

        override fun onBindViewHolder(holder: NerdActivityViewHolder, position: Int) {
            val resolveInfo = activities[position]
            holder.bindActivity(resolveInfo)
        }

        override fun getItemCount(): Int {
            return activities.size
        }
    }
}