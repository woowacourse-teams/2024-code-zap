package codezap.category.domain.exception;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;

public class DefaultCategoryException extends CodeZapException {

    public DefaultCategoryException(String message) {
        super(ErrorCode.DEFAULT_CATEGORY, message);
    }
}
