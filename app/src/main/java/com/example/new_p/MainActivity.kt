package com.example.new_p

import android.app.TabActivity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.view.marginLeft
import com.example.new_p.Memo
import com.example.new_p.SqliteHelper
import java.text.SimpleDateFormat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.security.cert.CertPath
import java.util.*

@Suppress("deprecation")
class MainActivity : TabActivity() {

    var currenttime = Calendar.getInstance().time
    var Year = Integer.parseInt(SimpleDateFormat("yyyy", Locale.getDefault()).format(currenttime))
    var Month = Integer.parseInt(SimpleDateFormat("MM", Locale.getDefault()).format(currenttime))
    var Day = Integer.parseInt(SimpleDateFormat("dd", Locale.getDefault()).format(currenttime))
    var Val : Int = 0
    var Content : String = ""

    var DB_NAME = "sqlite.sql"
    var DB_VERSION = 1
    lateinit var Value1 : EditText
    lateinit var Content1 : EditText

    var selectYear : Int = Year
    var selectMonth : Int = Month
    var selectDay : Int = Day

    var selectYear2 : Int = Year
    var selectMonth2 : Int = Month
    var selectDay2 : Int = Day

    fun datasave1(year : Int, month : Int){
        var helper = SqliteHelper(this, DB_NAME, DB_VERSION)
        var inc : Int = 0
        var outc : Int = 0

        var list = helper.selectMemo()

        for(i in 0 until list.size step 1){
            if(year === list[i].year && month === list[i].month){
                if(list[i].value > 0) inc += list[i].value
                else{
                    outc += (-list[i].value)
                }
            }
        }

        if (inc > outc) {
            compare1.text = (inc - outc).toString()
            compare1.setTextColor(Color.BLUE)
        } else {
            compare1.text = (inc - outc).toString()
            compare1.setTextColor(Color.RED)
        }

        incom1.text = inc.toString()
        outcom1.text = outc.toString()
    }

    fun datasave2(year : Int, month : Int, day : Int){
        var helper = SqliteHelper(this, DB_NAME, DB_VERSION)
        var inc : Int = 0
        var outc : Int = 0

        var list = helper.selectMemo()

        for(i in 0 until list.size step 1){
            if(year === list[i].year && month === list[i].month && day === list[i].day){
                if(list[i].value > 0) inc += list[i].value
                else{
                    outc += (-list[i].value)
                }
            }
        }

        if (inc > outc) {
            compare2.text = (inc - outc).toString()
            compare2.setTextColor(Color.BLUE)
        } else {
            compare2.text = (inc - outc).toString()
            compare2.setTextColor(Color.RED)
        }

        incom2.text = inc.toString()
        outcom2.text = outc.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        title = "가계부"

        var helper = SqliteHelper(this, DB_NAME, DB_VERSION)

        var list = helper.selectMemo()

        var array1 = ArrayList<CheckBox>(1000)
        var array2 = ArrayList<Long>(1000)
        var count : Int = 0

        ok.setOnClickListener {

            array1.clear()
            array2.clear()

            list = helper.selectMemo()
            baseLayout.removeAllViews()

            for(i in 0 until list.size step 1) {
                if(list[i].year == selectYear2 && list[i].month == selectMonth2 && list[i].day == selectDay2){

                    var param = LinearLayout(this)
                    param.orientation = LinearLayout.VERTICAL
                    param.id = i

                    var list_Can = TextView(this)
                    list_Can.textSize = 20.0f
                    list_Can.text = list[i].month.toString() + " / " + list[i].day.toString()
                    param.addView(list_Can)

                    var list_Val = TextView(this)
                    list_Val.textSize = 20.0f
                    list_Val.text = list[i].value.toString() + "원"
                    param.addView(list_Val)

                    var list_Con = TextView(this)
                    list_Con.textSize = 15.0f
                    list_Con.text = list[i].content.toString()
                    param.addView(list_Con)

                    var list_Che = CheckBox(this)
                    param.addView(list_Che)
                    list_Che.id = i + 1
                    var list_line = TextView(this)
                    list_line.text = "ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ"
                    param.addView(list_line)

                    array1.add(list_Che)
                    array2.add(list[i].no!!)

                    count++

                    param.setPadding(30, 0, 20, 0)
                    baseLayout.addView(param)
                }
            }
        }

        delete.setOnClickListener {
            for(i in 0 until array1.size step 1) {
                if (array1[i].isChecked) {
                    helper.deleteMemo(array2[i])
                }
            }
            datasave1(selectYear, selectMonth)
            datasave2(selectYear, selectMonth, selectDay)
        }

        all_Che.setOnClickListener {
            for (i in 0 until array1.size step 1) {
                array1[i].isChecked = true
            }
        }

        Value1 = findViewById<EditText>(R.id.Value)
        Content1 = findViewById<EditText>(R.id.content)
        var tabhost = this.tabhost

        var tab1 = tabhost.newTabSpec("calender").setIndicator("달력")
        tab1.setContent(R.id.tab_Calender)
        tabhost.addTab(tab1)

        var tab2 = tabhost.newTabSpec("display").setIndicator("출력")
        tab2.setContent(R.id.tab_Display)
        tabhost.addTab(tab2)

        var tab3 = tabhost.newTabSpec("list").setIndicator("리스트")
        tab3.setContent(R.id.tab_List)
        tabhost.addTab(tab3)

        tabhost.currentTab = 0

        calender.setOnDateChangeListener { calendarView, year, month, day ->
            Year = year
            Month = month + 1
            Day = day
        }

        save.setOnClickListener {
            var s1 = Value1.text.toString()
            var s2 = Content1.text.toString()

            if(s1.isNotEmpty() && s2.isNotEmpty()){
                Val = Integer.parseInt(s1)
                Content = s2
                var memo = Memo(null, Year, Month, Day, Val, Content)
                helper.insertMemo(memo)

                var list = helper.selectMemo()
                var sum = 0

                for(i in 0..list.size - 1 step 1){
                    sum += list[i].value
                }

            } else{
                Toast.makeText(applicationContext, "금액과 내용을 입력하세요", Toast.LENGTH_SHORT).show()
            }

            Toast.makeText(applicationContext, "저장되었습니다.", Toast.LENGTH_SHORT).show()

            datasave1(selectYear, selectMonth)
            datasave2(selectYear, selectMonth, selectDay)
        }

        var YData = resources.getStringArray(R.array.Year)
        var adapter1 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, YData)
        Year_spinner.adapter = adapter1
        Year_spinner.setSelection(Year - 2015)

