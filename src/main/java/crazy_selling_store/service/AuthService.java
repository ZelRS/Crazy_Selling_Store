package crazy_selling_store.service;

import crazy_selling_store.dto.Register;

public interface AuthService {
    boolean login(String userName, String password);

    boolean register(Register register);
}