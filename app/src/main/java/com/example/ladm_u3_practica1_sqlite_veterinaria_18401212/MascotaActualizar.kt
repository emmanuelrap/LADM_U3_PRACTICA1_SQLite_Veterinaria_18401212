package com.example.ladm_u3_practica1_sqlite_veterinaria_18401212

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.ladm_u3_practica1_sqlite_veterinaria_18401212.databinding.ActivityMascotaActualizarBinding
import com.example.ladm_u3_practica1_sqlite_veterinaria_18401212.databinding.ActivityPropietarioActualizarBinding

class MascotaActualizar : AppCompatActivity() {
    lateinit var binding: ActivityMascotaActualizarBinding
    var id_mascota = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMascotaActualizarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id_mascota = this.intent.extras!!.getString("mascotaActualizar")!!

        var mascota = Mascota(this).mostrarMascota(id_mascota)
        var propietario = Propietario(this).mostrarPropietario(mascota.curp)

        binding.txtcurp.setText(propietario.curp)
        binding.txtnombrePropietario.setText(propietario.nombre)
        binding.txttelefono.setText(propietario.telefono)
        binding.txtedadPropietario.setText(propietario.edad.toString())
        binding.etID.setText(mascota.id_mascota)
        binding.etNombre.setText(mascota.nombre)
        binding.etCurpPropietario.setText(mascota.curp)

        binding.btnActualizar.setOnClickListener {
            var mascota = Mascota(this)

            try {
                mascota.id_mascota = binding.etID.text.toString()
                mascota.nombre = binding.etNombre.text.toString()
                mascota.raza = binding.etRaza.text.toString()
                mascota.curp = binding.etCurpPropietario.text.toString()
            } catch (e:Exception) {
                AlertDialog.Builder(this)
                    .setMessage("REVISE QUE LOS CAMPOS ESTEN COMPLETOS")
                    .show()
                return@setOnClickListener
            }

            var respuesta = mascota.actualizar()

            if (respuesta) {
                Toast.makeText(this, "DATOS ACTUALIZADOS!!", Toast.LENGTH_LONG).show()
                limpiarCampos()
                finish()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("ERROR")
                    .setMessage("ERROR AL REALIZAR LA OPERACION")
                    .show()
                finish()
            }
        }
    }

    fun limpiarCampos() {
        binding.txtcurp.setText("")
        binding.txtnombrePropietario.setText("")
        binding.txttelefono.setText("")
        binding.txtedadPropietario.setText("")
    }

}