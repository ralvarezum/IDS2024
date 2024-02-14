/*Este controlador maneja las solicitudes de autenticación de usuarios, como iniciar sesión, 
registrarse y cerrar sesión, utilizando los servicios proporcionados por AuthService.*/

package app.project.ProjectSI.controller;

import app.project.ProjectSI.auth.AuthResponse;
import app.project.ProjectSI.auth.LoginRequest;
import app.project.ProjectSI.auth.RegisterRequest;
import app.project.ProjectSI.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /*Se manejan las solicitudes POST en la ruta "/auth/login".
    Este método toma un objeto LoginRequest como parámetro de la solicitud y devuelve una respuesta de tipo AuthResponse.*/
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    /*Se manejan las solicitudes POST en la ruta "/auth/register".
    Toma un objeto RegisterRequest como parámetro y devuelve una respuesta de tipo AuthResponse.*/
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    /* Se manejan las solicitudes POST en la ruta "/auth/logout".
    Toma un objeto HttpServletRequest como parámetro. Devuelve una respuesta exitosa con el mensaje "Logged out successfully.".*/
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // Realiza cualquier tarea de limpieza necesaria en el servidor

        // Retorna una respuesta exitosa
        return ResponseEntity.ok("Logged out successfully.");
    }

}
