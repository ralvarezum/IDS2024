package app.project.ProjectSI.service;

import app.project.ProjectSI.model.MensajeMuro;
import app.project.ProjectSI.model.Usuario;
import app.project.ProjectSI.repo.IMensajeMuro;
import app.project.ProjectSI.repo.IUsuarioRepo;
import app.project.ProjectSI.request.MensajeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

// Servicio con métodos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) en mensajes del muro.
@Service
public class MensajeMuroService {
    private final IMensajeMuro mensajeMuroRepo;
    private final IUsuarioRepo usuarioRepo;

    @Autowired
    public MensajeMuroService(IMensajeMuro mensajeMuroRepo, IUsuarioRepo usuarioRepo) {
        this.mensajeMuroRepo = mensajeMuroRepo;
        this.usuarioRepo = usuarioRepo;
    }

    /* Obtiene el usuario autenticado del contexto de seguridad (SecurityContextHolder) y asigna este usuario al mensaje antes de guardarlo.
     Luego, crea y devuelve un DTO (MensajeDTO) que representa el mensaje creado. */
    public MensajeDTO create_mensajeMuro_service(MensajeMuro mensajeMuro){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioActual = (Usuario) authentication.getPrincipal();
        Usuario usuarioConContactos = usuarioRepo.findWithContactosById(usuarioActual.getId()).orElseThrow();
        mensajeMuro.setUsuario(usuarioConContactos);
        mensajeMuro.setFecha(Instant.now());
        mensajeMuroRepo.save(mensajeMuro);

        MensajeDTO mensajeDTO = new MensajeDTO();
        mensajeDTO.setUsername(mensajeMuro.getUsuario().getUsername());
        mensajeDTO.setFecha(mensajeMuro.getFecha());
        mensajeDTO.setMensaje(mensajeMuro.getMensaje());
        mensajeDTO.setTags(mensajeMuro.getTags());
        return mensajeDTO;
    }

    /* Obtiene el usuario autenticado, busca el mensaje existente por su ID, actualiza sus campos con los nuevos valores y lo guarda.
     Devuelve un DTO representando el mensaje actualizado. */
    public MensajeDTO update_mensajeMuro_service(Long id, MensajeMuro mensajeMuro){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioActual = (Usuario) authentication.getPrincipal();
        Usuario usuarioConContactos = usuarioRepo.findWithContactosById(usuarioActual.getId()).orElseThrow();
        MensajeMuro mensajeMuroExistente = mensajeMuroRepo.findById(id).orElseThrow( ()-> new NoSuchElementException("Mensaje no encontrado"));
        mensajeMuroExistente.setMensaje(mensajeMuro.getMensaje());
        mensajeMuroExistente.setFecha(Instant.now());
        mensajeMuroExistente.setUsuario(usuarioConContactos);
        mensajeMuroExistente.setTags(mensajeMuro.getTags());
        mensajeMuroRepo.save(mensajeMuroExistente);

        MensajeDTO mensajeDTO = new MensajeDTO();
        mensajeDTO.setUsername(mensajeMuroExistente.getUsuario().getUsername());
        mensajeDTO.setFecha(mensajeMuroExistente.getFecha());
        mensajeDTO.setMensaje(mensajeMuroExistente.getMensaje());
        mensajeDTO.setTags(mensajeMuroExistente.getTags());
        return mensajeDTO;
    }

    /* Elimina un mensaje del muro por su ID.  */
    public String delete_mensajeMuro_service(Long id){
        mensajeMuroRepo.deleteById(id);
        return "Mensaje eliminado";
    }

    /* Busca el mensaje en la base de datos por su ID y devuelve un DTO representando el mensaje encontrado. */
    public MensajeDTO get_MensajeMuro_service(Long id){
        MensajeDTO mensajeDTO = new MensajeDTO();
        MensajeMuro mensajeMuro = mensajeMuroRepo.findById(id).orElseThrow( ()-> new NoSuchElementException("Mensaje no encontrado"));
        mensajeDTO.setId(mensajeMuro.getId());
        mensajeDTO.setUsername(mensajeMuro.getUsuario().getUsername());
        mensajeDTO.setMensaje(mensajeMuro.getMensaje());
        mensajeDTO.setFecha(mensajeMuro.getFecha());
        mensajeDTO.setTags(mensajeMuro.getTags());
        mensajeDTO.setIdUsuario(mensajeMuro.getUsuario().getId());
        return mensajeDTO;
    }

    /* Utiliza el ID del usuario autenticado para buscar los mensajes asociados a ese usuario 
    y devuelve un conjunto de DTOs representando los mensajes encontrados. */
    public Set<MensajeDTO> get_MensajesMuro_service(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioActual = (Usuario) authentication.getPrincipal();

        return mensajeMuroRepo.findAllByUsuarioId(usuarioActual.getId()).stream()
                .map(mensaje -> {
                    MensajeDTO mensajeDTO = new MensajeDTO();
                    mensajeDTO.setUsername(mensaje.getUsuario().getUsername());
                    mensajeDTO.setFecha(mensaje.getFecha());
                    mensajeDTO.setMensaje(mensaje.getMensaje());
                    mensajeDTO.setTags(mensaje.getTags());
                    return mensajeDTO;
                })
                .collect(Collectors.toSet());
    }

    /* Obtiene los IDs de los usuarios seguidos por el usuario autenticado, busca los mensajes asociados a esos usuarios
     y devuelve un conjunto de DTOs representando los mensajes encontrados. */
    public Set<MensajeDTO> get_all_MensajeMuro_service(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioActual = (Usuario) authentication.getPrincipal();

        // Cargar explícitamente los usuarios seguidos
        Usuario usuarioConSeguidos = usuarioRepo.findWithSeguidosById(usuarioActual.getId()).orElseThrow();
        Set<Long> usuariosSeguidosIds = new HashSet<>(usuarioConSeguidos.getSeguidosIds());
        usuariosSeguidosIds.add(usuarioActual.getId()); // Agregar el ID del usuario autenticado

        Set<MensajeDTO> mensajesDTO = usuariosSeguidosIds.stream()
                .flatMap(id -> mensajeMuroRepo.findAllByUsuarioId(id).stream())
                .map(mensaje -> {
                    MensajeDTO mensajeDTO = new MensajeDTO();
                    mensajeDTO.setId(mensaje.getId());
                    mensajeDTO.setUsername(mensaje.getUsuario().getUsername());
                    mensajeDTO.setFecha(mensaje.getFecha());
                    mensajeDTO.setMensaje(mensaje.getMensaje());
                    mensajeDTO.setTags(mensaje.getTags());
                    mensajeDTO.setIdUsuario(mensaje.getUsuario().getId());
                    return mensajeDTO;
                })
                .collect(Collectors.toSet());

        return mensajesDTO;
}
}
