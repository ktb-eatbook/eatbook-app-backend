package com.ktb.eatbookappbackend.domain.global.message;

import org.springframework.http.HttpStatus;

public interface MessageCode {

    HttpStatus getStatus();

    String getMessage();
}
