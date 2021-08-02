package com.example.androiddemo.Criminallntent

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddemo.Criminallntent.CriminalRoom.Crime
import com.example.androiddemo.R
import java.util.*

private const val TAG = "CrimeFragment"
private const val ARG_CRIME_ID = "crime_id"

class CrimeListFragment : Fragment() {

    private lateinit var crimeRecyclerView: RecyclerView
    private var adater:CrimeAdapter? = CrimeAdapter(emptyList())
    private var callbacks: Callbacks? = null
    private lateinit var crime: Crime
    private lateinit var solvedCheckBox: CheckBox
    private val crimeDetailViewModel:CrimeDetailViewModel by lazy {
        ViewModelProvider(this).get(CrimeDetailViewModel::class.java)
    }

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }

    companion object{
        const val TAG = "CrimeListFragment"
        fun newInstance():CrimeListFragment{
            return CrimeListFragment()
        }
    }


    private inner class CrimeViewHolder(view:View) : RecyclerView.ViewHolder(view),View.OnClickListener{
        private lateinit var crime:Crime
        val titleTextView:TextView = itemView.findViewById(R.id.crime_title)
        val dateTextView:TextView = itemView.findViewById(R.id.crime_date)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime:Crime){
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()
        }

        override fun onClick(v: View?) {
            Toast.makeText(context,"${crime.title} pressed!",Toast.LENGTH_SHORT ).show()
            callbacks?.onCrimeSelected(crime.id)
        }
    }

    private inner class CrimeAdapter(var crimes:List<Crime>):
        RecyclerView.Adapter<CrimeViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeViewHolder {
            val view = layoutInflater.inflate(R.layout.list_crime_item,parent,false)
            return CrimeViewHolder(view)
        }

        override fun onBindViewHolder(holder: CrimeViewHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }

        override fun getItemCount() = crimes.size
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
//        crime = Crime()
//        val crimeId:UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID
//        crimeDetailViewModel.loadCrime(crimeId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        crimeRecyclerView = view.findViewById(R.id.crime_recycle_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer {
                crimes-> crimes?.let {
                Log.i(TAG,"Got crimes ${crimes.size}")
                    updateUI(crimes)
                }
            }
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list,menu)
    }

    fun updateUI(crimes: List<Crime>){
        adater = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adater
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.new_crime -> {
                val crime = Crime()
                crimeListViewModel.addCrime(crime)
                callbacks?.onCrimeSelected(crimeId = crime.id)
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    interface Callbacks{
        fun onCrimeSelected(crimeId: UUID)
    }
}
