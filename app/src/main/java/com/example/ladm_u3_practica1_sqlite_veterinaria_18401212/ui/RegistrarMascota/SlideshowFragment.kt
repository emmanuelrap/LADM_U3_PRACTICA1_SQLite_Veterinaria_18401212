package com.example.ladm_u3_practica1_sqlite_veterinaria_18401212.ui.RegistrarMascota

import android.R
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ladm_u3_practica1_sqlite_veterinaria_18401212.Mascota
import com.example.ladm_u3_practica1_sqlite_veterinaria_18401212.MascotaActualizar
import com.example.ladm_u3_practica1_sqlite_veterinaria_18401212.Propietario
import com.example.ladm_u3_practica1_sqlite_veterinaria_18401212.databinding.FragmentSlideshowBinding

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var IDs = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mostrarPropietario("")//Mostrar todos los propietarios
        mostrarMascotaBuscada("","CURP")  //mostrar Mascotas

        binding.insertarMascota.setOnClickListener {
            var id = binding.txtcurp.text.toString()
            if (id == "") {
                AlertDialog.Builder(requireContext())
                    .setMessage("Debe poner una CURP del propietario")
                    .setNeutralButton("ACEPTAR") {d,i -> }
                    .show()
                return@setOnClickListener
            } else {
                if (binding.txtnombreMascota.text.toString() == "") {
                    AlertDialog.Builder(requireContext())
                        .setMessage("Agrege un Nombre")
                        .setNeutralButton("ACEPTAR") {d,i -> }
                        .show()
                    return@setOnClickListener
                } else {
                    var mascota = Mascota(requireContext())

                    mascota.nombre = binding.txtnombreMascota.text.toString()
                    mascota.raza = binding.txtRaza.text.toString()
                    mascota.curp = binding.txtcurp.text.toString()

                    var resultado = mascota.insertar()
                    if(resultado) {
                        Toast.makeText(requireContext(),"Mascota Registrada!!", Toast.LENGTH_LONG).show()
                        mostrarMascotaBuscada("","CURP")

                        binding.txtRaza.setText("")
                        binding.txtcurp.setText("")
                        binding.txtnombreMascota.setText("")
                    } else {
                        AlertDialog.Builder(requireContext())
                            .setTitle("ERROR")
                            .setMessage("Error: No se pudo hacer la Accion ")
                            .show()
                    }
                }
            }
        }

        binding.btnBuscarNombre.setOnClickListener {
            if(binding.etBuscar.text.toString() == ""){
                Toast.makeText(requireContext(),"Ingrese un Dato para Buscar", Toast.LENGTH_LONG)
                    .show()
            }else{
                var cadenaBuscar = binding.etBuscar.text.toString()
                mostrarMascotaBuscada(cadenaBuscar,"NOMBRE_MASCOTA")}
        }

        binding.btnBuscarRaza.setOnClickListener {
            if(binding.etBuscar.text.toString() == ""){
                Toast.makeText(requireContext(),"Ingrese un Dato para Buscar", Toast.LENGTH_LONG)
                    .show()
            }else{
                var cadenaBuscar = binding.etBuscar.text.toString()
                mostrarMascotaBuscada(cadenaBuscar,"RAZA")}
        }

        binding.btnBuscarCurp.setOnClickListener {
            if(binding.etBuscar.text.toString() == ""){
                Toast.makeText(requireContext(),"Ingrese un Dato para Buscar", Toast.LENGTH_LONG)
                    .show()
            }else{
                var cadenaBuscar = binding.etBuscar.text.toString()
                mostrarMascotaBuscada(cadenaBuscar,"CURP")}
        }

        binding.btnBuscarNombre.setOnClickListener {
            if(binding.etBuscar.text.toString() == ""){
                Toast.makeText(requireContext(),"Ingrese un Dato para Buscar", Toast.LENGTH_LONG)
                    .show()
            }else{
                var cadenaBuscar = binding.etBuscar.text.toString()
                mostrarMascotaBuscada(cadenaBuscar,"NOMBRE_PROPIETARIO")}
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun mostrarPropietario(busqueda:String) {
        var Filtro = Propietario(requireContext()).buscarPropietario(busqueda)
        var listaInformacion = ArrayList<String>()
        var informacion = Propietario(requireContext())

        IDs.clear()
        
        (0..Filtro.size-1).forEach {
            val persona = Filtro.get(it)
            informacion.curp = persona.curp
            informacion.nombre = persona.nombre

            listaInformacion.add(persona.curp + " - "+ informacion.getName())
            IDs.add(persona.curp)
        }

        binding.listaPropietario.adapter = ArrayAdapter(requireContext(),
            R.layout.simple_list_item_1,listaInformacion)
    }

    fun mostrarMascotaBuscada(busqueda:String, filtro:String) {
        var Filtro = Mascota(requireContext()).buscarFiltroMascota(busqueda,filtro)
        var listaInformacion = ArrayList<String>()
        var Data = Mascota(requireContext())

        IDs.clear()
        (0..Filtro.size-1).forEach {
            val pets = Filtro.get(it)
            Data.curp = pets.id_mascota
            Data.nombre = pets.nombre
            Data.raza = pets.raza
            Data.curp = pets.curp

            listaInformacion.add(Data.datosMascota())
            IDs.add(pets.id_mascota)
        }
        binding.listaMascotas.adapter = ArrayAdapter(requireContext(),R.layout.simple_list_item_1,listaInformacion)

        //Escuha cuando damos clic en la Lista
        binding.listaMascotas.setOnItemClickListener { adapterView, view, indice, l ->
            val idLista = IDs.get(indice)
            val pet = Mascota(requireContext()).mostrarMascota(idLista)

            AlertDialog.Builder(requireContext())
                .setMessage("¿Desea Elimnar o Modificar a  ${pet.nombre} ?")
                .setNegativeButton("Eliminar") {d,i ->
                    pet.eliminar()
                    mostrarMascotaBuscada("","CURP")
                }
                .setPositiveButton("Actualizar") {d,i ->
                    val otraVentana = Intent(requireActivity(), MascotaActualizar::class.java)
                    otraVentana.putExtra("mascotaActualizar", pet.id_mascota)
                    startActivity(otraVentana)
                }
                .setNeutralButton("Cerrar") {d,i -> }
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        mostrarMascotaBuscada("","CURP")
    }
}