package com.example.gestionscolaire.authentication.service;

//import com.adeli.adelispringboot.Users.entity.Users;

import com.example.gestionscolaire.Users.entity.Users;

import java.util.List;

public interface IAuthorizationService {

	Users getUserInContextApp();


	List<String> getUserRoles();

}
