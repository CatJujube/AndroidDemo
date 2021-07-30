package com.example.androiddemo.Criminallntent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.androiddemo.Criminallntent.CrimeDialog.DatePickFragment
import com.example.androiddemo.Criminallntent.CriminalRoom.Crime
import com.example.androiddemo.R
import java.util.*

private const val TAG = "CrimeFragment"
private const val ARG_CRIME_ID = "crime_id"
private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0

class CrimeFragment : Fragment(),DatePickFragment.Callbacks {
    private lateinit var crime: Crime
    private lateinit var titleFiled: EditText
    private lateinit var dateButton: Button
    private lateinit var slovedCheckBox: CheckBox

    private val crimeDetailViewModel:CrimeDetailViewModel by lazy {
        ViewModelProvider(this).get(CrimeDetailViewModel::class.java)
    }

    companion object{
        fun newInstance(crimeId:UUID):CrimeFragment{
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimeFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime, container, false)
        titleFiled = view.findViewById(R.id.cirme_title)
        dateButton = view.findViewById(R.id.crime_date_btn)
        slovedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeDetailViewModel.crimeLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { crime ->
                crime?.let {
                    this.crime = crime
                    updateUI()
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()
        val titleWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                crime.title = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        titleFiled.addTextChangedListener(titleWatcher)

        slovedCheckBox.apply {
            setOnClickListener {
                setOnCheckedChangeListener { buttonView, isChecked ->
                    crime.isSloved = isChecked
                }
            }
        }
        dateButton.setOnClickListener{
            DatePickFragment.newInstance(crime.date).apply{
                setTargetFragment(this@CrimeFragment, REQUEST_DATE)
                show(this@CrimeFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        crimeDetailViewModel.saveCrime(crime)
    }

    private fun updateUI(){
        titleFiled.setText(crime.title)
        dateButton.text = crime.date.toString()
        slovedCheckBox.apply {
            isChecked = crime.isSloved
            jumpDrawablesToCurrentState()
        }
    }

    override fun onDateSelected(date: Date) {
        crime.date = date
        updateUI()
    }
}