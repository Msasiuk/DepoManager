package com.depomanager.auth.dtos;

import com.depomanager.usuario.models.Usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioGeneralDTO {

	private Long id;
	private String alias;
	
	public UsuarioGeneralDTO(Usuario usuario) {
		this.id=usuario.getId();
		this.alias=usuario.getAlias();
	}
}
