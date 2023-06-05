package com.azatkhaliullin.socialmediaapi;

public class Exception {

    public static class AccountExistsException extends RuntimeException {
        public AccountExistsException() {
            super("User already exists");
        }
    }

    public static class UsernameFormatException extends RuntimeException {
        public UsernameFormatException() {
            super("Invalid username format");
        }
    }

    public static class EmailFormatException extends RuntimeException {
        public EmailFormatException() {
            super("Invalid email format");
        }
    }

    public static class PasswordFormatException extends RuntimeException {
        public PasswordFormatException() {
            super("Invalid password format");
        }
    }

    public static class PostValidationException extends RuntimeException {
        public PostValidationException() {
            super("Post must not be null or empty");
        }
    }

    public static class TokenGenerationException extends RuntimeException {
        public TokenGenerationException() {
            super("Token generation error");
        }
    }

    public static class InvalidPostOwnershipException extends RuntimeException {
        public InvalidPostOwnershipException() {
            super("This post does not belong to this user");
        }
    }

}