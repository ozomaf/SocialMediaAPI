package com.azatkhaliullin.socialmediaapi.controller;

import com.azatkhaliullin.socialmediaapi.Exception.AccountExistsException;
import com.azatkhaliullin.socialmediaapi.Exception.EmailFormatException;
import com.azatkhaliullin.socialmediaapi.Exception.InvalidPostOwnershipException;
import com.azatkhaliullin.socialmediaapi.Exception.PasswordFormatException;
import com.azatkhaliullin.socialmediaapi.Exception.PostValidationException;
import com.azatkhaliullin.socialmediaapi.Exception.RelationshipExistsException;
import com.azatkhaliullin.socialmediaapi.Exception.TokenGenerationException;
import com.azatkhaliullin.socialmediaapi.Exception.UsernameFormatException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(AccountExistsException.class)
    public ResponseEntity<?> accountExistsException() {
        return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameFormatException.class)
    public ResponseEntity<?> usernameFormatException() {
        return new ResponseEntity<>("Invalid username format", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailFormatException.class)
    public ResponseEntity<?> emailFormatException() {
        return new ResponseEntity<>("Invalid email format", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordFormatException.class)
    public ResponseEntity<?> passwordFormatException() {
        return new ResponseEntity<>("Invalid password format", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PostValidationException.class)
    public ResponseEntity<?> postValidationException() {
        return new ResponseEntity<>("Post must not be null or empty", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenGenerationException.class)
    public ResponseEntity<?> tokenGenerationException() {
        return new ResponseEntity<>("Token generation error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({DataAccessException.class, SQLException.class})
    public ResponseEntity<?> databaseException() {
        return new ResponseEntity<>("Database error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidPostOwnershipException.class)
    public ResponseEntity<?> invalidPostOwnershipException() {
        return new ResponseEntity<>("This post does not belong to this user", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RelationshipExistsException.class)
    public ResponseEntity<?> relationshipExistsException() {
        return new ResponseEntity<>("Relationship already exists", HttpStatus.CONFLICT);
    }

}