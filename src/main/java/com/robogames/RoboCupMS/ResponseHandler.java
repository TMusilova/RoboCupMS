package com.robogames.RoboCupMS;

import org.springframework.stereotype.Component;

@Component
public class ResponseHandler {

    public static Response response(Object data) {
        return new Response(Response.Type.RESPONSE, data);
    }

    public static Response warning(Object data) {
        return new Response(Response.Type.WARNING, data);
    }

    public static Response error(Object data) {
        return new Response(Response.Type.ERROR, data);
    }

}
