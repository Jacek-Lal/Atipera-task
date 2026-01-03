package com.jacek.atipera_task;

final class UserNotFoundException extends RuntimeException {
    UserNotFoundException(String username, Exception e) {
      super("User " + username + " does not exist", e);
    }
}
