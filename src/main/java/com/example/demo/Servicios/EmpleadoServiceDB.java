package com.example.demo.Servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.example.demo.Modelos.Empleado;
import com.example.demo.Repositorios.EmpleadoRepository;

//Servicio encargado de gestionar el comportamiento de la base de datos
@Primary
@Component
public class EmpleadoServiceDB{

	//interfaz que extiende de la interfaz que tiene los metodos necesarios para gestionar la base de datos
	@Autowired
	private EmpleadoRepository repositorio;
	
	/**
	 * metodo para añadir un empleado
	 * @param e -> el empleado a añadir
	 * @return
	 */
	public Empleado add(Empleado e) {
		return repositorio.save(e);
	}
	
	/**
	 * metodo para hacer una consulta de todos los empleados
	 * @return
	 */
	public List<Empleado> findAll() {
		List<Empleado> empleados = repositorio.findAll();
		return empleados;
	}
	
	/**
	 * metodo para buscar a un empleado por su id
	 * @param id -> el id del empleado que queremos buscar
	 * @return
	 */
	public Empleado findById(long id) {
		Empleado e = (Empleado) repositorio.findById(id).orElse(null);
		return e;
	}

	/**
	 * metodo para editar un empleado
	 * @param e -> el empleado ya editado
	 * @return
	 */
	public Empleado edit(Empleado e) {
		return repositorio.save(e);
	}
	
	//buscador, le llegará cualquier cosa(nombre, email, telefono) y el buscara por esa cosa, en los tres campos
	public List<Empleado> buscador(String cadena) {
		List<Empleado> empleados = repositorio.encuentraPorNombreEmailOTelefonoNativa(cadena);
		return empleados;
	}

}
