package org.blbcase.core.json.additionals;

import com.google.gson.Gson;

/**
 *
 * @author Ruzov Vasilii (email: ruzov.vo@gmail.com)
 */
public class SimpleResponseWrapper {

    public static String getJsonResponse(JsonResponse resp) {
        return (new Gson()).toJson(resp);
    }
}