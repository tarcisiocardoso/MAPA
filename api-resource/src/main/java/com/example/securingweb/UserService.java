package com.example.securingweb;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.WebServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.example.securingweb.model.JAASApplication;
import com.example.securingweb.model.UserVO;
import com.example.securingweb.repository.MediatorApplication;
import com.example.securingweb.repository.MediatorRole;
import com.example.securingweb.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository repository;
	
	@Autowired
	MediatorRole mediatorRole;
	
	@Autowired
	MediatorApplication mediatorApplication;
	

	
	public UserVO getUser(Principal principal) {
		UserVO userVo = null;
		try {
			System.out.println("Principal no me: "+principal.getName());
			String split[] =  principal.getName().split(":");
			String id = split[0];
			
			boolean autenticadoComLoginUnico = false;
			List<Principal> principals = new ArrayList<Principal>();
			
			Authentication aut = SecurityContextHolder.getContext().getAuthentication();
			
	    	
			Jwt jwt = (Jwt)aut.getCredentials();
			
			List<String> aplicacoes = (List<String>)jwt.getClaims().get("aud");
			
			
			userVo = repository.getUser(id);
			String[] idsAplicacoes = (String[])aplicacoes.toArray(new String[0]);
			List<JAASApplication> apps = mediatorApplication.getAplicacoesUsuario(id, idsAplicacoes, autenticadoComLoginUnico, principals);
//			mediatorRole.getModulosUsuario(idUsuario, idsGrupos, principals)
			
			userVo.apps = apps;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebServiceException(e.getMessage());
			
		}
		
		return userVo;
	}
}
