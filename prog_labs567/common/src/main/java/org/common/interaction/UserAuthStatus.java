package org.common.interaction;

public enum UserAuthStatus implements Status {
    OK,
    WRONG_PASSWORD,
    ALREADY_EXISTS,
    NO_SUCH_LOGIN,
    ERROR;
}
