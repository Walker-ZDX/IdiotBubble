package com.walker.bubble

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var mLeft : TextView
    lateinit var mRight : TextView
    lateinit var mTop : TextView
    lateinit var mBottom : TextView

    lateinit var mBubble : Bubble

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.mLeft = findViewById(R.id.left)
        this.mRight = findViewById(R.id.right)
        this.mTop = findViewById(R.id.top)
        this.mBottom = findViewById(R.id.bottom)

        bindListener()
    }

    fun bindListener() {
        mLeft.setOnClickListener {
            val content = LayoutInflater.from(this@MainActivity).inflate(R.layout.bubble_test, null)
            mBubble = Bubble.Builder(this@MainActivity)
                .setContentView(content)
                .setBubbleConfig(Color.parseColor("#cc000000"), 10)
                .setShadowConfig(true, 5, Color.BLACK, -2, -2)
                .setArrowConfig(20, 20, 0.5f)
                .setGravity(Gravity.LEFT)
                .build()
            mBubble.show(it, 0)
        }

        mTop.setOnClickListener {
            val content = LayoutInflater.from(this@MainActivity).inflate(R.layout.bubble_test, null)
            mBubble = Bubble.Builder(this@MainActivity)
                .setContentView(content)
                .setBubbleConfig(Color.parseColor("#cc000000"), 20)
                .setShadowConfig(true, 5, Color.BLACK, -1, -1)
                .setArrowConfig(30, 20, 0.5f)
                .setGravity(Gravity.TOP)
                .build()
            mBubble.show(it, 10)
        }

        mRight.setOnClickListener {
            val content = LayoutInflater.from(this@MainActivity).inflate(R.layout.bubble_test, null)
            mBubble = Bubble.Builder(this@MainActivity)
                .setContentView(content)
                .setBubbleConfig(Color.parseColor("#cc000000"), 10)
                .setShadowConfig(false, 2, Color.BLACK, 0, 0)
                .setArrowConfig(20, 20, 0.5f)
                .setGravity(Gravity.RIGHT)
                .build()
            mBubble.show(it, 0)
        }

        mBottom.setOnClickListener {
            val content = LayoutInflater.from(this@MainActivity).inflate(R.layout.bubble_test, null)
            mBubble = Bubble.Builder(this@MainActivity)
                .setContentView(content)
                .setBubbleConfig(Color.parseColor("#cc000000"), 10)
                .setShadowConfig(false, 2, Color.BLACK, -1, -1)
                .setArrowConfig(20, 20, 0.5f)
                .setGravity(Gravity.BOTTOM)
                .build()
            mBubble.show(it, 0)
        }
    }
}
