package com.hey.report

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.gyf.immersionbar.ImmersionBar
import com.hey.skin.SkinManager
import com.hey.skin.SkinPreference
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var reportList: RecyclerView

    private val dateTimeFormat =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.getDefault())

    private val date = LocalDateTime.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initStatusBar()
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn_loadSkin).setOnClickListener {
            SkinManager.getInstance().loadSkin(SkinPreference.getInstance().skinPath);
        }
    }

    private fun initStatusBar() {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarColor(R.color.white)
            .autoDarkModeEnable(true)
            .autoStatusBarDarkModeEnable(true, 0.2f)
            .fitsSystemWindows(true)
            .statusBarDarkFont(true)
            .init()
    }


}