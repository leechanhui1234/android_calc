package com.example.new_p

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Memo(var no: Long?, var year: Int, var month: Int, var day: Int, var value: Int, var content: String?)

class SqliteHelper(context : Context, name: String, version : Int)
    :SQLiteOpenHelper(context, name, null, version){
    override fun onCreate(db: SQLiteDatabase?) {
        val create = "create table memo (`no` integer primary key, year integer, month integer, day integer, value integer, content text)"
        db?.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) { //파일은 생성되있으나 버전이 다른 경우
        //테이블에 변경사항이 있을 경우
        //SqliteHelper()의 생성자를 호출할 때 기존 데이터베이스와 version을 비교해 더 높으면 호출
    }

    fun insertMemo(memo : Memo){
        val wd = writableDatabase //db에서 꺼냄
        var values = ContentValues() //메모를 입력타입으로 변환
        values.put("year", memo.year)
        values.put("month", memo.month)
        values.put("day", memo.day)
        values.put("value", memo.value)
        values.put("content", memo.content)

        wd.insert("memo", null, values)

        wd.close()
    }

    @SuppressLint("Range")
    fun selectMemo() : MutableList<Memo>{
        val list = mutableListOf<Memo>()

        val select = "select * from memo"
        var rd = readableDatabase
        var cursor = rd.rawQuery(select, null)

        while(cursor.moveToNext()){
            val no = cursor.getLong(cursor.getColumnIndex("no"))
            val year = cursor.getInt(cursor.getColumnIndex("year"))
            val month = cursor.getInt(cursor.getColumnIndex("month"))
            val day = cursor.getInt(cursor.getColumnIndex("day"))
            val value = cursor.getInt(cursor.getColumnIndex("value"))
            val content = cursor.getString(cursor.getColumnIndex("content"))

            //Toast.makeText(applicationContext, no.toString(), Toast.LENGTH_SHORT).show()

            var memo = Memo(no, year, month, day, value, content)
            list.add(memo)
        }

        cursor.close()
        rd.close()

        return list
    }

    //데이터 수정함수
    fun updateMemo(memo:Memo){
        val wd = writableDatabase

        val values = ContentValues()
        values.put("year", memo.year) //column명 값
        values.put("month", memo.month) //column명 값
        values.put("day", memo.day) // column명 값
        values.put("value", memo.value) //column명 값
        values.put("content", memo.content) //column명 값

        wd.update("memo", values, "no = ${memo.no}", null) //테이블명, value, 몇번째커서 수정?
        wd.close()
    }

    //데이터 삭제 함수
    fun deleteMemo(num : Long){
        val delete = "delete from memo where no = ${num}"
        val wd = writableDatabase
        wd.execSQL(delete)
        wd.close()
    }

    fun deleteMemo2(){
        val delete = "delete from memo"
        val wd = writableDatabase
        wd.execSQL(delete)
        wd.close()
    }
}