package com.example.demo.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.Modelos.Empleado;
//Interfaz que extiende de la interfaz que tiene los metodos que gestionan la bdd	
											//Tipo de la tabla bdd, tipo de la PK de esa tabla
public interface EmpleadoRepository extends JpaRepository<Empleado, Long>{
	
	//buscador, le llegará cualquier cosa(nombre, email, telefono) y el buscara por esa cosa en los tres campos
	List<Empleado> findByNombreContainsIgnoreCaseOrEmailContainsIgnoreCaseOrTelefonoContainsIgnoreCase(String nombre, String email, String telefono);

	//busca en empleado donde en minuscula el nombre es igual a algo_primer parametro_algo, etc, etc
	@Query("select e from EMPLEADO e where e.nombre like %?1% or e.email like %?1% or e.telefono like %?1%")
	List<Empleado> encuentraPorNombreEmailOTelefono(String cadena);
	
	//uso de consulta antiva
	//            SELECT TODOS LOS EMPLEADOS DONDE EL NOMBRE SEA IGUAL A ALGO + PRIMER PARAMETRO DEL METODO + ALGO  O EL EMAIL SEA IGUAL, etc
	@Query(value="SELECT * FROM EMPLEADO WHERE NOMBRE LIKE CONCAT('%',?1,'%') OR EMAIL LIKE CONCAT('%',?1,'%') OR TELEFONO LIKE CONCAT('%',?1,'%')", nativeQuery=true)
	List<Empleado> encuentraPorNombreEmailOTelefonoNativa(String cadena);
}