        var MData = resources.getStringArray(R.array.Month)
        var adapter2 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MData)
        Month_spinner.adapter = adapter2
        Month_spinner.setSelection(Month)

        var DData = resources.getStringArray(R.array.Day)
        var adapter3 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DData)
        Day_spinner.adapter = adapter3
        Day_spinner.setSelection(Day)

        var spi_Y = resources.getStringArray(R.array.Year)
        var adapter4 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spi_Y)
        spi_year.adapter = adapter4
        spi_year.setSelection(Year - 2015)

        var spi_M = resources.getStringArray(R.array.Month)
        var adapter5 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spi_M)
        spi_month.adapter = adapter5
        spi_month.setSelection(Month)

        var spi_d = resources.getStringArray(R.array.Day)
        var adapter6 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spi_d)
        spi_day.adapter = adapter6
        spi_day.setSelection(Day)

        Year_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if(position != 0) {
                    selectYear = Integer.parseInt(YData[position])
                }
                if(selectYear != -1 && selectMonth != -1) {
                    datasave1(selectYear, selectMonth)
                }
                if(selectYear != -1 && selectMonth != -1 && selectDay != -1) {
                    datasave2(selectYear, selectMonth, selectDay)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        Month_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position != 0) {
                    selectMonth = Integer.parseInt(MData[position])
                }
                if(selectYear != -1 && selectMonth != -1) {
                    datasave1(selectYear, selectMonth)
                }
                if(selectYear != -1 && selectMonth != -1 && selectDay != -1) {
                    datasave2(selectYear, selectMonth, selectDay)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        Day_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position != 0) {
                    selectDay = Integer.parseInt(DData[position])
                }
                if(selectYear != -1 && selectMonth != -1) {
                    datasave1(selectYear, selectMonth)
                }
                if(selectYear != -1 && selectMonth != -1 && selectDay != -1) {
                    datasave2(selectYear, selectMonth, selectDay)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        spi_year.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if(position != 0) {
                    selectYear2 = Integer.parseInt(YData[position])
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        spi_month.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position != 0) {
                    selectMonth2 = Integer.parseInt(MData[position])
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        spi_day.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position != 0) {
                    selectDay2 = Integer.parseInt(DData[position])
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }


        var tabdev = tabhost.newTabSpec("develop").setIndicator("…")
        tabdev.setContent(R.id.develop)
        tabhost.addTab(tabdev)

        var pocari : Boolean = false

        people.setOnClickListener {
            var we = findViewById<LinearLayout>(R.id.people_print)

            if (!pocari) {

                we.visibility = View.VISIBLE

                var I = TextView(this)
                I.gravity = Gravity.CENTER
                I.textSize = 20.0f
                I.setTextColor(Color.BLUE)
                I.text = "null"
                we.addView(I)

                var I_group = TextView(this)
                I_group.gravity = Gravity.CENTER
                I_group.textSize = 15.0f
                I_group.text = "소속 : -"
                we.addView(I_group)

                var blink = TextView(this)
                we.addView(blink)

                var You = TextView(this)
                You.gravity = Gravity.CENTER
                You.textSize = 20.0f
                You.setTextColor(Color.BLUE)
                You.text = "null"
                we.addView(You)

                var You_group = TextView(this)
                You_group.gravity = Gravity.CENTER
                You_group.textSize = 15.0f
                You_group.text = "소속 : -"
                we.addView(You_group)

                pocari = true
            }

            else {
                we.removeAllViews()
                we.visibility = View.INVISIBLE
                pocari = false
            }

        }

        moonhui.setOnClickListener {
            Toast.makeText(applicationContext, "이메일 : 1724014@donga.ac.kr" + "\n" + "전화번호 : 010-2335-4641",Toast.LENGTH_LONG).show()
        }
    }

}

