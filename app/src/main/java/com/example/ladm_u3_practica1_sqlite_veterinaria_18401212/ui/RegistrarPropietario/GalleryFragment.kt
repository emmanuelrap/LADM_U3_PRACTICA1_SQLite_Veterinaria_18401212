package com.example.ladm_u3_practica1_sqlite_veterinaria_18401212.ui.RegistrarPropietario

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ladm_u3_practica1_sqlite_veterinaria_18401212.Propietario
import com.example.ladm_u3_practica1_sqlite_veterinaria_18401212.PropietarioActualizar
import com.example.ladm_u3_practica1_sqlite_veterinaria_18401212.databinding.FragmentDuenoRegistroBinding


class GalleryFragment : Fragment() {

    private var _binding: FragmentDuenoRegistroBinding? = null
    var IDs = ArrayList<String>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
        _binding = FragmentDuenoRegistroBinding.inflate(inflater, container, false)
        val root: View = binding.root


        //mostrar todos
        mostrarPropietariosBuscados("","CURP")

        binding.insertar.setOnClickListener {
            var propietario = Propietario(requireContext())

            propietario.telefono = binding.etTelefono.text.toString()
            propietario.edad = binding.etEdad.text.toString().toInt()
            propietario.curp = binding.etCurp.text.toString()
            propietario.nombre = binding.etNombre.text.toString()
          

            var resultado = propietario.insertar()
            if(resultado) {
                Toast.makeText(requireContext(),"NUEVO PROPIETARIO INGRESADO!!", Toast.LENGTH_LONG).show()
                mostrarPropietariosBuscados("","CURP") //mostrar todos
                clear()
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle("ERROR AL INSERTAR")
                    .setMessage("ERROR AL INSERTAR, INTENTE NUEVAMENTE")
                    .show()
            }
        }// FIN INSERTAR

        //BOTONES DE PARA BUSCAR PROPIETARIOS
        binding.btnBuscar.setOnClickListener {
            if(binding.etBuscar.text.toString() == ""){
                Toast.makeText(requireContext(),"Ingrese un Dato para Buscar", Toast.LENGTH_LONG)
                    .show()
            }else{
            var cadenaBuscar = binding.etBuscar.text.toString()
                mostrarPropietariosBuscados(cadenaBuscar,"CURP")
            }
        }

        binding.btnBuscarNombre.setOnClickListener {
            if(binding.etBuscar.text.toString() == ""){
                Toast.makeText(requireContext(),"Ingrese un Dato para Buscar", Toast.LENGTH_LONG)
                    .show()
            }else{
            var cadenaBuscar = binding.etBuscar.text.toString()
            mostrarPropietariosBuscados(cadenaBuscar,"NOMBRE")}
        }

        binding.btnBuscarEdad.setOnClickListener {
            if(binding.etBuscar.text.toString() == ""){
                Toast.makeText(requireContext(),"Ingrese un Dato para Buscar", Toast.LENGTH_LONG)
                    .show()
            }else{
            var cadenaBuscar = binding.etBuscar.text.toString()
            mostrarPropietariosBuscados(cadenaBuscar,"EDAD")}
        }

        binding.btnBuscarTelefono.setOnClickListener {
            if(binding.etBuscar.text.toString() == ""){
                Toast.makeText(requireContext(),"Ingrese un Dato para Buscar", Toast.LENGTH_LONG)
                    .show()
            }else{
            var cadenaBuscar = binding.etBuscar.text.toString()
            mostrarPropietariosBuscados(cadenaBuscar,"TELEFONO")}
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onResume() {
        super.onResume()
        mostrarPropietariosBuscados("","CURP")
    }



    fun clear() {
        binding.etCurp.setText("")
        binding.etCurp.setText("")
        binding.etTelefono.setText("")
        binding.etNombre.setText("")
    }

    fun mostrarPropietariosBuscados(cadenaBuscar:String, filtro:String) {
        var listaFiltro = Propietario(requireContext()).mostrarFiltro(cadenaBuscar,filtro)
        var informacionPropietario = ArrayList<String>()
        var datosPropietario = Propietario(requireContext())
        IDs.clear()
        
        (0..listaFiltro.size-1).forEach {
            val persona = listaFiltro.get(it)
            datosPropietario.curp = persona.curp
            datosPropietario.nombre = persona.nombre
            datosPropietario.telefono = persona.telefono
            datosPropietario.edad = persona.edad

            informacionPropietario.add(datosPropietario.contenido())
            IDs.add(persona.curp)
        }
        binding.listaPropietario.adapter = ArrayAdapter(requireContext(),R.layout.simple_list_item_1,informacionPropietario)
        binding.listaPropietario.setOnItemClickListener { adapterView, view, indice, l ->
            val curpLista = IDs.get(indice)
            val propietario = Propietario(requireContext()).mostrarPropietario(curpLista)

            AlertDialog.Builder(requireContext())
                .setTitle("ATENCIÓN")
                .setMessage("¿Desea Eliminar o Actualizar a ${propietario.nombre} ?")
                .setNegativeButton("Eliminar") {d,i ->
                    Toast.makeText(requireContext(),"Eliminacion Exitosa!!", Toast.LENGTH_LONG).show()
                    propietario.eliminar()
                    mostrarPropietariosBuscados("","CURP") //mostrar todos
                }
                .setPositiveButton("Actualizar") {d,i ->
                    val intentVentanaActualizar = Intent(requireActivity(), PropietarioActualizar::class.java)
                    intentVentanaActualizar.putExtra("propietarioActualizar", propietario.curp)
                    startActivity(intentVentanaActualizar)
                }
                .setNeutralButton("Cerrar") {d,i -> }
                .show()

        }
    }
}