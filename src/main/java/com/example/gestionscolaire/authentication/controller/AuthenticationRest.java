package com.example.gestionscolaire.authentication.controller;


import com.example.gestionscolaire.Users.dto.*;
import com.example.gestionscolaire.Users.entity.*;
import com.example.gestionscolaire.Users.repository.IRoleUserRepo;
//import com.example.gestionscolaire.Users.repository.ITypeAccountRepository;
import com.example.gestionscolaire.Users.repository.IUserRepo;
import com.example.gestionscolaire.Users.service.IUserService;
import com.example.gestionscolaire.authentication.dto.*;
import com.example.gestionscolaire.authentication.service.IAuthorizationService;
import com.example.gestionscolaire.authentication.service.JwtUtils;
import com.example.gestionscolaire.authentication.service.UserDetailsImpl;
import com.example.gestionscolaire.configuration.globalCoonfig.email.dto.EmailDto;
import com.example.gestionscolaire.configuration.globalCoonfig.email.service.IEmailService;
import com.example.gestionscolaire.configuration.globalCoonfig.globalConfiguration.ApplicationConstant;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@OpenAPIDefinition(info = @Info(title = "API ADELI V1.0", description = "Documentation de l'API", version = "2.0"))
@RestController
@Tag(name = "authentification")
@RequestMapping("/api/auth")
@Slf4j
//@RequiredArgsConstructor
public class AuthenticationRest {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private IRoleUserRepo roleRepo;

//    @Autowired
//    ITypeAccountRepository typeAccountRepo;

    @Autowired
    IEmailService emailService;

    @Autowired
    IAuthorizationService authorizationService;


    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Value("${app.front-reset-password-page}")
    String urlResetPasswordPage;
    @Value("${app.api-confirm-account-url}")
    String urlConfirmAccount;
    @Value("${app.front-singIn}")
    String signInUrl;
    @Value("${app.api-confirm-code-url}")
    String urlConfirmCode;
    @Value("${mail.from[0]}")
    String mailFrom;
    @Value("${mail.replyTo[0]}")
    String mailReplyTo;


//    @Parameters(@Parameter(name = "tel", required = true))
//    @Operation(summary = "Confirmation du login pour activation du compte", tags = "authentification", responses = {
//            @ApiResponse(responseCode = "200", description = "Code vérifié avec succès", content = @Content(mediaType = "Application/Json", array = @ArraySchema(schema = @Schema(implementation = UserResDto.class)))),
//            @ApiResponse(responseCode = "404", description = "Erreur: Utilisateur inexistant", content = @Content(mediaType = "Application/Json")),
//            @ApiResponse(responseCode = "401", description = "Erreur: Identification requise / Login déjà confirmé", content = @Content(mediaType = "Application/Json")),})
//
//    @GetMapping("/confirm-account")
//    public ResponseEntity<Object> confirmUserAccount(@RequestParam String code,
//                                                     @RequestParam(required = false) String email) {
//        Users user;
//        String newUrl = null;
//        if (code.length() == 6) {
//            user = userService.getByEmail(email).orElseThrow(() -> new ResourceNotFoundException(
//                    messageSource.getMessage("messages.user_not_found", null, LocaleContextHolder.getLocale())));
//            if (ChronoUnit.HOURS.between(user.getOtpCodeCreatedAT(), LocalDateTime.now()) > 1) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body(messageSource.getMessage("messages.code_expired", null, LocaleContextHolder.getLocale()));
//            }
//        } else {
//            try {
//                user = getUser(code, jwtUtils.getSecretBearerToken());
//            } catch (ExpiredJwtException e) {
//                log.error("JWT token is expired: {}", e.getMessage());
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body(messageSource.getMessage("messages.code_expired", null, LocaleContextHolder.getLocale()));
//            }
//        }
//        if ((code.equals(user.getTokenAuth()) || code.equals(user.getOtpCode()))
//                && user.getStatus().getName() == EStatusUser.USER_DISABLED) {
//            userService.editToken(user.getUserId(), null);
//            user.setOtpCode(null);
//            user.setOtpCodeCreatedAT(null);
//            userRepo.save(user);
//
//            userService.editStatus(user.getUserId(), Long.valueOf(EStatusUser.USER_ENABLED.ordinal() + 1L));
//
//            newUrl = signInUrl ;
//        } else {
//            newUrl = signInUrl;
//        }
//        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, newUrl).build();
//    }

