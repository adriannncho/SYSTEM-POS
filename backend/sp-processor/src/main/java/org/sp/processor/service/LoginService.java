package org.sp.processor.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;
import org.sp.processor.domain.business.Business;
import org.sp.processor.domain.user.*;
import org.sp.processor.helper.exception.SPException;
import org.sp.processor.helper.jwt.Token;
import org.sp.processor.repository.RoleRepository;
import org.sp.processor.repository.TypeDocumentRepository;
import org.sp.processor.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class LoginService {

    private final Logger LOG = Logger.getLogger(LoginService.class);

    @ConfigProperty(name = "CONSTANTS.PROPERTIES.KEY_TOKEN")
    private String keyToken;

    @ConfigProperty(name = "CONSTANTS.PROPERTIES.URL_TOKEN")
    private String issuerToken;

    @Inject
    private UserRepository userRepository;

    @Inject
    private RoleRepository roleRepository;

    @Inject
    private TypeDocumentRepository typeDocumentRepository;

    public Map<String, String> validateLogin(LoginDTO loginDTO) throws SPException {
        LOG.infof("@validateLogin SERV > Start service to validate the user");

        User user = userRepository.findById(loginDTO.getDocument());
        LOG.infof("@validateLogin SERV > Validate the user found:  %s", user);

        validateUserExist(user);

        if (!user.isUserStatus()) {
            LOG.warnf("@validateLogin SERV > No user found");
            throw new SPException(Response.Status.NOT_FOUND.getStatusCode(), "El usuario con el que intentas acceder esta desactivado.");
        }

        if (!checkPassword(loginDTO.getPassword(), user.getPassword())) {
            LOG.warnf("@validateLogin SERV > Incorrect password");
            throw new SPException(Response.Status.UNAUTHORIZED.getStatusCode(), "Contraseña incorrecta.");
        }

        LOG.infof("@validateLogin SERV > Finish service to validate the user");

        return Token.generateToken(user, keyToken, issuerToken);
    }

    private void validateUserExist(User user) {
        LOG.info("@validateUserExist SERV > Validating if user exists");

        if (user == null) {
            LOG.warn("@validateUserExist SERV > No user found, throwing NOT_FOUND exception");
            throw new SPException(Response.Status.NOT_FOUND.getStatusCode(), "No se encontró usuario con el número de documento ingresado.");
        }

        LOG.info("@validateUserExist SERV > User exists, proceeding with login validation");
    }

    private boolean checkPassword(String rawPassword, String encryptedPassword) throws SPException {
        try {
            return BCrypt.checkpw(rawPassword, encryptedPassword);
        } catch (Exception e) {
            LOG.errorf("@checkPassword SERV > Error validating password", e);
            throw new SPException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Error validando la contraseña.");
        }
    }

    public Map<String, String> refreshToken(String authHeader) {
        LOG.infof("@refreshToken SERV > Extracting token from Authorization header");

        String token = authHeader.substring(7);
        LOG.infof("@refreshToken SERV > Token extracted: %s", token);

        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(keyToken))
                .build()
                .verify(token);

        LOG.infof("@refreshToken SERV > Decoded JWT: %s", decodedJWT.getClaims());

        Long documentNumber = decodedJWT.getClaim("document_number").asLong();
        LOG.infof("@refreshToken SERV > Extracted document number: %s", documentNumber);

        User user = userRepository.findByDocumentNumber(documentNumber);

        if (user == null) {
            LOG.warnf("@refreshToken SERV > User not found with document number: %s", documentNumber);
            throw new SPException(Response.Status.UNAUTHORIZED.getStatusCode(), "Usuario no encontrado");
        }

        LOG.infof("@refreshToken SERV > Validate the user found: %s", user);

        Map<String, String> newToken = Token.generateToken(user, keyToken, issuerToken);
        LOG.infof("@refreshToken SERV > New token generated successfully");

        Map<String, String> tokenRefresh = new HashMap<>();
        tokenRefresh.put("refresh_token", newToken.get("access_token"));

        LOG.infof("@refreshToken SERV > Returning refreshed token for user: %s", user.getDocumentNumber());

        return tokenRefresh;
    }

    public void saveUser(UserDTO userDTO) throws SPException {
        LOG.infof("@saveUser SERV > Start service to save a new user");

        if (userRepository.findByDocumentNumber(userDTO.getDocumentNumber()) != null) {
            LOG.warnf("@saveUser SERV > User already exists with document number %s", userDTO.getDocumentNumber());
            throw new SPException(Response.Status.CONFLICT.getStatusCode(), "El usuario ya existe.");
        }

        String encryptedPassword = encryptPassword(userDTO.getPassword());

        LOG.infof("@saveUser SERV > Creating user entity from DTO");
        User user = User.builder()
                .documentNumber(userDTO.getDocumentNumber())
                .userType(TypeUser.builder()
                        .idTypeUser(userDTO.getUserTypeId())
                        .build())
                .documentTypeId(TypeDocument.builder()
                        .idTypeDocument(userDTO.getDocumentTypeId())
                        .build())
                .businessId(Business.builder()
                        .idBusiness((long) userDTO.getBusinessId())
                        .build())
                .userStatus(true)
                .name(userDTO.getName())
                .phone(userDTO.getPhone())
                .email(userDTO.getEmail())
                .password(encryptedPassword)
                .build();

        LOG.infof("@saveUser SERV > Persisting user with document number %s", userDTO.getDocumentNumber());
        userRepository.persist(user);

        LOG.infof("@saveUser SERV > User saved successfully with document number %s", userDTO.getDocumentNumber());
    }

    private String encryptPassword(String password) throws SPException {
        try {
            return BCrypt.hashpw(password, BCrypt.gensalt());
        } catch (Exception e) {
            LOG.errorf("@encryptPassword SERV > Error encrypting password", e);
            throw new SPException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Error encriptando la contraseña.");
        }
    }

    public void updateUser(UserDTO userDTO) throws SPException {
        LOG.infof("@updateUser SERV > Start service to update user with ID %d", userDTO.getDocumentNumber());

        User existingUser = userRepository.findById(userDTO.getDocumentNumber());
        validateUserExist(existingUser);

        LOG.infof("@updateUser SERV > Found user with ID %d, proceeding with update", userDTO.getDocumentNumber());

        LOG.debugf("@updateUser SERV > Updating user type for ID %d", userDTO.getDocumentNumber());
        existingUser.setUserType(TypeUser.builder()
                .idTypeUser(userDTO.getUserTypeId())
                .build());
        existingUser.setDocumentTypeId(TypeDocument.builder()
                .idTypeDocument(userDTO.getDocumentTypeId())
                .build());
        existingUser.setUserStatus(userDTO.isStatusUser());
        existingUser.setName(userDTO.getName());
        existingUser.setPhone(userDTO.getPhone());
        existingUser.setEmail(userDTO.getEmail());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            LOG.infof("@updateUser SERV > Encrypting password for user ID %d", userDTO.getDocumentNumber());
            String encryptedPassword = encryptPassword(userDTO.getPassword());
            existingUser.setPassword(encryptedPassword);
            LOG.infof("@updateUser SERV > Password encrypted successfully for user ID %d", userDTO.getDocumentNumber());
        } else {
            LOG.errorf("@updateUser SERV > Password is null or empty for user ID %d", userDTO.getDocumentNumber());
            throw new SPException(Response.Status.CONFLICT.getStatusCode(), "La contraseña no puede estar vacía o ser nula.");
        }

        LOG.infof("@updateUser SERV > Persisting updated user with ID %d", userDTO.getDocumentNumber());
        userRepository.persist(existingUser);

        LOG.infof("@updateUser SERV > User with ID %d updated successfully", userDTO.getDocumentNumber());
    }

    public void changeStatusUser(Long userId) throws SPException {
        LOG.infof("@deleteUser SERV > Start service to delete user with ID %d", userId);

        User existingUser = userRepository.findById(userId);
        validateUserExist(existingUser);

        LOG.infof("@deleteUser SERV > Deactivating user with ID %d", userId);
        existingUser.setUserStatus(!existingUser.isUserStatus());
        userRepository.persist(existingUser);

        LOG.infof("@deleteUser SERV > User with ID %d deleted successfully", userId);
    }

    public List<TypeDocument> getTypeDocument() throws SPException {
        LOG.infof("@getTypeDocument SERV > Start service to obtain type documents");

        List<TypeDocument> typeDocumentList = typeDocumentRepository.listAll();
        LOG.infof("@getTypeDocument SERV > Retrieved list of type documents");

        if (typeDocumentList.isEmpty()) {
            LOG.warnf("@getTypeDocument SERV > No type documents found");
            throw new SPException(Response.Status.NOT_FOUND.getStatusCode(), "No se encontraron tipos de documentos.");
        }

        LOG.infof("@getTypeDocument SERV > Finish service to obtain type documents");
        return typeDocumentList;
    }

    public List<TypeUser> getRole() throws SPException {
        LOG.infof("@getRole SERV > Start service to obtain roles");

        List<TypeUser> roleList = roleRepository.listAll();
        LOG.infof("@getRole SERV > Retrieved list of roles");

        if (roleList.isEmpty()) {
            LOG.warnf("@getRole SERV > No roles found");
            throw new SPException(Response.Status.NOT_FOUND.getStatusCode(), "No se encontraron roles registrados.");
        }

        LOG.infof("@getRole SERV > Finish service to obtain roles");
        return roleList;
    }

    public List<User> getUser() {
        LOG.infof("@getUser SERV > Start service to get users");

        List<User> userList = userRepository.listAll();
        LOG.infof("@getUser SERV > Retrieved %d users from repository", userList.size());

        if (userList.isEmpty()) {
            LOG.infof("@getUser SERV > No users found, throwing NOT_FOUND exception");
            throw new SPException(Response.Status.NOT_FOUND.getStatusCode(), "No se encontraron registros de usuarios.");
        }

        LOG.infof("@getUser SERV > Returning list of users");
        return userList;
    }

}
