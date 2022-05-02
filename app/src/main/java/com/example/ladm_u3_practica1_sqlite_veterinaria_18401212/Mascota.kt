package com.example.ladm_u3_practica1_sqlite_veterinaria_18401212

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException

class Mascota (este: Context) {
    var este = este
    var id_mascota = ""
    var nombre = ""
    var raza = ""
    var curp = ""
    var err = ""

    fun insertar() : Boolean {
        var baseDatos = DataBase(este, "mascota1", null, 1)
        err = ""
        try {
            val tabla = baseDatos.writableDatabase

            var datos = ContentValues()

            datos.put("NOMBRE",nombre)
            datos.put("RAZA",raza)
            datos.put("CURP_PRO",curp)

            var resultado = tabla.insert("MASCOTA",null,datos)
            if (resultado == -1L) {
                return false
            }
        } catch (err: SQLiteException) {
            this.err = err.message!!
            return false
        } finally {
            baseDatos.close()
        }
        return true
    }

    fun actualizar() : Boolean {
        var baseDatos = DataBase(este, "mascota1", null, 1)
        err = ""
        try {
            var tabla = baseDatos.writableDatabase
            val datosActualizados = ContentValues()

            datosActualizados.put("ID_MASCOTA",id_mascota)
            datosActualizados.put("NOMBRE",nombre)
            datosActualizados.put("RAZA",raza)
            datosActualizados.put("CURP_PRO",curp)

            val resultado = tabla.update("MASCOTA",datosActualizados,"ID_MASCOTA=?", arrayOf(id_mascota))
            if (resultado == 0) {
                return false
            }
        } catch (err: SQLiteException) {
            this.err = err.message!!
            return false
        } finally {
            baseDatos.close()
        }
        return true
    }

    fun eliminar() : Boolean {
        var baseDatos = DataBase(este, "mascota1", null, 1)
        err = ""
        try {
            var tabla = baseDatos.writableDatabase
            val respuesta = tabla.delete("MASCOTA","ID_MASCOTA=?", arrayOf(id_mascota))

            if (respuesta == 0) {
                return false
            }
        } catch (err: SQLiteException) {
            this.err = err.message!!
            return false
        } finally {
            baseDatos.close()
        }
        return true
    }


    fun mostrarMascota(buscar:String) : Mascota {
        var baseDatos = DataBase(este, "mascota1", null, 1)
        err = ""
        val mascota = Mascota(este)

        try {
            var tabla = baseDatos.readableDatabase
            var sentenciaSQL = "SELECT * FROM MASCOTA WHERE ID_MASCOTA=?"

            var cursor = tabla.rawQuery(sentenciaSQL, arrayOf(buscar))
            if (cursor.moveToFirst()) {
                mascota.id_mascota = cursor.getString(0)
                mascota.nombre = cursor.getString(1)
                mascota.raza = cursor.getString(2)
                mascota.curp = cursor.getString(3)
            }
        } catch (err: SQLiteException) {
            this.err = err.message!!
        } finally {
            baseDatos.close()
        }
        return mascota
    }

    fun buscarFiltroMascota(busqueda:String,filtro:String) : ArrayList<Mascota> {
        var sentenciaSQL = ""
        var baseDatos = DataBase(este, "mascota1", null, 1)
        err = ""
        var arreglo = ArrayList<Mascota>()
        var bus = busqueda
        var columnaBuscar = filtro
       

        try {
            when (columnaBuscar) {
                "CURP" -> { sentenciaSQL = "SELECT * FROM MASCOTA WHERE CURP_PRO LIKE '${bus}%'" }
                "NOMBRE_MASCOTA" -> { sentenciaSQL = "SELECT * FROM MASCOTA WHERE NOMBRE LIKE '${bus}%' OR NOMBRE LIKE '%${bus}%'" }
                "RAZA" -> { sentenciaSQL = "SELECT * FROM MASCOTA WHERE RAZA LIKE '${bus}%'" }
                "NOMBRE_PROPIETARIO" -> { sentenciaSQL = "SELECT * FROM MASCOTA INNER JOIN PROPIETARIO ON PROPIETARIO.CURP LIKE '${bus}%'" }
            }
            var tabla = baseDatos.readableDatabase

            var cursor = tabla.rawQuery(sentenciaSQL, null)
            if (cursor.moveToFirst()) {
                do {
                    val mascota = Mascota(este)
                    mascota.id_mascota = cursor.getString(0)
                    mascota.nombre = cursor.getString(1)
                    mascota.raza = cursor.getString(2)
                    mascota.curp = cursor.getString(3)
                    arreglo.add(mascota)
                } while (cursor.moveToNext())
            }
        } catch (err: SQLiteException) {
            this.err = err.message!!
        } finally {
            baseDatos.close()
        }
        return arreglo
    }

    fun datosMascota() : String {
        return "NOMBRE: ${nombre}\n " +
                "RAZA: ${raza}\n" +
                "CURP DUEÑO: ${curp}"
    }
}