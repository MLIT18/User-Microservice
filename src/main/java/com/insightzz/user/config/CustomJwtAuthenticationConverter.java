package com.insightzz.user.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomJwtAuthenticationConverter
        implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {

        List<GrantedAuthority> authorities =
                new ArrayList<>();

        // =====================================================
        // ROLE
        // =====================================================

        String role = jwt.getClaimAsString("role");

        if (role != null && !role.isBlank()) {

            authorities.add(
                    new SimpleGrantedAuthority(
                            "ROLE_" + role.toUpperCase()
                    )
            );
        }


        // =====================================================
        // PERMISSIONS
        // =====================================================

        List<String> permissions =
                jwt.getClaimAsStringList("authorities");

        if (permissions != null) {

            for (String permission : permissions) {

                if (permission == null ||
                        permission.isBlank()) {
                    continue;
                }

                authorities.add(
                        new SimpleGrantedAuthority(
                                permission
                        )
                );
            }
        }

        return authorities;
    }
}