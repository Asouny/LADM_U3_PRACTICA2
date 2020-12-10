package mx.tecnm.tepic.ladm_u3_practica2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos(
        context: Context?,
        name: String?,
        factory: SQLiteDatabase.CursorFactory?,
        version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE AGENDA(IDAGENDA INTEGER " +
                "PRIMARY KEY AUTOINCREMENT, LUGAR VARCHAR(200), HORA VARCHAR(200), " +

                "FECHA VARCHAR(200), DESCRIPCION VARCHAR(200))")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}