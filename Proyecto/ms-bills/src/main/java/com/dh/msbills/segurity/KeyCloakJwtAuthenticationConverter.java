
package com.dh.msbills.segurity;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class KeyCloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
  private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

  public KeyCloakJwtAuthenticationConverter() {
  }

  public AbstractAuthenticationToken convert(final Jwt source) {
    Collection<GrantedAuthority> authorities = null;
    try {
      authorities = this.getGrantedAuthorities(source);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return new JwtAuthenticationToken(source, authorities);
  }

  public Collection<GrantedAuthority> getGrantedAuthorities(Jwt source) throws JsonProcessingException {
    Set<GrantedAuthority> resourcesRoles = new HashSet<>();
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    JsonNode jwtClaims = objectMapper.readTree(objectMapper.writeValueAsString(source));

    resourcesRoles.addAll(extractRoles("resource_access", jwtClaims.get("claims"), objectMapper));
    resourcesRoles.addAll(extractRolesRealmAccess("realm_access", jwtClaims.get("claims"), objectMapper));
    resourcesRoles.addAll(extractAud("aud", jwtClaims.get("claims"), objectMapper));

    return Stream.concat(this.defaultGrantedAuthoritiesConverter.convert(source).stream(), resourcesRoles.stream())
            .collect(Collectors.toSet());
  }

  private List<GrantedAuthority> extractRoles(String route, JsonNode jwt, ObjectMapper objectMapper) {
    Set<String> rolesWithPrefix = new HashSet<>();

    jwt.path(route)
            .elements()
            .forEachRemaining(e -> e.path("roles")
                    .elements()
                    .forEachRemaining(r -> rolesWithPrefix.add("ROLE_" + r.asText())));

    return AuthorityUtils.createAuthorityList(rolesWithPrefix.toArray(new String[0]));
  }

  private List<GrantedAuthority> extractRolesRealmAccess(String route, JsonNode jwt, ObjectMapper objectMapper) {
    Set<String> rolesWithPrefix = new HashSet<>();

    jwt.path(route)
            .path("roles")
            .elements()
            .forEachRemaining(r -> rolesWithPrefix.add("ROLE_" + r.asText()));

    return AuthorityUtils.createAuthorityList(rolesWithPrefix.toArray(new String[0]));
  }

  private List<GrantedAuthority> extractAud(String route, JsonNode jwt, ObjectMapper objectMapper) {
    Set<String> rolesWithPrefix = new HashSet<>();

    jwt.path(route)
            .elements()
            .forEachRemaining(e -> rolesWithPrefix.add("AUD_" + e.asText()));

    return AuthorityUtils.createAuthorityList(rolesWithPrefix.toArray(new String[0]));
  }
}
