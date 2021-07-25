package com.example.androiddemo.demo.viewmodel

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.example.androiddemo.R

class QuizActivity : AppCompatActivity(), ViewModelStoreOwner {

    private var mButton1:Button? = null
    private var mButton2:Button? = null
    private var mButton3:Button? = null
    private var mButton4:Button? = null
    var mButton5:Button? = null

    private val mQuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        /**
         * ViewModelProviders类（留意“Providers”的复数形式）提供了ViewModelProvider类的实例。调用ViewModelProvider(this)的作用是创建并返回一个关联了QuizActivity的ViewModelProvider实例。
         **/
        Log.d(QuizViewModel.TAG,"Got a QuizViewModel :$mQuizViewModel")
         mButton1 = findViewById<Button>(R.id.true_btn1)
         mButton2 = findViewById<Button>(R.id.true_btn2)
         mButton3 = findViewById<Button>(R.id.true_btn3)
         mButton4 = findViewById<Button>(R.id.true_btn4)
         mButton5 = findViewById<Button>(R.id.true_btn5)

        mButton1?.setOnClickListener {
            mQuizViewModel.changeButtonName(0,"按钮/Button1")
            updateButtonName()
        }
        mButton2?.setOnClickListener {
            mQuizViewModel.changeButtonName(1,"按钮/Button2")
            updateButtonName()
        }
        mButton3?.setOnClickListener {
            mQuizViewModel.changeButtonName(2,"按钮/Button3")
            updateButtonName()
        }
        mButton4?.setOnClickListener {
            mQuizViewModel.changeButtonName(3,"按钮/Button4")
            updateButtonName()
        }
        mButton5?.setOnClickListener {
            mQuizViewModel.changeButtonName(4,"按钮/Button5")
            updateButtonName()
        }
    }

    fun updateButtonName(){
        mButton1?.text = mQuizViewModel.mButtonNames[0]
        mButton2?.text = mQuizViewModel.mButtonNames[1]
        mButton3?.text = mQuizViewModel.mButtonNames[2]
        mButton4?.text = mQuizViewModel.mButtonNames[3]
        mButton5?.text = mQuizViewModel.mButtonNames[4]
    }
}