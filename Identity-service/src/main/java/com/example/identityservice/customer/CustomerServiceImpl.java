package com.example.identityservice.customer;

import com.example.common.DTO.CustomerDTO;
import com.example.common.enums.ErrorCode;
import com.example.common.exception.AppException;
import com.example.common.request.AuthRequest;
import com.example.common.request.CustomerRequest;
import com.example.common.request.CustomerUpdateRequest;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.keycloak.admin.client.Keycloak;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CustomerServiceImpl implements CustomerService {


    @Autowired
    private Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realmKey;

    @Value("${keycloak.realm}")
    private  String realm;
    @Value("${keycloak.adminClientId}")
    private String client;
    @Value("${keycloak.urls.auth}")
    private String url;
    @Value("${keycloak.adminClientSecret}")
    private String clientSecret;


    @Override
    public List<CustomerDTO> listCustomer() {
        List<UserRepresentation> users = getUsersResource().list();
        return users
                .stream()
                .map(CustomerMapper::toCustomerDTO)
                .toList();
    }

    @Override
    public void deleteCustomerById(String id) {
        Response response = getUsersResource().delete(id);
        if (response.getStatus() == 404) {
            throw new AppException(ErrorCode.CustomerNotFound);
        }
    }

    @Override
    public CustomerDTO getCustomerById(String id) {
        UserRepresentation userRepresentation = getUserById(id);
        return CustomerMapper.toCustomerDTO(userRepresentation);
    }

    private boolean userExists(String email) {
        List<UserRepresentation> users = getUsersResource().search(email, true);
        return !users.isEmpty();
    }

    @Override
    public CustomerDTO createCustomer(CustomerRequest request) {
        if (userExists(request.getEmail())) {
            throw new AppException(ErrorCode.CustomerAlreadyExists);
        }

        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setEmail(request.getEmail());
        user.setUsername(request.getEmail());
        user.setLastName(request.getLastName());
        user.setFirstName(request.getFirstName());
        user.setEmailVerified(false);

        Map<String, List<String>> attribute = Map.of(
                "address", List.of(request.getAddress()),
                "phone", List.of(request.getPhone())
        );

        user.setAttributes(attribute);
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setValue(request.getPassword());
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);

        List<CredentialRepresentation> list = List.of(credential);

        user.setCredentials(list);
        UsersResource userResource = getUsersResource();
        Response response = userResource.create(user);
        if (response.getStatus() == 201) {
            String userId = getUserIdFromResponse(response);
            assignRoleToUser(userId);
            Customer customer = CustomerMapper.requestToCustomer(request);
            customer.setId(userId);
            return CustomerMapper.CustomertoCustomerDTO(customer) ;
        }

        throw new AppException(ErrorCode.CustomerAlreadyExists);
    }


    @Override
    public CustomerDTO updateCustomer(String id, CustomerUpdateRequest request) {
        UserRepresentation user = getUserById(id);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmailVerified(false);
        Map<String, List<String>> attribute = Map.of(
                "address", List.of(request.getAddress()),
                "phone", List.of(request.getPhone())
        );
        user.setAttributes(attribute);
        getUsersResource().get(id).update(user);
        return CustomerMapper.toCustomerDTO(user);
    }

    @Override
    public AccessTokenResponse login(AuthRequest authRequest) {
        Keycloak keycloak1 = KeycloakBuilder.builder()
                .clientId(client)
                .realm(realm)
                .grantType("password")
                .serverUrl(url)
                .username(authRequest.getUsername())
                .password(authRequest.getPassword())
                .clientSecret(clientSecret)
                .build();

        return keycloak1.tokenManager().getAccessToken();
    }

    @Override
    public CustomerDTO getCustomerInBorrowing(String id) {
        return getCustomerById(id);

    }

    @Override
    public List<CustomerDTO> getListCustomerInBorrowing() {
        List<UserRepresentation> users = getUsersResource().list();
        return  users.stream().map(CustomerMapper::toCustomerDTO).toList();
    }


    private UsersResource getUsersResource() {
        RealmResource realm = keycloak.realm(realmKey);
        return realm.users();
    }

    private void assignRoleToUser(String userId) {
        UserResource userResource = getUsersResource().get(userId);
        RoleRepresentation roleRepresentation = keycloak.realm(realmKey).roles().get("user").toRepresentation();
        userResource.roles().realmLevel().add(Collections.singletonList(roleRepresentation));
    }

    private String getUserIdFromResponse(Response response) {
        String location = response.getHeaderString("Location");
        return location.substring(location.lastIndexOf('/') + 1);
    }

    private UserRepresentation getUserById(String id) {
        try {
            return getUsersResource().get(id).toRepresentation();
        } catch (jakarta.ws.rs.NotFoundException ex) {
            throw new AppException(ErrorCode.CustomerNotFound);
        }
    }


}