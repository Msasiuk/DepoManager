package com.depomanager.auth.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDTO {

	private String alias;
	private String password;
}
