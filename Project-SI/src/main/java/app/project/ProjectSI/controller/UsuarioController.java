package app.project.ProjectSI.controller;

import app.project.ProjectSI.model.Usuario;
import app.project.ProjectSI.request.SecurityDTO;
import app.project.ProjectSI.request.UsuarioDTO;
import app.project.ProjectSI.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    //Usuariol logeado.
    @GetMapping("/get/")
    public ResponseEntity<UsuarioDTO> get_usuario_controller() {
        UsuarioDTO usuario = usuarioService.get_usuario_service();
        return ResponseEntity.ok(usuario);
    }

    //Buscar usuario por su username.
    @GetMapping("/find/{username}")
    public ResponseEntity<UsuarioDTO> find_username_controller(@PathVariable String username) {
        UsuarioDTO nameUsuario = usuarioService.find_usuario_service(username);
        return ResponseEntity.ok(nameUsuario);
    }

    //Crear usuario.
    @PostMapping("/create")
    public ResponseEntity<Usuario> create_usuario_controller(@RequestBody Usuario usuario) {
        Usuario newUsuario = usuarioService.create_usuario_service(usuario);
        return ResponseEntity.ok(newUsuario);
    }

    //Actualizar usuario.
    @PutMapping ("/update")
    public ResponseEntity<UsuarioDTO> update_usuario_controller(@RequestBody Usuario usuario) {
        UsuarioDTO updateUsuario = usuarioService.update_usuario_service(usuario);
        return ResponseEntity.ok(updateUsuario);
    }

    //Actualizar contraseña.
    @PutMapping ("/update/password")
    public ResponseEntity<SecurityDTO> update_password_controller(@RequestBody SecurityDTO security) {
        SecurityDTO updateSecurity = usuarioService.update_password_service(security);
        return ResponseEntity.ok(updateSecurity);
    }

    //Eliminar cuenta.
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete_usuario_controller() {
        String deleteUsuario = usuarioService.delete_usuario_service();
        return ResponseEntity.ok(deleteUsuario);
    }

    //Agregar usuario/contacto.
    @PostMapping("/add/contacto/{contactoID}")
    public ResponseEntity<String> add_contacto_controller(@PathVariable Long contactoID) {
        String contacto = usuarioService.add_contacto_service(contactoID);
        return ResponseEntity.ok(contacto);
    }

    //Obtiene todos los contactos del usuario.
    @GetMapping("/get/contactos")
    public ResponseEntity<Set<UsuarioDTO>> get_contactos_controller() {
        Set<UsuarioDTO> contactos = usuarioService.get_all_contactos_service();
        return ResponseEntity.ok(contactos);
    }

    //Eliminar un contacto.
    @DeleteMapping("/delete/contacto/{contactoID}")
    public ResponseEntity<String> delete_contacto_controller(@PathVariable Long contactoID) {
        String deleteContacto = usuarioService.delete_contacto_service(contactoID);
        return ResponseEntity.ok(deleteContacto);
    }

    //Seguir a un usuario.
    @PostMapping("/add/seguido/{seguidoID}")
    public ResponseEntity<String> add_seguido_controller(@PathVariable Long seguidoID) {
        String seguido = usuarioService.add_seguido_service(seguidoID);
        return ResponseEntity.ok(seguido);
    }

    //Obtiene todos los usuarios seguidos de un usuario.
    @GetMapping("/get/seguidos")
    public ResponseEntity<Set<UsuarioDTO>> get_seguidos_controller() {
        Set<UsuarioDTO> seguidos = usuarioService.get_all_seguidos_service();
        return ResponseEntity.ok(seguidos);
    }

    //Eliminar un seguido.
    @DeleteMapping("/delete/seguido/{seguidoID}")
    public ResponseEntity<String> delete_seguido_controller(@PathVariable Long seguidoID) {
        String deleteSeguido = usuarioService.delete_seguido_service(seguidoID);
        return ResponseEntity.ok(deleteSeguido);
    }
}
