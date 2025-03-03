package com.depomanager.usuario.models;

import com.depomanager.usuario.Roles;
import com.depomanager.utilidades.models.Fechas;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario extends Fechas{

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Getter @Setter
    @Column(name = "alias", length = 50, nullable = false)
    private String alias;

    @Getter @Setter
    @Column(name = "contrasenia", length = 255, nullable = false)
    private String contrasenia;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Set<Roles> roles;


    public Set<Roles> getRoles() {
        if (roles == null || roles.isEmpty()) {
            roles = new HashSet<>();
            roles.add(Roles.USER);
        }
        return roles;
    }
    public void setRoles(Set<Roles> roles) {
        this.roles = roles;
    }



    }