    @Operation(summary = "Réinitialiser son mot de passe ", tags = "authentification", description = "La validation du token est requis pour le client web", responses = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "Application/Json")),
            @ApiResponse(responseCode = "404", description = "L'utilisateur n'existe pas dans la BD", content = @Content(mediaType = "Application/Json")),
            @ApiResponse(responseCode = "400", description = "Erreur dans le format de la requete", content = @Content(mediaType = "Application/Json"))})

    @GetMapping("/confirm-code")
    public ResponseEntity<Object> resetPassword(@RequestParam String code) throws Exception {
        Users user;
        try {
            user = getUser(code, jwtUtils.getSecretBearerToken());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponseDto(HttpStatus.UNAUTHORIZED, messageSource.getMessage("messages.code_expired", null, LocaleContextHolder.getLocale())));
        }
        if (code.equals(user.getTokenAuth())) {
            String newUrl = urlResetPasswordPage +"/" + code;
            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, newUrl).build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new MessageResponseDto(HttpStatus.UNAUTHORIZED, messageSource.getMessage("messages.unauthorized", null, LocaleContextHolder.getLocale())));
    }

    @Operation(summary = "Authentifie un utilisateur", tags = "authentification", responses = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "Application/Json", array = @ArraySchema(schema = @Schema(implementation = AuthResDto.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "Application/Json")),
            @ApiResponse(responseCode = "401", description = "", content = @Content(mediaType = "Application/Json"))})

    @PostMapping("/sign-in")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody AuthReqDto userAuthDto) throws Exception {
        Users user = new Users(userAuthDto.getLogin(), userAuthDto.getPassword());
        if (userAuthDto.getLogin().contains("@")) {
            Optional<Users> user2 = userService.getByEmail(userAuthDto.getLogin());
            System.out.println(user2.get().getUserId());
            if (user2.isPresent()) {
                user = new Users(user2.get().getEmail(), userAuthDto.getPassword());
            }
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        if (userPrincipal.getStatus().getName() == EStatusUser.USER_ENABLED) {
            String code = String.valueOf(jwtUtils.generateOtpCode());
            Users userUpdate = updateExistingUser(userPrincipal.getUsername(), code);
            String telephone = userUpdate.getTelephone() != null ? userUpdate.getTelephone() : String.valueOf(userUpdate.getTelephone());
            String email = userUpdate.getEmail() != null ? userUpdate.getEmail() : String.valueOf(userUpdate.getEmail());
            String bearerToken = jwtUtils.generateJwtToken(userPrincipal.getUsername(),
                    jwtUtils.getExpirationBearerToken(), jwtUtils.getSecretBearerToken(), false);

//            String bearerToken = jwtUtils.generateJwtToken(users.getEmail(),
//                    jwtUtils.getExpirationBearerToken(), jwtUtils.getSecretBearerToken(), true);
            String refreshToken = jwtUtils.generateJwtToken(userPrincipal.getUsername(),
                    jwtUtils.getExpirationRefreshToken(), jwtUtils.getSecretRefreshToken(), true);
            userService.editToken(userPrincipal.getId(), refreshToken);
            List<String> roles = userPrincipal.getRoles().stream().map(item -> item.getAuthority())
                    .collect(Collectors.toList());
            updateExistingUser(userPrincipal.getEmail(), null);
            userService.updateDateLastLoginUser(userPrincipal.getId());
            userService.updateFistLogin(userPrincipal.getId());
            userService.editToken(userPrincipal.getId(), null);
            Users users = userService.getByEmail(userPrincipal.getUsername()).get();

//            userRepo.save(users);
//            log.info("user " + users.getOtpCode() + " authenticated");
            return ResponseEntity.ok(new AuthSignInResDto(bearerToken, refreshToken, "Bearer", users, roles, true));


//            return ResponseEntity.ok().body(new SignInResponse(true, messageSource.getMessage("messages.code-otp", null, LocaleContextHolder.getLocale()), bearerToken, false));


        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new MessageResponseDto(HttpStatus.UNAUTHORIZED, messageSource.getMessage("messages.email-adresse-not-verify", null, LocaleContextHolder.getLocale())));
    }


    @Operation(summary = "Inscription sur l'application", tags = "authentification", responses = {
            @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès", content = @Content(mediaType = "Application/Json", array = @ArraySchema(schema = @Schema(implementation = UserResDto.class)))),
            @ApiResponse(responseCode = "400", description = "Erreur: Ce nom d'utilisateur est déjà utilisé/Erreur: Cet email est déjà utilisé", content = @Content(mediaType = "Application/Json")),})
    @PostMapping("/sign-up")
    public ResponseEntity<Object> add(@Valid @RequestBody UserReqDto userAddDto, HttpServletRequest request) {
        System.out.println("user "+ userAddDto.getEmail());
        System.out.println(userService.existsByEmail(userAddDto.getEmail(), null));
        if (userService.existsByEmail(userAddDto.getEmail(), null)) {
            return ResponseEntity.badRequest().body(new MessageResponseDto(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("messages.email_exists", null, LocaleContextHolder.getLocale())));
        }
        if (userService.existsByTelephone(userAddDto.getTelephone(), null)) {
            return ResponseEntity.badRequest().body(new MessageResponseDto(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("messages.phone_exists", null, LocaleContextHolder.getLocale())));
        }

        Users u = modelMapper.map(userAddDto, Users.class);
        u.setFirstName(userAddDto.getFirstName());
        u.setLastName(userAddDto.getLastName());
        Set<RoleUser> roles = new HashSet<>();
        RoleUser rolesUser = roleRepo.findByName(userAddDto.getRoleName() != null ? ERole.valueOf(userAddDto.getRoleName()) : ERole.ROLE_USER).orElseThrow(()-> new ResourceNotFoundException("Role not found"));
        roles.add(rolesUser);
        u.setRoles(roles);

        u.setPassword(encoder.encode(userAddDto.getPassword()));
        u.setCreatedDate(LocalDateTime.now());
        Users user = new Users();
        String password = null;
        Map<String, Object> userAndPasswordNotEncoded = new HashMap<>();

        userAndPasswordNotEncoded = userService.add(u);
        user = (Users) userAndPasswordNotEncoded.get("user");
        userService.editStatus(user.getUserId(), Long.valueOf(EStatusUser.USER_ENABLED.ordinal() + 1L));
        UserResDto userResDto = modelMapper.map(u, UserResDto.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResDto);
    }

    @Operation(summary = "Modification des information/profil de l'utilisateur", tags = "authentification", responses = {
            @ApiResponse(responseCode = "201", description = "Utilisateur modifié avec succès", content = @Content(mediaType = "Application/Json", array = @ArraySchema(schema = @Schema(implementation = UserResDto.class)))),
            @ApiResponse(responseCode = "400", description = "Erreur: Ce nom d'utilisateur est déjà utilisé/Erreur: Cet email est déjà utilisé", content = @Content(mediaType = "Application/Json")),})
    @PutMapping("/{internalReference:[0-9]+}")
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','AGENT','USER')")
    public ResponseEntity<Object> update(@Valid @RequestBody UserModifyReqDto userModifyReqDto, @PathVariable Long internalReference) {


        Users u = userService.getByInternalReference(internalReference);
        u.setFirstName(userModifyReqDto.getFirstName());
        u.setLastName(userModifyReqDto.getLastName());
        Set<RoleUser> roles = new HashSet<>();
        RoleUser rolesUser = roleRepo.findByName(userModifyReqDto.getRoleName() != null ? ERole.valueOf(userModifyReqDto.getRoleName()) : ERole.ROLE_USER).orElseThrow(()-> new ResourceNotFoundException("Role not found"));
        roles.add(rolesUser);
        u.setRoles(roles);

        u.setCreatedDate(LocalDateTime.now());
        Users user = new Users();
        String password = null;
        Map<String, Object> userAndPasswordNotEncoded = new HashMap<>();

        userAndPasswordNotEncoded = userService.modify(u);
        user = (Users) userAndPasswordNotEncoded.get("user");
        UserResDto userResDto = modelMapper.map(u, UserResDto.class);


        return ResponseEntity.status(HttpStatus.CREATED).body(userResDto);
    }

    @Operation(summary = "Obtenir un nouveau token de sécurité", tags = "authentification", responses = {
            @ApiResponse(responseCode = "200", description = "Succès de l'opération", content = @Content(mediaType = "Application/Json")),
            @ApiResponse(responseCode = "404", description = "L'utilisateur n'existe pas dans la BD", content = @Content(mediaType = "Application/Json")),
            @ApiResponse(responseCode = "401", description = "Refresh token revoqué", content = @Content(mediaType = "Application/Json")),
            @ApiResponse(responseCode = "400", description = "Erreur dans le format de la requete", content = @Content(mediaType = "Application/Json"))})

    @GetMapping("/refresh")
    public ResponseEntity<Object> refreshUserToken(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String refreshToken) throws Exception {
        String token = jwtUtils.parseJwt(refreshToken);
        Users user = getUser(token, jwtUtils.getSecretRefreshToken());
        if (token.equals(user.getTokenAuth())) {
            String newBearerToken = jwtUtils.refreshToken(token);
            return ResponseEntity.ok(new MessageResponseDto(newBearerToken));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponseDto(
                messageSource.getMessage("messages.token_revoked", null, LocaleContextHolder.getLocale())));
    }

    @Operation(summary = "Réinitialiser son mot de passe etape 1 (verification du user)", tags = "authentification", responses = {
            @ApiResponse(responseCode = "201", content = @Content(mediaType = "Application/Json")),
            @ApiResponse(responseCode = "404", description = "L'utilisateur n'existe pas dans la BD", content = @Content(mediaType = "Application/Json")),
            @ApiResponse(responseCode = "400", description = "Erreur dans le format de la requete", content = @Content(mediaType = "Application/Json"))})
    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto)
            throws Exception {
        if (resetPasswordDto.getLogin() == null || resetPasswordDto.getLogin().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponseDto(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("messages.requete_incorrect", null, LocaleContextHolder.getLocale())));
        }
        Users user = userService.checkUserAndGenerateCode(resetPasswordDto.getLogin());

        Map<String, Object> emailProps = new HashMap<>();
        emailProps.put("firstname", user.getFirstName());
        emailProps.put("lastname", user.getLastName());
        emailProps.put("email", user.getEmail());
        emailProps.put("username", user.getEmail());
        emailProps.put("code", urlConfirmCode + user.getTokenAuth());

        emailService.sendEmail(new EmailDto(mailFrom, ApplicationConstant.ENTREPRISE_NAME, user.getEmail(), "", emailProps, ApplicationConstant.SUBJECT_PASSWORD_RESET, ApplicationConstant.TEMPLATE_PASSWORD_RESET));
        log.info("Email for reset password send successfull for user: " + user.getEmail());

        return ResponseEntity.ok().body(new MessageResponseDto(HttpStatus.OK,
                messageSource.getMessage("messages.code_sent_success", null, LocaleContextHolder.getLocale())));
    }

    @Operation(summary = "Réinitialiser son mot de passe etape 2 (enrégistrement du password)", tags = "authentification", responses = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "Application/Json")),
            @ApiResponse(responseCode = "404", description = "L'utilisateur n'existe pas dans la BD", content = @Content(mediaType = "Application/Json")),
            @ApiResponse(responseCode = "400", description = "Mot de passe déjà utilisé par le passé", content = @Content(mediaType = "Application/Json"))})

    @PutMapping("/reset-password")
    public ResponseEntity<Object> resetPassword2(@RequestParam String code, @RequestBody UserResetPassword userResetPwd)
            throws Exception {
        Users user = userRepo.findByTokenAuth(code).orElseThrow(() -> new ResourceNotFoundException(
                messageSource.getMessage("messages.user_not_found", null, LocaleContextHolder.getLocale())));
        List<OldPassword> oldPasswords = user.getOldPasswords();
        for (OldPassword oldPassword : oldPasswords) {
            if (BCrypt.checkpw(userResetPwd.getPassword(), oldPassword.getPassword())) {
                return ResponseEntity.badRequest().body(new MessageResponseDto(HttpStatus.BAD_REQUEST,messageSource.getMessage("messages.password_already_use", null,
                        LocaleContextHolder.getLocale())));
            }
        }
        Users user2 = userService.resetPassword(user, userResetPwd.getPassword());
        return ResponseEntity.ok(user2);
    }

    @Operation(summary = "modifier le password d'un utilisateur", tags = "authentification", responses = {
            @ApiResponse(responseCode = "200", description = "Mot de passe changé avec succès", content = @Content(mediaType = "Application/Json")),
            @ApiResponse(responseCode = "400", description = "Erreur: Ancien mot de passe incorrect", content = @Content(mediaType = "Application/Json"))})
    @PreAuthorize("@authorizationService.canUpdateOwnerItem(#id, 'User')")
    @PutMapping("/user/{id}/password-update")
    public ResponseEntity<?> editPassword(@PathVariable Long id, @Valid @RequestBody UserEditPasswordDto userEditPasswordDto) {
        Users user = userService.getById(id);
        if (!BCrypt.checkpw(userEditPasswordDto.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(new DefaultResponseDto("Ancien mot de passe incorrect", HttpStatus.BAD_REQUEST));
        }
        List<OldPassword> oldPasswords = user.getOldPasswords();
        for (OldPassword oldPassword : oldPasswords) {
            if (BCrypt.checkpw(userEditPasswordDto.getPassword(), oldPassword.getPassword())) {
                return ResponseEntity.badRequest().body(new DefaultResponseDto("Mot de passe déjà utilisé par le passé", HttpStatus.BAD_REQUEST));
            }
        }
        userService.editPassword(user, userEditPasswordDto);
        userService.updateFistLogin(user.getUserId());
        return ResponseEntity.ok(new DefaultResponseDto("Mot de passe réinitialisé avec succès ", HttpStatus.OK));
    }

    @Operation(summary = "Information sur un utilisateur", tags = "authentification", responses = {
            @ApiResponse(responseCode = "200", description = "Succès de l'opération", content = @Content(mediaType = "Application/Json", array = @ArraySchema(schema = @Schema(implementation = Users.class)))),
            @ApiResponse(responseCode = "401", description = "", content = @Content(mediaType = "Application/Json"))})
    @GetMapping("/user/me")
    public Users getCurrentUser(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
        String tokenauth = jwtUtils.parseJwt(token);
        Users user = getUser(tokenauth, jwtUtils.getSecretBearerToken());
        return user;
    }

    private Users getUser(String token, String secret) {
        String email = jwtUtils.getIdGulfcamFromJwtToken(token, secret);
        return userService.getByEmail(email).get();
    }

    private Users updateExistingUser(String email, String code) {
        Optional<Users> existingUser = userService.getByEmail(email);

//        if(code != null)
//            existingUser.get().setOtpCode(code);
//
//        existingUser.get().setOtpCodeCreatedAT(LocalDateTime.now());
        return userRepo.save(existingUser.get());
    }

}
