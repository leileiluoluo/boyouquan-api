package com.boyouquan.util;

import com.boyouquan.enumration.RestErrorResponse;
import com.boyouquan.model.ErrorResponse;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static ResponseEntity<ErrorResponse> errorResponse(RestErrorResponse restErrorResponse) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(restErrorResponse.getCode());
        errorResponse.setMessage(restErrorResponse.getMessage());
        return ResponseEntity.status(restErrorResponse.getHttpStatus())
                .body(errorResponse);
    }

    public static ResponseEntity<ErrorResponse> errorResponse(RestErrorResponse restErrorResponse, String message) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(restErrorResponse.getCode());
        errorResponse.setMessage(message);
        return ResponseEntity.status(restErrorResponse.getHttpStatus())
                .body(errorResponse);
    }

}
