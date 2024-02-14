package app.project.ProjectSI.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "usuario", uniqueConstraints = {
        @UniqueConstraint(columnNames = "nombre usuario"),
        @UniqueConstraint(columnNames = "email")
})
public class Usuario implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 40)
    @Column(name = "nombre usuario")
    private String username;
    @Email
    @NotBlank
    @Size(max = 60)
    @Column(name = "email")
    private String email;
    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "fecha nacimiento")
    private LocalDate fechaNacimiento;
    @NotBlank
    @Column(name = "password")
    private String password;
    @Column(name = "telefono")
    private Long telefono;
    @ElementCollection
    @CollectionTable(name = "contactos", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "contacto_id")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Long> contactosIds = new HashSet<>();
    @ElementCollection
    @CollectionTable(name = "seguidos", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "seguido_id")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Long> seguidosIds = new HashSet<>();
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Mensajes> mensajes = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    @Column(name = "rol")
    private Roles rol;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority((rol.name())));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
