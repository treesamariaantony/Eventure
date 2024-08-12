package fr.epita.eventure.service;

import com.mongodb.MongoException;
import fr.epita.eventure.datamodel.User;
import fr.epita.eventure.datamodel.UserRole;
import fr.epita.eventure.repository.UserRepository;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PreDestroy;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;

//
//@Service
//public class UserService {
//
//    @Value("${keycloak.auth-server-url}")
//    private String keycloakAuthServerUrl;
//
//    @Value("${keycloak.realm}")
//    private String keycloakRealm;
//
//    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
//    private String keycloakClientId;
//
//    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
//    private String keycloakClientSecret;
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//    public User getUserById(String userId) {
//        return userRepository.findById(userId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
//    }
//
//    private Keycloak getAdminKeycloak() {
//        return KeycloakBuilder.builder()
//                .serverUrl(keycloakAuthServerUrl)
//                .realm(keycloakRealm)
//                .clientId(keycloakClientId)
//                .clientSecret(keycloakClientSecret)
//                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
//                .build();
//    }

//    public void registerUser(String username, String password, String email, String name) {
//        Keycloak keycloak = null;
//        try {
//            keycloak = getAdminKeycloak();
//
//            UserRepresentation user = new UserRepresentation();
//            user.setUsername(username);
//            user.setEmail(email);
//            user.setEnabled(true);
//            user.setFirstName(name);
//
//            CredentialRepresentation credential = new CredentialRepresentation();
//            credential.setType(CredentialRepresentation.PASSWORD);
//            credential.setValue(password);
//            credential.setTemporary(false);
//            user.setCredentials(Collections.singletonList(credential));
//
//            Response response = keycloak.realm(keycloakRealm).users().create(user);
//            if (response.getStatus() == 201) {
//                String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
//
//                User mongoUser = new User();
//                mongoUser.setUsername(username);
//                mongoUser.setEmail(email);
//                mongoUser.setName(name);
//                mongoUser.setPassword(passwordEncoder.encode(password));
//                mongoUser.setRole(UserRole.USER);
//
//                userRepository.save(mongoUser);
//            } else {
//                throw new RuntimeException("Failed to create user in Keycloak. Status code: " + response.getStatus());
//            }
//        } catch (MongoException e) {
//            throw new RuntimeException("Error saving user in MongoDB: " + e.getMessage(), e);
//        } catch (Exception e) {
//            throw new RuntimeException("Unexpected error during user registration: " + e.getMessage(), e);
//        } finally {
//            if (keycloak != null) {
//                keycloak.close();
//            }
//        }
//    }

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PreDestroy;
import javax.ws.rs.core.Response;
import java.util.Collections;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Value("${keycloak.auth-server-url}")
    private String keycloakAuthServerUrl;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String keycloakClientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String keycloakClientSecret;

    @Value("${spring.security.oauth2.client.registration.keycloak-admin.client-id}")
    private String keycloakAdminClientId;

    @Value("${spring.security.oauth2.client.registration.keycloak-admin.client-secret}")
    private String keycloakAdminClientSecret;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private Keycloak adminKeycloak; // Keycloak instance for admin client

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        log.debug("UserService initialized with Keycloak URL: {}", keycloakAuthServerUrl);
    }

    // Lazy initialization of admin Keycloak instance
    private Keycloak getAdminKeycloak() {
        if (adminKeycloak == null) {
            if (keycloakAuthServerUrl == null || keycloakRealm == null || keycloakAdminClientId == null || keycloakAdminClientSecret == null) {
                throw new IllegalStateException("Keycloak configuration properties are not set correctly");
            }
            adminKeycloak = KeycloakBuilder.builder()
                    .serverUrl(keycloakAuthServerUrl)
                    .realm(keycloakRealm)
                    .clientId(keycloakAdminClientId)
                    .clientSecret(keycloakAdminClientSecret)
                    .grantType("client_credentials")
                    .build();
        }
        return adminKeycloak;
    }

    // Clean up method to close Keycloak client when service is destroyed
    @PreDestroy
    public void closeKeycloakClient() {
        if (adminKeycloak != null) {
            adminKeycloak.close();
        }
    }

    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
    }

    public void registerUser(String username, String password, String email, String name) {
        try {
            // Use adminKeycloak without closing it here
            UserRepresentation user = new UserRepresentation();
            user.setUsername(username);
            user.setEmail(email);
            user.setEnabled(true);

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            credential.setTemporary(false);
            user.setCredentials(Arrays.asList(credential));
            log.debug("Credential Type: {}", credential.toString());
            Response response = getAdminKeycloak().realm(keycloakRealm).users().create(user);
            if (response.getStatus() == 201) {
                // User creation successful
                String keycloakUserId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                User mongoUser = new User();
                mongoUser.setUsername(username);
                mongoUser.setEmail(email);
                mongoUser.setName(name);
                mongoUser.setPassword(passwordEncoder.encode(password));
                mongoUser.setRole(UserRole.USER);
                mongoUser.setId(keycloakUserId);

                userRepository.save(mongoUser);
            } else {
                String errorMessage = "Failed to create user in Keycloak. Status code: " + response.getStatus();
                if (response.hasEntity()) {
                    errorMessage += ", Response body: " + response.readEntity(String.class);
                }
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during user registration: " + e.getMessage(), e);
        }
    }

    public String authenticateUser(String username, String password) {
        Keycloak keycloak = null;
        try {
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(keycloakAuthServerUrl)
                    .realm(keycloakRealm)
                    .clientId(keycloakClientId)
                    .clientSecret(keycloakClientSecret)
                    .grantType(OAuth2Constants.PASSWORD)
                    .username(username)
                    .password(password)
                    .build();

            AccessTokenResponse tokenResponse = keycloak.tokenManager().getAccessToken();
            return tokenResponse.getToken(); // Return the access token
        } catch (Exception e) {
            throw new RuntimeException("Error authenticating user with Keycloak", e);
        } finally {
            if (keycloak != null) {
                keycloak.close();
            }
        }
    }
}
