package org.blbcase.core.exceptions;

import org.blbcase.core.json.additionals.ResponseConstants;

/**
 *
 * @author Ruzov Vasilii (email: ruzov.vo@gmail.com)
 */
public class BLBException extends Exception {

    private Integer errorCode;

    /**
     * Creates a new instance of
     * <code>CardioException</code> without detail message.
     */
    public BLBException() {
    }

    /**
     * Constructs an instance of
     * <code>CardioException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public BLBException(String msg) {
        super(msg);
        this.errorCode = ResponseConstants.NORMAL_ERROR_CODE;
    }

    public BLBException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
