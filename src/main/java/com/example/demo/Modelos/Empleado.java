package com.example.demo.Modelos;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//Para las validaciones a partir de spring boot 2.3 se usa jakarta en vez de javax(no solo para las validaciones, para otras muchas cosas más)
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//@Entity para decirle que es una tabla de la base de datos
@Entity(name="EMPLEADO")
public class Empleado implements Comparable<Empleado> {
	
	//atributos
	//@Id -> para decirle que esta es la PK de la tabla
	@Id @GeneratedValue
	//Validaciones
    @Min(value = 0, message = "{empleado.id.mayorquecero}")
	private long id;
	@Column(nullable = false)
    @NotNull
	@Size(min=5, max=100, message = "{empleado.id.nombre}")
	private String nombre;
	private String email;
	private String telefono;
	private String puesto;
	private String imagen;
	
	
	//constructores
	public Empleado() {
		
	}
	public Empleado(long id, String nombre, String email, String telefono, String puesto) {
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.telefono = telefono;
		this.puesto = puesto;
	}
	public Empleado(long id, String nombre, String email, String telefono, String puesto, String imagen) {
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.telefono = telefono;
		this.puesto = puesto;
		this.imagen = imagen;
	}
	//constructor sin id(ya que este va a ser autogenerado)
	public Empleado(String nombre, String email, String telefono, String puesto) {
		//this.id = 0;
		this.nombre = nombre;
		this.email = email;
		this.telefono = telefono;
		this.puesto = puesto;
	}
	
	
	//getters y setters
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public String getPuesto() {
		return puesto;
	}
	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}
	
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	
	//hashCode
	@Override
	public int hashCode() {
		return Objects.hash(email, id, imagen, nombre, puesto, telefono);
	}
	
	
	//equals
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Empleado other = (Empleado) obj;
		return Objects.equals(email, other.email) && id == other.id && Objects.equals(imagen, other.imagen)
				&& Objects.equals(nombre, other.nombre) && Objects.equals(puesto, other.puesto)
				&& Objects.equals(telefono, other.telefono);
	}
	
	
	//toString
	@Override
	public String toString() {
		return "Empleado [id=" + id + ", nombre=" + nombre + ", email=" + email + ", telefono=" + telefono + ", puesto="
				+ puesto + ", imagen=" + imagen + "]";
	}
	
	
	//Interfaz comparable, para ordenar por el id
	@Override
    public int compareTo(Empleado empleado) {
        int resultado = 0;
        if(this.id < empleado.id){
            resultado = -1;
        }
        else{
            if(this.id > empleado.id){
                resultado = 1;
            }
        }
        
        return resultado;
    }
}
