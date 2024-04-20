package info.camposha.kotlinsqlite

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        if (!isTableExists(db, TABLE_NAME)) {
            db.execSQL(
                "CREATE TABLE $TABLE_NAME (ID INTEGER PRIMARY KEY " +
                        "AUTOINCREMENT,VALOR TEXT, TIPO TEXT, FONTE TEXT, DESCRICAO TEXT, diaHORA TEXT)"
            )
        }
    }

    private fun isTableExists(db: SQLiteDatabase, tableName: String): Boolean {
        val cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", arrayOf(tableName))
        val tableExists = cursor.count > 0
        cursor.close()
        return tableExists
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun insertData(valor: String, tipo: String, fonte: String, descricao: String, diaHora: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_2, valor)
        contentValues.put(COL_3, tipo)
        contentValues.put(COL_4, fonte)
        contentValues.put(COL_5, descricao)
        contentValues.put(COL_6, diaHora)
        db.insert(TABLE_NAME, null, contentValues)
    }

    fun updateData(id: String, valor: String, tipo: String, fonte: String, descricao: String, diaHora: String):
            Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_1, id)
        contentValues.put(COL_2, valor)
        contentValues.put(COL_3, tipo)
        contentValues.put(COL_4, fonte)
        contentValues.put(COL_5, descricao)
        contentValues.put(COL_6, diaHora)
        db.update(TABLE_NAME, contentValues, "ID = ?", arrayOf(id))
        return true
    }

    fun deleteData(id : String) : Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME,"ID = ?", arrayOf(id))
    }

    val allData : Cursor
        get() {
            val db = this.writableDatabase
            val res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
            return res
        }

    companion object {
        val DATABASE_NAME = "fin_database.db"
        val TABLE_NAME = "fin_table"
        val COL_1 = "ID"
        val COL_2 = "VALOR"
        val COL_3 = "TIPO"
        val COL_4 = "FONTE"
        val COL_5 = "DESCRICAO"
        val COL_6 = "diaHora"


    }
}