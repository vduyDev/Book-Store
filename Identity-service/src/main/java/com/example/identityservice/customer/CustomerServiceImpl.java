package com.example.identityservice.customer;
import com.example.common.enums.ErrorCode;
import com.example.common.exception.AppException;
import com.example.common.request.CustomerRequest;
import com.example.common.request.CustomerUpdateRequest;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
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

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realmKey;


    @Override
    public List<Customer> listCustomer() {
        List<UserRepresentation> users = getUsersResource().list();
        return users
                .stream()
                .map(CustomerMapper::userToCustomer)
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
    public Customer getCustomerById(String id) {
        UserRepresentation userRepresentation = getUserById(id);
        return CustomerMapper.userToCustomer(userRepresentation);
    }

    private boolean userExists(String email) {
        List<UserRepresentation> users = getUsersResource().search(email, true);
        return !users.isEmpty();
    }

    @Override
    public Customer createCustomer(CustomerRequest request) {
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
            return customer;
        }

        throw new AppException(ErrorCode.CustomerAlreadyExists);
    }


    @Override
    public Customer updateCustomer(String id, CustomerUpdateRequest request) {
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
        return CustomerMapper.userToCustomer(user);
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