package com.depomanager.jwt;



import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.depomanager.model.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {
	

    private static String secretKey = "MI_CLAVE_SECRETA_SEGURA";

    // Generar el token
    public static String generarToken(Usuario user) {
        return Jwts.builder()
                .setSubject(user.getAlias())
                .claim("roles", user.getRoles().stream().map(Enum::name).collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora de expiración
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Obtener los roles desde el token
    public List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        // Verificar que el claim "roles" sea una lista
        Object rolesObject = claims.get("roles");
        if (rolesObject instanceof List<?>) {
            return (List<String>) rolesObject; 
        } else {
            throw new IllegalArgumentException("El claim 'roles' no es una lista válida");
        }
    }

    // Validar el token
    public static Claims validarToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
    
    
    public String limpiarToken(String token) {
    	if (token.startsWith("Bearer")) {
            token = token.substring(7);
        }
        return null;
    }
    
   
    
    public boolean estadoToken(String token) {
    		Claims claims= Jwts.parser()
    				.parseClaimsJws(token)
    				.getBody();
    		
    		Date expiration=claims.getExpiration();
    		
    		return expiration.before(new Date());
    		
    		
    }
    
}
