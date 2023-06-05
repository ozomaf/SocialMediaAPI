package com.azatkhaliullin.socialmediaapi;

import com.azatkhaliullin.socialmediaapi.Exception.EmailFormatException;
import com.azatkhaliullin.socialmediaapi.Exception.PasswordFormatException;
import com.azatkhaliullin.socialmediaapi.Exception.UsernameFormatException;
import com.azatkhaliullin.socialmediaapi.dto.AuthData;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FieldValidationUtils {

    public void validateSignUpRequest(AuthData request) {
        validateUsername(request.getUsername());
        validatePassword(request.getPassword());
        validateEmail(request.getEmail());
    }

    public void validateLoginRequest(AuthData request) {
        validateUsername(request.getUsername());
        validatePassword(request.getPassword());
    }

    public void validateUsername(String username) {
        if (username == null || username.isBlank())
            throw new UsernameFormatException();
    }

    public void validateEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9_-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"))
            throw new EmailFormatException();
    }

    public void validatePassword(String password) {
        if (password == null || password.isBlank())
            throw new PasswordFormatException();
    }

}