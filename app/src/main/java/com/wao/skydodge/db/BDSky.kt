package com.wao.skydodge.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

/**
 * Created by wanderson on 08/08/19.
 * @noinspection resource
 */
class BDSky(context: Context?) {
    private val db: SQLiteDatabase

    init {
        val aux = Conection(context)
        db = aux.writableDatabase
    }


    fun atualizar(dados: Base) {
        val valores = ContentValues()
        valores.put("nivel", dados.nivel.toString())
        valores.put("pontos", dados.pontos.toString())
        valores.put("luz", dados.luz.toString())
        valores.put("ima", dados.ima.toString())
        valores.put("sufle", dados.sufle.toString())

        db.update("tile", valores, null, null)
    }

    fun atualizarRecursos(moedas: Int, luz: Int, ima: Int, sufle: Int) {
        val valores = ContentValues()
         valores.put("pontos", moedas.toString())
        valores.put("luz", luz.toString())
        valores.put("ima", ima.toString())
        valores.put("sufle", sufle.toString())

        db.update("tile", valores, null, null)
    }

    fun buscar(): Base {
        val cursor = db.rawQuery(
            "SELECT  nivel,pontos,luz,ima,sufle" +
                    " FROM tile ", null
        )
        cursor.moveToNext()
        val p = Base(
            cursor.getString(0).toInt(),
            cursor.getString(1).toLong(),
                    cursor.getString(2).toInt(),
                    cursor.getString(3).toInt(),
                    cursor.getString(4).toInt()

        )


        cursor.close()

        return p
    }
}