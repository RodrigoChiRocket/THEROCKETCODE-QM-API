package com.qualitas.portal.accountfraudesapi.configuracion.security;

import com.qualitas.portal.fraudes.account.application.dto.response.RolRespuestaDto;
import com.qualitas.portal.fraudes.account.application.service.RolService;
import com.qualitas.portal.fraudes.account.application.service.UsuarioService;
import com.qualitas.portal.fraudes.account.domain.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.obtenerUsuarioPorEmail(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("User not found");
        }

        RolRespuestaDto rol = rolService.obtenerRol(usuario.getiRolClav());
        if (rol == null) {
            throw new UsernameNotFoundException("Role not found for user");
        }

        List<GrantedAuthority> authorities = rol.getPermisos().stream()
                .map(SimpleGrantedAuthority::new) // Cada permiso se convierte en una autoridad
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + rol.getvRolNombre()));

        return new User(usuario.getvEmail(), usuario.getvContrasena(), authorities);
    }
}

