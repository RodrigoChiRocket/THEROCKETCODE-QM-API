package com.qualitas.portal.accountfraudesapi.configuracion.security;

import com.qualitas.portal.fraudes.account.application.service.UsuarioService;
import com.qualitas.portal.fraudes.account.domain.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;

    public CustomUserDetailsService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.obtenerUsuarioPorEmail(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con email: " + username);
        }

        // Normalización del rol (asegura que tenga el prefijo ROLE_)
        String rol = usuarioService.obtenerRolUsuario(usuario.getiIdUsuario().longValue());
        String rolNormalizado = rol.startsWith("ROLE_") ? rol : "ROLE_" + rol;

        // Creación de la lista de authorities (usando Collections.singletonList para mejor performance)
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(rolNormalizado)
        );

        return new User(
                usuario.getvEmail(),
                usuario.getvPasswordHash(),
                true,  // enabled
                true,  // accountNonExpired
                true,  // credentialsNonExpired
                true,  // accountNonLocked
                authorities
        );
    }
}