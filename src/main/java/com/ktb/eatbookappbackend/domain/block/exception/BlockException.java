package com.ktb.eatbookappbackend.domain.block.exception;

import com.ktb.eatbookappbackend.domain.block.message.BlockErrorCode;
import com.ktb.eatbookappbackend.global.exception.DomainException;
import lombok.Getter;

@Getter
public class BlockException extends DomainException {

    private final BlockErrorCode errorCode;

    public BlockException(BlockErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
