package com.example.androiddemo.Criminallntent

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.androiddemo.Criminallntent.CrimeDialog.DatePickFragment
import com.example.androiddemo.Criminallntent.CriminalRoom.Crime
import com.example.androiddemo.R
import java.io.File
import java.sql.BatchUpdateException
import java.util.*

private const val TAG = "CrimeFragment"
private const val ARG_CRIME_ID = "crime_id"
private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0
private const val REQUEST_CONTACT = 1
private const val REQUEST_PHOTO = 2
private const val DATE_FORMAT = "EEE, MMM, dd"

class CrimeFragment : Fragment(),DatePickFragment.Callbacks {
    private lateinit var crime: Crime
    private lateinit var titleFiled: EditText
    private lateinit var dateButton: Button
    private lateinit var slovedCheckBox: CheckBox
    private lateinit var reportButton:Button
    private lateinit var suspectButton: Button
    private lateinit var photoButton: ImageButton
    private lateinit var photoView: ImageView
    private lateinit var photoFile: File
    private var photoUri: Uri? = null

    private val crimeDetailViewModel:CrimeDetailViewModel by lazy {
        ViewModelProvider(this).get(CrimeDetailViewModel::class.java)
    }


    companion object{
        const val TAG = "Criminallntent"
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView")
        val view = inflater.inflate(R.layout.fragment_crime, container, false)
        titleFiled = view.findViewById(R.id.cirme_title)
        dateButton = view.findViewById(R.id.crime_date_btn)
        slovedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox
        reportButton = view.findViewById<Button>(R.id.crime_report) as Button
        suspectButton = view.findViewById(R.id.crime_suspect) as Button
        photoView = view.findViewById(R.id.crime_photo)
        photoButton = view.findViewById(R.id.crime_camera)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated")
        crime = Crime()
        photoFile = crimeDetailViewModel.getPhotoFile(crime)
        photoUri = FileProvider.getUriForFile(requireActivity(),"com.example.androiddemo.Criminallntent.provider",photoFile)
        updateUI()
        crimeDetailViewModel.crimeLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { crime ->
                crime?.let {
                    this.crime = crime
                    photoFile = crimeDetailViewModel.getPhotoFile(crime)
                    photoUri = FileProvider.getUriForFile(requireActivity(),"com.example.androiddemo.Criminallntent.fileprovider",photoFile)
                    updateUI()
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart")
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

        reportButton.setOnClickListener{
            Intent(Intent.ACTION_SEND).apply{
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getCrimeReport())
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
            }.also {
                intent ->
                val chooserIntent = Intent.createChooser(intent, getString(R.string.send_report))
                startActivity(chooserIntent)
            }
        }

        suspectButton.apply {
            val pickContactIntent = Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI)
//            pickContactIntent.addCategory(Intent.CATEGORY_HOME)
            setOnClickListener {
                startActivityForResult(pickContactIntent, REQUEST_CONTACT)
            }
            val packageManager:PackageManager = requireActivity().packageManager
            val resolvedActivity: ResolveInfo? = packageManager.resolveActivity(pickContactIntent,PackageManager.MATCH_DEFAULT_ONLY)
            if(resolvedActivity == null){
                isEnabled = false
            }
        }

        photoButton.apply {
            val packageManager:PackageManager = requireActivity().packageManager
            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolvedActivity:ResolveInfo? = packageManager.resolveActivity(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
            if(resolvedActivity == null) isEnabled = false

            setOnClickListener {
//                if(photoUri == null) photoUri = FileProvider.getUriForFile(requireActivity(),"com.example.androiddemo.Criminallntent.fileprovider",photoFile)
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
                val cameraActivities:List<ResolveInfo> = packageManager.queryIntentActivities(captureImage,PackageManager.MATCH_DEFAULT_ONLY)
                for(cameraActivity in cameraActivities){
                    requireActivity().grantUriPermission(
                        cameraActivity.activityInfo.packageName,
                        photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                }
                startActivityForResult(captureImage, REQUEST_PHOTO)
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
        if(crime.suspect.isNotEmpty()){
            suspectButton.text = crime.suspect
        }
    }

    private fun getCrimeReport():String{
        val slovedString = if(crime.isSloved){
            getString(R.string.crime_report_solved)
        }else{
            getString(R.string.crime_report_unsolved)
        }

        val dateString = DateFormat.format(DATE_FORMAT,crime.date).toString()

        val suspect = if(crime.suspect.isBlank()){
            getString(R.string.crime_report_no_suspect)
        }else{
            getString(R.string.crime_report_suspect, crime.suspect)
        }

        return getString(R.string.crime_report, crime.title, dateString, slovedString, suspect)
    }

    override fun onDateSelected(date: Date) {
        crime.date = date
        updateUI()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when{
            requestCode != Activity.RESULT_OK -> return

            requestCode == REQUEST_CONTACT && data!=null -> {
                val contactUri: Uri? = data.data
                val queryFileds = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
                val cursor = contactUri?.let { requireActivity().contentResolver.query(it, queryFileds, null, null, null) }
                cursor?.use {
                    if(it.count == 0) return
                    it.moveToFirst()
                    val suspect = it.getString(0)
                    crime.suspect = suspect
                    crimeDetailViewModel.saveCrime(crime)
                    suspectButton.text = suspect
                }
            }

            requestCode == REQUEST_PHOTO -> {
                requireActivity().revokeUriPermission(photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                updatePhotoView()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().revokeUriPermission(photoUri,
        Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }

    private fun updatePhotoView(){
        if(photoFile.exists()){
            val bitmap = PictureUtil.getScaledBitmap(photoFile.path,requireActivity())
            photoView.setImageBitmap(bitmap)
        }else{
            photoView.setImageDrawable(null)
        }
    }
}