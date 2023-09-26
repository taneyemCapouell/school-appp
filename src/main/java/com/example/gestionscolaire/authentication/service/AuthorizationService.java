package com.example.gestionscolaire.authentication.service;


//import com.adeli.adelispringboot.Users.entity.Users;
//import com.adeli.adelispringboot.Users.repository.IUserRepo;
import com.example.gestionscolaire.Users.entity.Users;
import com.example.gestionscolaire.Users.repository.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorizationService implements IAuthorizationService {

	@Autowired
	private IUserRepo userRepo;


	@Override
	public Users getUserInContextApp() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}

		UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
		return userRepo.findById(currentUser.getId())
				.orElseThrow(() -> new ResourceNotFoundException("User " + currentUser.getUsername() + " not found"));
	}

	@Override
	public List<String> getUserRoles() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}
		UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
		return  currentUser.getRoles().stream()
					.map(item -> item.getAuthority())
					.collect(Collectors.toList());
	}

	public boolean canUpdateOwnerItem(Long itemId, String item) {
		boolean authorized = false;
		Users currentUser = getUserInContextApp();
		switch (item) {
		case "User":
			if (currentUser.getUserId().equals(itemId)) {
				authorized = true;
			}
			break;

		default:
			break;
		}
		return authorized;
	}
}
