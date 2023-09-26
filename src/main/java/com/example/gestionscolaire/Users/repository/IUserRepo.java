package com.example.gestionscolaire.Users.repository;

import com.example.gestionscolaire.Users.entity.RoleUser;
import com.example.gestionscolaire.Users.entity.StatusUser;
import com.example.gestionscolaire.Users.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IUserRepo extends IUserBaseRepo<Users> {

	Optional<Users> findByEmail(String email);
	
	Optional<Users> findByTelephone(String tel);

	boolean existsByEmail(String email);
	boolean existsByTelephone(String email);

	Page<Users> findDistinctByRolesIn(List<RoleUser> rolesManagers, Pageable p);

//	Optional<Users> findByOtpCode(String code);

	Optional<Users> findByTokenAuth(String tokenAuth);

	Page<Users> findTop20ByStatus(StatusUser status, Pageable p);


}
