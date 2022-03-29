package com.hey.report

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gyf.immersionbar.ImmersionBar
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
        reportList = findViewById(R.id.report_list)
        reportList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(getDrawable(R.drawable.item_divider)!!)
        reportList.addItemDecoration(dividerItemDecoration)
        val adapter = ReportAdapter(makeReport())
        reportList.adapter = adapter
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

    private fun makeReport(): List<ReportData> {
        val data = ArrayList<ReportData>()
        data.add(
            ReportData(
                "黄田园",
                "托乐嘉售楼处",
                dateTimeFormat.format(date.minusDays(1).plusHours(2)),
                "阴性"
            )
        )
        data.add(
            ReportData(
                "黄田园",
                "托乐嘉售楼处",
                dateTimeFormat.format(date.minusDays(3).minusMinutes(30)),
                "阴性"
            )
        )
        data.add(
            ReportData(
                "黄田园",
                "托乐嘉售楼处",
                dateTimeFormat.format(date.minusDays(5).minusMinutes(30).plusHours(1)),
                "阴性"
            )
        )
        return data
    }


}