package dio.restapi2.service;

import dio.restapi2.model.User;

public interface UserService {

    User findById(Long id);

    User create(User userToCreate);
}
