package com.wao.skydodge.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by wanderson on 08/08/19.
 */
class Conection(context: Context?) :
    SQLiteOpenHelper(context, nomeDB, null, vers) {
    override fun onOpen(database: SQLiteDatabase) {
        super.onOpen(database)


        database.disableWriteAheadLogging()
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table tile(_id integer primary key autoincrement," +
                    "nivel text ," +
                    "pontos text," +
                    "luz text ," +
                    "ima text ," +
                    "sufle text ); "
        )


        val valores = ContentValues()
        valores.put("nivel", 0)
        valores.put("pontos", 0)
        valores.put("luz", 3)
        valores.put("ima", 3)
        valores.put("sufle", 3)
        db.insert("tile", null, valores)

        db.execSQL(
            "create table nivel(_id integer primary key autoincrement," +
                    "nivel text ," +
                    "pontos text," +
                    "estrelas text); "
        )


    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }


    companion object {
        private const val nomeDB = "WSSKYT1"
        private const val vers = 1
    }
}