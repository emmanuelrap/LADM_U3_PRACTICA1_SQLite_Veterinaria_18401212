package com.example.ladm_u3_practica1_sqlite_veterinaria_18401212

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.ladm_u3_practica1_sqlite_veterinaria_18401212.databinding.ActivityPropietarioActualizarBinding

class PropietarioActualizar : AppCompatActivity() {
    lateinit var binding: ActivityPropietarioActualizarBinding
    var curp = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropietarioActualizarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        curp = this.intent.extras!!.getString("propietarioActualizar")!!
        var propietario = Propietario(this).mostrarPropietario(curp)

        binding.txtcurp.setText(propietario.curp)
        binding.txtnombrePropietario.setText(propietario.nombre)
        binding.txttelefono.setText(propietario.telefono)
        binding.txtedadPropietario.setText(propietario.edad.toString())

        binding.btnActualizar.setOnClickListener {
            var propietario = Propietario(this)

            try {
                propietario.curp = binding.txtcurp.text.toString()
                propietario.nombre = binding.txtnombrePropietario.text.toString()
                propietario.telefono = binding.txttelefono.text.toString()
                propietario.edad = binding.txtedadPropietario.text.toString().toInt()
            } catch (e:Exception) {
                AlertDialog.Builder(this)
                    .setTitle("ATENCIÓN")
                    .setMessage("HAY CAMPOS VACIOS")
                    .show()
                return@setOnClickListener
            }


            var respuesta = propietario.actualizar()

            if (respuesta) {
                Toast.makeText(this, "ACTUALIZACION DE DATOS EXITOSA! )", Toast.LENGTH_LONG).show()
                clear()
                finish()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("ERROR")
                    .setMessage("NO SE PUDO REALIZAR LA OPERACION")
                    .show()
            }
        }
    }

    fun clear() {
        binding.txtcurp.setText("")
        binding.txtnombrePropietario.setText("")
        binding.txttelefono.setText("")
        binding.txtedadPropietario.setText("")
    }
}