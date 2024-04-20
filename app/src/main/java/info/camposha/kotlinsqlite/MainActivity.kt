package info.camposha.kotlinsqlite

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.deleteBtn
import kotlinx.android.synthetic.main.activity_main.descricaoEditTxt
import kotlinx.android.synthetic.main.activity_main.diaHoraEditTxt
import kotlinx.android.synthetic.main.activity_main.fonteTxt
import kotlinx.android.synthetic.main.activity_main.idTxt
import kotlinx.android.synthetic.main.activity_main.insertBtn
import kotlinx.android.synthetic.main.activity_main.tipoEditTxt
import kotlinx.android.synthetic.main.activity_main.updateBtn
import kotlinx.android.synthetic.main.activity_main.valDespEditTxt
import kotlinx.android.synthetic.main.activity_main.viewBtn
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    internal var dbHelper = DatabaseHelper(this)

    val dataFormatada = mostraDataHoraAtual()
    private fun mostraDataHoraAtual(): String {
        val dataHoraAtual = Calendar.getInstance().time
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:MM:ss", Locale.getDefault())
        return sdf.format(dataHoraAtual)
    }
    //Toast.makeText(this, "Data e hora atual: $dataHoraFormatada", Toast.LENGTH_LONG).show()

    fun showToast(text: String){
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_LONG).show()
    }

    fun showDialog(title : String, Message : String){
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle(title)
        builder.setMessage(Message)
        builder.show()
    }

    fun clearEditTexts(){
        idTxt.setText("")
        valDespEditTxt.setText("")
        tipoEditTxt.setText("")
        fonteTxt.setText("")
        descricaoEditTxt.setText("")
        diaHoraEditTxt.setText("")
    }

    private val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_WRITE_EXTERNAL_STORAGE
            )
        } else {

            backupDatabase()
            showToast("backs")
        }

        // Define diaHoraEditTxt com dataFormatada
        diaHoraEditTxt.setText(mostraDataHoraAtual())

        handleInserts()
        handleUpdates()
        handleDeletes()
        handleViewing()
    }

    private fun backupDatabase(): Boolean {
        return try {
            val currentDBPath = getDatabasePath("fin_database.db").absolutePath
            val backupDBPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + File.separator + "fin_bkp.db"
            //val backupDBPath = Environment.getExternalStorageDirectory().path + File.separator + "fin_bkp.db"
            val currentDB = File(currentDBPath)
            val backupDB = File(backupDBPath)
            val inputStream = FileInputStream(currentDB)
            val outputStream = FileOutputStream(backupDB)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun handleInserts() {
        insertBtn.setOnClickListener {
            if (valDespEditTxt.text.toString().isEmpty()) {
                showToast("Por favor, insira um valor")
                valDespEditTxt.requestFocus()
                return@setOnClickListener
            }

            try {
                dbHelper.insertData(
                    valDespEditTxt.text.toString(),
                    tipoEditTxt.text.toString(),
                    fonteTxt.text.toString(),
                    descricaoEditTxt.text.toString(),
                    diaHoraEditTxt.text.toString()
                )
                showToast("Registros inserido: "+dataFormatada)
                //showToast("Registros inserido: "+valDespEditTxt.text.toString())
                clearEditTexts()
                valDespEditTxt.requestFocus()
                diaHoraEditTxt.setText(mostraDataHoraAtual())
            }catch (e: Exception){
                e.printStackTrace()
                //showToast(valDespEditTxt.text.toString())
                //showToast(e.message.toString()) //ORIG não estava chamando, subi
            }
        }
    }

    fun handleUpdates() {
        updateBtn.setOnClickListener {
            try {
                val isUpdate = dbHelper.updateData(
                    idTxt.text.toString(),
                    valDespEditTxt.text.toString(),
                    tipoEditTxt.text.toString(),
                    fonteTxt.text.toString(),
                    descricaoEditTxt.text.toString(),
                    diaHoraEditTxt.text.toString()
                )
                if (isUpdate == true)
                    Toast.makeText(this, "Data Updated Successfully:" + mostraDataHoraAtual(), Toast.LENGTH_LONG).show()
                //  showToast("Data Updated Successfully: $dataHoraFormatada")
                //  showToast("Data Updated Successfully")
                else
                    showToast("Data Not Updated")
            }catch (e: Exception){
                e.printStackTrace()
                showToast(e.message.toString())
            }
        }
    }

    fun handleDeletes(){
        deleteBtn.setOnClickListener {
            try {
                dbHelper.deleteData(idTxt.text.toString())
                Toast.makeText(this, "Data Updated Successfully:" + mostraDataHoraAtual(), Toast.LENGTH_LONG).show()
                clearEditTexts()
            }catch (e: Exception){
                e.printStackTrace()
                showToast(e.message.toString())
            }
        }
    }

    fun handleViewing() {
        viewBtn.setOnClickListener(
            View.OnClickListener {
                val res = dbHelper.allData
                if (res.count == 0) {
                    showDialog("Error", "No Data Found")
                    return@OnClickListener
                }

                val buffer = StringBuffer()
                while (res.moveToNext()) {
                    // buffer.append("ID :" + res.getString(0) + "\n")
                    buffer.append(res.getString(0) + "\n")
                    buffer.append(res.getString(1) + "\n")
                    buffer.append(res.getString(2) + "\n")
                    buffer.append(res.getString(3) + "\n")
                    buffer.append(res.getString(4) + "\n")
                    buffer.append(res.getString(5) + "\n")
                    buffer.append("\n") // Linha em branco como separador
                }
                showDialog("Data Listing", buffer.toString())
            }
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val backupSuccess: Boolean = backupDatabase()
                if (backupSuccess) {
                    Toast.makeText(this,"Backup do banco de dados concluído com sucesso.",Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(this, "Falha ao realizar o backup do banco de dados.",  Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this,"Permissão de escrita no armazenamento externo negada.", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}