package com.depomanager.model.ModelLogin;

import com.depomanager.model.Usuario;
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
