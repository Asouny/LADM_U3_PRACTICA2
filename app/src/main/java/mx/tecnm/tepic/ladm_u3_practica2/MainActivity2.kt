package mx.tecnm.tepic.ladm_u3_practica2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.descripEvent
import kotlinx.android.synthetic.main.activity_main.fechaEvent
import kotlinx.android.synthetic.main.activity_main.horaEvent
import kotlinx.android.synthetic.main.activity_main.lugarEvent
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {

    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        var extras = intent.extras

        lugarEvent.setText(extras!!.getString("Lugar"))
        horaEvent.setText(extras!!.getString("Hora"))
        fechaEvent.setText(extras!!.getString("Fecha"))
        descripEvent.setText(extras!!.getString("Descripcion"))

        id = extras.getInt("ID").toString()

        updateEvent.setOnClickListener{
            var agendaUpdate = Agenda(lugarEvent.text.toString(),horaEvent.text.toString(),fechaEvent.text.toString(),descripEvent.text.toString())
            agendaUpdate.id = id.toInt()
            agendaUpdate.asignarPuntero(this)
            if(agendaUpdate.actualizar()==true){
                mensaje("Se actualizo correctamente el registro: "+agendaUpdate.id)
            }else{
                mensaje("No se pudo actualizar el registro: ")
            }
            finish()
        }

        deleteEvent.setOnClickListener{
            var agendaDelete = Agenda("","","","")
            agendaDelete.id = id.toInt()
            agendaDelete.asignarPuntero(this)
            if(agendaDelete.eliminar()){
                mensaje("SE ELIMINÓ")
            }else
            {
                mensaje("No se pudo eliminar")
            }
            finish()
        }

    }

    fun mensaje(s:String){
        AlertDialog.Builder(this)
            .setTitle("Atencion, ¿seguro que desea eliminar este registro?").setMessage(s)
            .setPositiveButton("ok"){d,i->}
            .show()
    }
}