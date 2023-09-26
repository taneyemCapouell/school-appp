package com.example.gestionscolaire.Users.service;


import com.example.gestionscolaire.Users.dto.ResponseUsersDTO;
import com.example.gestionscolaire.Users.dto.UserEditPasswordDto;
import com.example.gestionscolaire.Users.dto.UserResDto;
import com.example.gestionscolaire.Users.entity.Users;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface IUserService {

	Map<String, Object> add(Users user);
//	UserResDto addUser(UserReqDto userReqDto);
	Map<String, Object> modify(Users user);

	boolean existsByEmail(String email, Long id);

	boolean existsByPinCode(int code);
	
	boolean existsByInternalReference(Long internalReference);
	
	Users getById(Long id);

	Users getByInternalReference(Long internalReference);

	Optional<Users> getByTelephone(String tel);

	Optional<Users> getByEmail(String email);

	Optional<Users> getByPinCode(int pinCode);
	
	Users editToken(Long id, String token);
	
	Users editEmail(Long id, String email);

	
	Users editStatus(Long id, Long statusID);
	
	String editPassword(Users user, UserEditPasswordDto userEditPasswordDto);

	boolean existsByTelephone(String tel, Long id);

	Users editProfil(Users user, Long updateRoleId, Long addRoleId);

	void deleteUser(Long userId);

	Users updateAuthToken(Long id, String token);

	Users updateOtpCode(Long id, String code);

	Users editUser(Long userId, Users u);

	void editTokenFcm(Long userId, String tokenFcm);

	Users getByOtpCode(String code);

	Users getNewCodeVerificationAccount(String login);

	Users checkUserAndGenerateCode(String login);

	Users resetPassword(Users user, String password);

	Optional<Users> getUserByUniqueConstraints(Users u);

	Page<ResponseUsersDTO> getUsers(int page, int size, String sort, String order);

	Page<UserResDto> getAllUsers(int page, int size, String sort, String order);

	Page<Users> get20Users(int parseInt, int parseInt2, String sort, String order);

	List<Users> getUsers();

	void updateDateLastLoginUser(Long id_user);

	void updateFistLogin(Long user_id);

	Users lockAndUnlockUsers(Long id_user, boolean status);
}
