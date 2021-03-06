package org.blbcase.core.json.additionals;

import com.google.gson.Gson;
import org.blbcase.core.exceptions.BLBException;

/**
 *
 * @author Ruzov Vasilii (email: ruzov.vo@gmail.com)
 */
public class BLBExceptionWrapper {

    public static String wrapException(BLBException exc) {
        return (new Gson()).toJson(new JsonResponse(ResponseConstants.ERROR, new JsonError(exc.getMessage(), exc.getErrorCode()), null));
    }
}