package mx.tecnm.tepic.ladm_u3_practica2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException

/*Agenda:
	Lugar: Hemilianos
	Hora: 9PM
	Fecha: 10 de diciembre
	Descripcion: Cena con los rivadeneira

FECHA DE TIPO DATE - Borrar fechas.
- Sincronizar */
class Agenda(l:String, h:String, f:String,d:String) {
    var lugar = l
    var hora = h
    var fecha = f
    var id = 0
    var descrip = d
    var error = -1

    val nombreBaseDatos = "Agenda"
    var puntero : Context ?= null

    fun asignarPuntero(p:Context){
        puntero = p
    }

    fun insertar():Boolean{
        error = -1

        try{
            var base = BaseDatos(puntero!!, nombreBaseDatos,null,1)
            var insertar = base.writableDatabase
            var datos = ContentValues()

            datos.put("LUGAR",lugar)
            datos.put("HORA",hora)
            datos.put("FECHA",fecha)
            datos.put("DESCRIPCION",descrip)


            var respuesta = insertar.insert("AGENDA","IDAGENDA",datos)
            if(respuesta.toInt()== -1){
                error = 2
                return false
            }
        }catch (e: SQLiteException){
            error = 1
            return false
        }
        return true
    }

    fun mostrarTodos():ArrayList<Agenda>{
        var data = ArrayList<Agenda>()
        error = -1
        try{
            var base = BaseDatos(puntero!!,nombreBaseDatos,null,1 )
            var select = base.readableDatabase
            var columnas = arrayOf("*")

            var cursor  = select.query("AGENDA", columnas, null, null, null, null, null)
            if(cursor.moveToFirst()){
                do{
                    var trabajadorTemporal = Agenda(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4))

                    trabajadorTemporal.id = cursor.getInt(0)
                    data.add(trabajadorTemporal)
                }while (cursor.moveToNext())
            }else{
                error = 3
            }
        }catch (e:SQLiteException){
            error = 1
        }
        return data
    }

    fun buscar(id:String): Agenda{
        var AgendaEncontrada = Agenda("-1","-1","-1","-1")

        error = -1
        try {
            var base = BaseDatos(puntero!!, nombreBaseDatos, null, 1)
            var select = base.readableDatabase
            var columnas = arrayOf("*")
            var idBuscar = arrayOf(id)

            var cursor = select.query("AGENDA", columnas, "IDAGENDA =?",idBuscar, null, null, null)
            if(cursor.moveToFirst()){
                AgendaEncontrada.id = id.toInt()
                AgendaEncontrada.lugar = cursor.getString(1)
                AgendaEncontrada.hora = cursor.getString(2)
                AgendaEncontrada.fecha = cursor.getString(3)
                AgendaEncontrada.descrip = cursor.getString(4)
            }else{
                error = 4
            }
        }catch (e:SQLiteException){
            error = 1
        }
        return AgendaEncontrada
    }

    fun eliminar():Boolean{
        error = -1
        try{
            var base = BaseDatos(puntero!!, nombreBaseDatos,null,1)
            var eliminar = base.writableDatabase
            var idEliminar = arrayOf(id.toString())

            var respuesta = eliminar.delete("AGENDA","IDAGENDA = ?",idEliminar)
            if(respuesta.toInt()== 0){
                error = 6
                return false
            }
        }catch (e:SQLiteException){
            error = 1
            return false
        }
        return true
    }

    fun actualizar():Boolean{
        error = -1
        try{
            var base = BaseDatos(puntero!!, nombreBaseDatos,null,1)
            var actualizar = base.writableDatabase
            var datos = ContentValues()
            var idActualizar = arrayOf(id.toString())

            datos.put("LUGAR",lugar)
            datos.put("HORA",hora)
            datos.put("FECHA",fecha)
            datos.put("DESCRIPCION",descrip)


            var respuesta = actualizar.update("AGENDA",datos,"IDAGENDA = ?", idActualizar)
            if(respuesta.toInt()== 0){
                error = 5
                return false
            }
        }catch (e:SQLiteException){
            error = 1
            return false
        }
        return true
    }


}