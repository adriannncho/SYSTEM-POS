package org.sp.processor.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;
import org.sp.processor.domain.business.Business;
import org.sp.processor.domain.user.*;
import org.sp.processor.helper.exception.PVException;
import org.sp.processor.helper.jwt.Token;
import org.sp.processor.repository.RoleRepository;
import org.sp.processor.repository.TypeDocumentRepository;
import org.sp.processor.repository.UserRepository;

import java.util.List;

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

    public String validateLogin(LoginDTO loginDTO) throws PVException {
        LOG.infof("@validateLogin SERV > Start service to validate the user");

        User user = userRepository.findById(loginDTO.getDocument());
        LOG.infof("@validateLogin SERV > Validate the user found:  %s", user);

        validateUserExist(user);

        if (!user.isUserStatus()) {
            LOG.warnf("@validateLogin SERV > No user found");
            throw new PVException(Response.Status.NOT_FOUND.getStatusCode(), "El usuario con el que intentas acceder esta desactivado.");
        }

        if (!checkPassword(loginDTO.getPassword(), user.getPassword())) {
            LOG.warnf("@validateLogin SERV > Incorrect password");
            throw new PVException(Response.Status.UNAUTHORIZED.getStatusCode(), "Contraseña incorrecta.");
        }

        LOG.infof("@validateLogin SERV > Finish service to validate the user");

        return Token.generateToken(user, keyToken, issuerToken);
    }

    private void validateUserExist(User user) {
        LOG.info("@validateUserExist SERV > Validating if user exists");

        if (user == null) {
            LOG.warn("@validateUserExist SERV > No user found, throwing NOT_FOUND exception");
            throw new PVException(Response.Status.NOT_FOUND.getStatusCode(), "No se encontró usuario con el número de documento ingresado.");
        }

        LOG.info("@validateUserExist SERV > User exists, proceeding with login validation");
    }


    private boolean checkPassword(String rawPassword, String encryptedPassword) throws PVException {
        try {
            return BCrypt.checkpw(rawPassword, encryptedPassword);
        } catch (Exception e) {
            LOG.errorf("@checkPassword SERV > Error validating password", e);
            throw new PVException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Error validando la contraseña.");
        }
    }

    public void saveUser(UserDTO userDTO) throws PVException {
        LOG.infof("@saveUser SERV > Start service to save a new user");

        if (userRepository.findByDocumentNumber(userDTO.getDocumentNumber()) != null) {
            LOG.warnf("@saveUser SERV > User already exists with document number %s", userDTO.getDocumentNumber());
            throw new PVException(Response.Status.CONFLICT.getStatusCode(), "El usuario ya existe.");
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

    private String encryptPassword(String password) throws PVException {
        try {
            return BCrypt.hashpw(password, BCrypt.gensalt());
        } catch (Exception e) {
            LOG.errorf("@encryptPassword SERV > Error encrypting password", e);
            throw new PVException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Error encriptando la contraseña.");
        }
    }

    public void updateUser(UserDTO userDTO) throws PVException {
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
            throw new PVException(Response.Status.CONFLICT.getStatusCode(), "La contraseña no puede estar vacía o ser nula.");
        }

        LOG.infof("@updateUser SERV > Persisting updated user with ID %d", userDTO.getDocumentNumber());
        userRepository.persist(existingUser);

        LOG.infof("@updateUser SERV > User with ID %d updated successfully", userDTO.getDocumentNumber());
    }

    public void inactiveUser(Long userId) throws PVException {
        LOG.infof("@deleteUser SERV > Start service to delete user with ID %d", userId);

        User existingUser = userRepository.findById(userId);
        validateUserExist(existingUser);

        LOG.infof("@deleteUser SERV > Deactivating user with ID %d", userId);
        existingUser.setUserStatus(false);
        userRepository.persist(existingUser);

        LOG.infof("@deleteUser SERV > User with ID %d deleted successfully", userId);
    }

    public List<TypeDocument> getTypeDocument() throws PVException {
        LOG.infof("@getTypeDocument SERV > Start service to obtain type documents");

        List<TypeDocument> typeDocumentList = typeDocumentRepository.listAll();
        LOG.infof("@getTypeDocument SERV > Retrieved list of type documents");

        if (typeDocumentList.isEmpty()) {
            LOG.warnf("@getTypeDocument SERV > No type documents found");
            throw new PVException(Response.Status.NOT_FOUND.getStatusCode(), "No se encontraron tipos de documentos.");
        }

        LOG.infof("@getTypeDocument SERV > Finish service to obtain type documents");
        return typeDocumentList;
    }

    public List<TypeUser> getRole() throws PVException {
        LOG.infof("@getRole SERV > Start service to obtain roles");

        List<TypeUser> roleList = roleRepository.listAll();
        LOG.infof("@getRole SERV > Retrieved list of roles");

        if (roleList.isEmpty()) {
            LOG.warnf("@getRole SERV > No roles found");
            throw new PVException(Response.Status.NOT_FOUND.getStatusCode(), "No se encontraron roles registrados.");
        }

        LOG.infof("@getRole SERV > Finish service to obtain roles");
        return roleList;
    }

}
