package com.example.ladm_u3_practica1_sqlite_veterinaria_18401212

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException

class Propietario (act: Context) {
    var act = act
    var curp:String  = ""
    var nombre:String = ""
    var edad:Int = 0
    var telefono:String  = ""
    var err = ""
   

    fun insertar() : Boolean {
        var baseDatos = DataBase(act, "propietario1", null, 1)
        err = ""
        try {
            val tabla = baseDatos.writableDatabase

            var datos = ContentValues()

            datos.put("CURP", curp)
            datos.put("NOMBRE",nombre)
            datos.put("TELEFONO",telefono)
            datos.put("EDAD",edad)

            var resultado = tabla.insert("PROPIETARIO",null,datos)
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

    fun mostrarTodos() : ArrayList<Propietario> {
        var baseDatos = DataBase(act, "propietario1", null, 1)
        err = ""
        var arreglo = ArrayList<Propietario>()

        try {
            var tabla = baseDatos.readableDatabase
            var senteciaSQL = "SELECT * FROM PROPIETARIO"


            var cursor = tabla.rawQuery(senteciaSQL,null)
            if (cursor.moveToFirst()) {
                do {
                    val propietario = Propietario(act)
                    propietario.curp = cursor.getString(0)
                    propietario.nombre = cursor.getString(1)
                    propietario.telefono = cursor.getString(2)
                    propietario.edad = cursor.getString(3).toInt()
                    arreglo.add(propietario)
                } while (cursor.moveToNext())
            }
        } catch (err: SQLiteException) {
            this.err = err.message!!
        } finally {
            baseDatos.close()
        }
        return arreglo
    }

    fun mostrarPropietario(curpBuscar:String) : Propietario {
        var baseDatos = DataBase(act, "propietario1", null, 1)
        err = ""
        val propietario = Propietario(act)

        try {
            var tabla = baseDatos.readableDatabase
            var senteciaSQL = "SELECT * FROM PROPIETARIO WHERE CURP=?"

            var cursor = tabla.rawQuery(senteciaSQL, arrayOf(curpBuscar))
            if (cursor.moveToFirst()) {
                propietario.curp = cursor.getString(0)
                propietario.nombre = cursor.getString(1)
                propietario.telefono = cursor.getString(2)
                propietario.edad = cursor.getString(3).toInt()
            }
        } catch (err: SQLiteException) {
            this.err = err.message!!
        } finally {
            baseDatos.close()
        }
        return propietario
    }
    
    fun buscarPropietario(textoBuscar:String) : ArrayList<Propietario> {
        var baseDatos = DataBase(act, "propietario1", null, 1)
        err = ""
        var arreglo = ArrayList<Propietario>()
        var textoBuscar = textoBuscar

        try {
            var tabla = baseDatos.readableDatabase
            var senteciaSQL = "SELECT * FROM PROPIETARIO WHERE NOMBRE LIKE '${textoBuscar}%' OR NOMBRE LIKE '%${textoBuscar}%'"

            var cursor = tabla.rawQuery(senteciaSQL, null)
            if (cursor.moveToFirst()) {
                do {
                    val propietario = Propietario(act)
                    propietario.curp = cursor.getString(0)
                    propietario.nombre = cursor.getString(1)
                    propietario.telefono = cursor.getString(2)
                    propietario.edad = cursor.getString(3).toInt()
                    arreglo.add(propietario)
                } while (cursor.moveToNext())
            }
        } catch (err: SQLiteException) {
            this.err = err.message!!
        } finally {
            baseDatos.close()
        }
        return arreglo
    }

    fun mostrarFiltro(textoBuscar:String,columnaBuscar:String) : ArrayList<Propietario> {
        var baseDatos = DataBase(act, "propietario1", null, 1)
        err = ""
        var arreglo = ArrayList<Propietario>()
        var textoBuscar = textoBuscar
        var columnaBuscar = columnaBuscar
        var senteciaSQL = ""

        try {
            when (columnaBuscar) {
                "CURP" -> { senteciaSQL = "SELECT * FROM PROPIETARIO WHERE CURP LIKE '${textoBuscar}%'" }
                "NOMBRE" -> { senteciaSQL = "SELECT * FROM PROPIETARIO WHERE NOMBRE LIKE '${textoBuscar}%' OR NOMBRE LIKE '%${textoBuscar}%'" }
                "TELEFONO" -> { senteciaSQL = "SELECT * FROM PROPIETARIO WHERE TELEFONO LIKE '${textoBuscar}%'" }
                "EDAD" -> { senteciaSQL = "SELECT * FROM PROPIETARIO WHERE EDAD LIKE '${textoBuscar}%'" }
            }
            var tabla = baseDatos.readableDatabase

            var cursor = tabla.rawQuery(senteciaSQL, null)
            if (cursor.moveToFirst()) {
                do {
                    val propietario = Propietario(act)
                    propietario.curp = cursor.getString(0)
                    propietario.nombre = cursor.getString(1)
                    propietario.telefono = cursor.getString(2)
                    propietario.edad = cursor.getString(3).toInt()
                    arreglo.add(propietario)
                } while (cursor.moveToNext())
            }
        } catch (err: SQLiteException) {
            this.err = err.message!!
        } finally {
            baseDatos.close()
        }
        return arreglo
    }

    fun actualizar() : Boolean {
        var baseDatos = DataBase(act, "propietario1", null, 1)
        err = ""
        try {
            var tabla = baseDatos.writableDatabase
            val datosActualizados = ContentValues()

            datosActualizados.put("NOMBRE",nombre)
            datosActualizados.put("TELEFONO",telefono)
            datosActualizados.put("EDAD",edad)

            val resultado = tabla.update("PROPIETARIO",datosActualizados,"CURP=?", arrayOf(curp))
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
        var baseDatos = DataBase(act, "propietario1", null, 1)
        err = ""
        try {
            var tabla = baseDatos.writableDatabase
            val respuesta = tabla.delete("PROPIETARIO","CURP=?", arrayOf(curp))

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

    fun contenido() : String {
        return "CURP: ${curp}\nNOMBRE: ${nombre}\n" +
                "TELEFONO: ${telefono}\n" +
                "EDAD: ${edad}"
    }

    fun getName() : String {
        return nombre
    }
}