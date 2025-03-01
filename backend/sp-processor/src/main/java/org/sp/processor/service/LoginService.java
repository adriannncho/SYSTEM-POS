package org.sp.processor.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;
import org.sp.processor.domain.user.LoginDTO;
import org.sp.processor.domain.user.User;
import org.sp.processor.domain.user.UserData;
import org.sp.processor.helper.exception.PVException;
import org.sp.processor.helper.jwt.Token;
import org.sp.processor.repository.UserDataRepository;
import org.sp.processor.repository.UserRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class LoginService {

    private final Logger LOG = Logger.getLogger(ProductService.class);

    @Inject
    UserRepository userRepository;

    public String validateLogin(LoginDTO loginDTO) throws PVException {
        LOG.infof("@validateLogin SERV > Start service to validate the user");

        User user = userRepository.findById(loginDTO.getDocument());
        LOG.infof("@validateLogin SERV > Validate the user found:  %s", user);

        if (user == null) {
            LOG.warnf("@validateLogin SERV > No user found");
            throw new PVException(Response.Status.NOT_FOUND.getStatusCode(), "No se encontró usuario con el número de documento ingresado.");
        }

        if (!user.isUserStatusId()){
            LOG.warnf("@validateLogin SERV > No user found");
            throw new PVException(Response.Status.NOT_FOUND.getStatusCode(), "El usuario con el que intentas acceder esta desactivado.");
        }

        if (!checkPassword(loginDTO.getPassword(), user.getPassword())) {
            LOG.warnf("@validateLogin SERV > Incorrect password");
            throw new PVException(Response.Status.UNAUTHORIZED.getStatusCode(), "Contraseña incorrecta.");
        }

        LOG.infof("@validateLogin SERV > Finish service to validate the user");

        return Token.generateToken(user);
    }

    private boolean checkPassword(String rawPassword, String encryptedPassword) throws PVException {
        try {
            return BCrypt.checkpw(rawPassword, encryptedPassword);
        } catch (Exception e) {
            LOG.errorf("@checkPassword SERV > Error validating password", e);
            throw new PVException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Error validando la contraseña.");
        }
    }
}
