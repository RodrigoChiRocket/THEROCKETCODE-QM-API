package com.qualitas.portal.accountfraudesapi.configuracion.security;

import com.qualitas.portal.fraudes.account.application.service.UsuarioService;
import com.qualitas.portal.fraudes.account.domain.model.Usuario;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

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

        return new User(
                usuario.getvEmail(),
                usuario.getvPasswordHash(),
                true,  // enabled
                true,  // accountNonExpired
                true,  // credentialsNonExpired
                true,  // accountNonLocked
                Collections.emptyList() // No authorities needed
        );
    }
}