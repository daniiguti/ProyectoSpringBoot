package com.example.demo.Servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import org.springframework.stereotype.Service;

import com.example.demo.Modelos.Empleado;

import javax.annotation.PostConstruct;

//esta clase no la utilizamos, es para hacer pruebas y guardar los datos en memoria en vez de en una bdd
@Service("empleadoServiceMemory")
public class EmpleadoServiceMemory {
	
	private List<Empleado> repositorio = new ArrayList<>();
	
	public Empleado add(Empleado e) {
		repositorio.add(e);
		return e;
	}
	
	public void ordenarPorID() {
		Collections.sort(repositorio);
	}
	
	public List<Empleado> findAll() {
		return repositorio;
	}
	
	public 	Empleado findById(long id) {
		Empleado buscado = null;
		boolean salir = false;
		
		for(int i = 0; i < this.repositorio.size() && salir == false; i++){
			if(this.repositorio.get(i).getId() == id) {
				buscado = this.repositorio.get(i);
				salir = true;
			}
		}
		
		return buscado;
	}
	
	public Empleado edit(Empleado e) {
		boolean salir = false;
		for(int i = 0; i < this.repositorio.size() && salir == false; i++){
			if(this.repositorio.get(i).getId() == e.getId()) {
				this.repositorio.remove(i);
				this.repositorio.add(i, e);
				salir = true;
			}
		}
		//si salir es = false significa que ese empleado no estaba dentro, por lo que hay que añadirlo
		if(salir == false) {
			this.repositorio.add(e);
		}
		return e;
	}	
	
	@PostConstruct
	public void init() {
		repositorio.addAll(
				Arrays.asList(new Empleado(3,"Antonio García", "antonio.garcia@openwebinars.net", "954000000", "Jefe"),
						new Empleado(1,"María López", "maria.lopez@openwebinars.net", "954000000", "Programador Junior"),
						new Empleado(2,"Ángel Antúnez", "angel.antunez@openwebinars.net", "954000000", "Talent Recluiter")						
						)
				);
	}

}
