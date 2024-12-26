package com.lamini.ecommerce.handler;

import java.util.Map;

public record ErrorResponse(
        //exp: firstname :  firstname is required or something
        Map<String,String> errors
) {
}
