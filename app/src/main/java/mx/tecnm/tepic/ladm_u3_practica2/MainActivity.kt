package mx.tecnm.tepic.ladm_u3_practica2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import mx.tecnm.tepic.ladm_u3_practica2.Agenda
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception



class MainActivity : AppCompatActivity() {
    var baseRemota = FirebaseFirestore.getInstance()
    var datos = ArrayList<String>()
    var listaID = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

/*ACTUALIZAR LA BASE DE DATOS:
Lo que se esta intentando aqui es en principio lo que el maestro beningo mensionaba. la base de datos de firebase tiene que ser un espejo
a la local, por eso. la manera mas facil (pero no correcta) de hacerlo, es borrar toda la coleccion y insertar la local de nuevo

En principio un espejo no funciona de esta manera pero es una alternativa rapida y viable.
 */
        fab.setOnClickListener {   view ->Snackbar.make(view, "Se actualizo la base de datos de FIREBASE", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()

            var conexion = Agenda("","","","")//recuperar data
            conexion.asignarPuntero(this)
            var data = conexion.mostrarTodos()
            var total = listaID.size

            (0..10).forEach {
               eliminar(it.toString())
            }
            cargarDatos()
        }

        insertar.setOnClickListener {
            var trabajador = Agenda(
                    lugarEvent.text.toString(),
                    horaEvent.text.toString(),
                    fechaEvent.text.toString(),
                    descripEvent.text.toString())

            trabajador.asignarPuntero(this)

            var resultado = trabajador.insertar()

            if(resultado== true){
                mensaje("se capturó el evento exitosamente")
                lugarEvent.setText("")
                horaEvent.setText("")
                fechaEvent.setText("")
                descripEvent.setText("")
                cargarLista()
            }else{
                when(trabajador.error){
                    1->{dialogo("error en tabla, no se creó o no se conectó en base de datos")}
                    2-> { dialogo("no se pudo insetar") }
                }
            }

        }
        cargarLista()
    }

    fun cargarDatos(){
            var conexion = Agenda("","","","")//recuperar data
            conexion.asignarPuntero(this)
            var data = conexion.mostrarTodos()

            if(data.size==0){
                if(conexion.error==3){
                    dialogo("no se pudo realizar consulta / tabla vacía")
                }
                return
            }
            var total = data.size-1
            var vector = Array<String>(data.size,{""})
            listaID = ArrayList<String>()
            (0..total).forEach {
                var agenda = data[it]
                var item =  agenda.id.toString()+"\n"+agenda.lugar+"\n"+agenda.hora+"\n"+agenda.fecha+"\n"+agenda.descrip
                vector[it] = item
                listaID.add(agenda.id.toString())


                var datosInsertar = hashMapOf(
                        "descripcion" to agenda.descrip,
                        "lugar" to agenda.lugar,
                        "fecha" to agenda.fecha,
                        "hora" to agenda.hora
                )
                baseRemota.collection("evento").document(agenda.id.toString()).set(datosInsertar)
            }
            lista.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,vector)

    }

    fun cargarLista(){
        try {
            var conexion = Agenda("","","","")//recuperar data
            conexion.asignarPuntero(this)
            var data = conexion.mostrarTodos()

            if(data.size==0){
                if(conexion.error==3){
                    dialogo("no se pudo realizar consulta / tabla vacía")
                }
                return
            }

            var total = data.size-1
            var vector = Array<String>(data.size,{""})
            listaID = ArrayList<String>()
            (0..total).forEach {
                var agenda = data[it]
                var item = agenda.lugar+"\n"+agenda.hora+"\n"+agenda.fecha+"\n"+agenda.descrip
                vector[it] = item
                listaID.add(agenda.id.toString())
            }
            lista.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,vector)
            lista.setOnItemClickListener { parent, view, position, id ->
                var con = Agenda("", "", "","")
                con.asignarPuntero(this)
                var agendaEncontrada = con.buscar(listaID[position])
                if(con.error==4){
                    dialogo("error no se encontro id")
                    return@setOnItemClickListener
                }
                AlertDialog.Builder(this)
                        .setTitle("¿Que deseas hacer?")
                        .setMessage("ID: ${agendaEncontrada.id}\n"+
                                "Lugar: ${agendaEncontrada.lugar}\nHora: ${agendaEncontrada.hora}\nFecha: ${agendaEncontrada.fecha}\n" +
                                "Descipcion: ${agendaEncontrada.descrip}")
                        //?
                    .setPositiveButton("Editar o Eliminar evento"){ d,i-> otroActivity(agendaEncontrada)
                    }
                        .setNeutralButton("Cancelar"){d,i->}
                        .show()

            }
        }catch (e:Exception){
            dialogo(e.message.toString())
        }
    }

    private fun otroActivity(a: Agenda){
    var intento = Intent(this, MainActivity2::class.java)

    intento.putExtra("Lugar",a.lugar)
    intento.putExtra("Hora",a.hora)
    intento.putExtra("Fecha",a.fecha)
    intento.putExtra("Descripcion",a.descrip)
    intento.putExtra("ID",a.id)
    startActivityForResult(intento,0)

}

    fun mensaje(s:String){
        Toast.makeText(this,s,Toast.LENGTH_LONG)
                .show()
    }

    fun dialogo(s:String){
        AlertDialog.Builder(this)
                .setTitle("Atencion").setMessage(s)
                .setPositiveButton("ok"){d,i->}
                .show()
    }

    private fun eliminar(idLista: String) {
        baseRemota.collection("evento")
                .document(idLista)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "SE ELIMINÓ CON ÉXITO EL EVENTO",Toast.LENGTH_LONG)
                            .show()
                }
                .addOnFailureListener{
                    mensaje("ERROR: NO SE ELIMINÓ\n"+it.message!!)
                }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        cargarLista()
    }
}