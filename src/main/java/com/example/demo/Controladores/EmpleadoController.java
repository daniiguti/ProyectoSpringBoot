package com.example.demo.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import com.example.demo.Modelos.Empleado;
import com.example.demo.Servicios.EmpleadoServiceDB;
import com.example.demo.upload.storage.StorageService;

import java.util.List;

import javax.validation.Valid;

@Controller
public class EmpleadoController {
	private int contador = 0;
	
	//el autowired lo necesita, puesto que es una clase ajena a esta, por lo que la "inyectamos"
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private EmpleadoServiceDB servicio;
	
	/*
	@GetMapping("/")
	public String inicio(Model model){
		return "index";
	}*/
	
	//las {} son para decirle, que la ruta puede ser: / o /empleado/list
	@GetMapping({"/", "empleado/list"}) // como se llama lo que vamos a obtener desde list.html
										//en este caso, list.html va a devolver la consulta de Buscar
	public String listado(Model model, @RequestParam(name="q", required=false, defaultValue="no hay consulta") String consulta){
		
		List<Empleado> empleados;
		//si en la consulta no hay nada, obtenemos todos los empleados
		if(consulta.equals("no hay consulta")) {
			empleados = servicio.findAll();
		//si en la consulta había algo	
		}else {
			//usamos un metodo, para obtener aquellos empleados según la consulta
			empleados = servicio.buscador(consulta);
		}
		
		//enviamos a list.html, una clave -> listaEmpleados, valor -> los empleados
		model.addAttribute("listaEmpleados", empleados);
		
		if(this.contador == 0) {
			this.contador++;
			//para inicializar la carpeta donde guardamos los recursos(fotos)
			storageService.init();
		}
		
		//devolvemos la vista
		return "list";
	}
	
	/**
	 * aquí se viene cuando se pulsa Nuevo Empleado en list.html
	 * metodo para dar de alta un nuevo empleado y mandar al usuario a la ventana de form.html(por eso el @GetMapping, pq es antes)
	 * @param model
	 * @return
	 */
	@GetMapping("/empleado/new")
	public String nuevoEmpleado(Model model) {
		//le pasamos al form.html una clave -> empleadoForm, valor -> el empleado vacío, que rellenaremos en form.html
		model.addAttribute("empleadoForm", new Empleado());
		//devolvemos la vista
		return "form";
	}
	
	/**
	 * aquí se viene cuando le hemos dado al boton de Submit en form.html (por eso el @PostMaping, pq es despues)
	 * @param nuevoEmpleado -> el empleado con sus atributos, nos lo devuelve form.html
	 * @param bindingResult -> validaciones(para comprobar si se introdujeron correctas los atributos del empleado)
	 * @param file -> avatar que puede subir el usuario
	 * @return
	 */
	@PostMapping("/empleado/new/submit")
	public String nuevoEmpleadoSubmit(@Valid @ModelAttribute("empleadoForm") Empleado nuevoEmpleado, BindingResult bindingResult, 
			//una vez que hemos añadido el empleado, obtenemos ese archivo así
			//file es como hemos llamado al campo en el formulario(name = "file")
			@RequestParam("file") MultipartFile file) {
		String sitio = "";
		//si el empleado introducido tiene errores, lo mandamos de nuevo al form.html, para que los corrija
		if(bindingResult.hasErrors()) {
			sitio = "form";
		}
		//sino tiene erores
		else {
			//si subio una imagen como avatar
			if(file.isEmpty() == false) {
				//Lógica de almacenamiento de fichero
				//utilizamos el método store(que almacena el fichero y además le cambia el nombre) que nos devuelve el nuevo nombre del fichero ya almacenado
				String avatar = storageService.store(file, nuevoEmpleado.getId());
				//vamos a ponerle al empleado la imagen de la siguiente forma:
				nuevoEmpleado.setImagen(
						MvcUriComponentsBuilder. //Nos permite crear una uri
						fromMethodName //la hace usando un método, en nuestro caso anotado con la ruta /files/filename
						(EmpleadoController.class, "serveFile", avatar).build().toUriString()   //llamamos al metodo que tenemos hecho más abajo(serveFile)
																																	  //y le pasamos como parámetro a ese metodo el nombre de como
																																	  //se guardó la imagen, este metodo nos devolverá esa imagen,
																																	  //como recurso
																																	  //y además usaremos todo eso para construit la uri(que es un identificador único de un recurso en la web)
						);
				System.out.println("uri: " + MvcUriComponentsBuilder.fromMethodName(EmpleadoController.class, "serveFile", avatar).build().toUriString());
			}
			//añadimos ese empleado a nuestra base de datos
			servicio.add(nuevoEmpleado);
			//enviamos al usuario a la pagina donde salen todos los empleados
			sitio = "redirect:/empleado/list";
		}
		return sitio;
	}
	
	/**
	 * Aqui se viene cuando pulsa el boton de Editar en list.html
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/empleado/edit/{id}")
	//                    obtenemos la id, que se la pasamos desde list.html, y la obtenemos aquí para saber que empleado hay que editar
	public String editarEmpleado(@PathVariable long id, Model model) {
		String sitio = "";
		//obtenemos el empleado con una consulta de la bd, a través del id
		Empleado aux = servicio.findById(id);
		if(aux != null) {
			//se lo ponemos como un atributo a form.html para que se puedan visualizar los datos del empleado en form.html
			model.addAttribute("empleadoForm", aux);
			//enviamos al usuario a form.html
			sitio = "form";
		}
		else {
			sitio = "redirect:/empleado/new";
		}
		return sitio;
	}
		
	/**
	 * aqui se viene cuando se pulsa el boton de Enviar en form.html(cuando viene de edit)
	 * @param empleado -> el empleado editado
	 * @param bindingResult -> validaciones
	 * @param file -> nuevo archivo como avatar
	 * @return
	 */
	@PostMapping("/empleado/edit/submit")
	public String editarEmpleadoSubmit(@Valid @ModelAttribute("empleadoForm") Empleado empleado, BindingResult bindingResult,
			@RequestParam("file") MultipartFile file) {
		//hacemos lo mismo que en guardar
		String sitio = "";
		if(bindingResult.hasErrors()) {
			sitio = "form";
		}
		else {
			if(file.isEmpty() == false) {
				
				String avatar = storageService.store(file, empleado.getId());
				
				empleado.setImagen(
						MvcUriComponentsBuilder. 
						fromMethodName 
						(EmpleadoController.class, "serveFile", avatar).build().toUriString());
			}	
			servicio.edit(empleado);
			sitio = "redirect:/empleado/list";
		}
		return sitio;
	}
	
	/**
	 * esto es para obtener un recurso, en este caso la imagen del avatar, por eso el @ResponseBody
	 * @param filename
	 * @return
	 */
	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename){
		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok().body(file);
	}
	
}
