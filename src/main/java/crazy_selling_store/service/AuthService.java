package crazy_selling_store.service;

import crazy_selling_store.entity.User;

public interface AuthService {
    boolean login(String userName, String password);

    boolean register(User user);
}
