package com.salvalinks.services;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

@Service
public class Util {
	public boolean validaSenha(String senha) {
		boolean status = true;

		if (senha.length() < 6)
			status = false;
		return status;
	}

	public boolean verificaSenha(String senhaUsuario, String senhaInformada) {
		boolean status = false;
		String senhaUsuarioDes = criptografar(senhaInformada);
		if (senhaUsuario.equals(senhaUsuarioDes))
			status = true;
		return status;
	}

	public String criptografar(String senha) {
		return Base64.encodeBase64String(senha.getBytes());
	}
}
