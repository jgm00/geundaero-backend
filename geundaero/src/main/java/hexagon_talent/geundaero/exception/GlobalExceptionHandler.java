package hexagon_talent.geundaero.exception;


import hexagon_talent.geundaero.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        String missingField = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField())
                .findFirst()
                .orElse("unknown");

        String message = "필수 파라미터가 누락되었습니다.";
        return ResponseEntity
                .status(400)
                .body(ApiResponse.fail("1001", message, Map.of("missingField", missingField)));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthorized(UnauthorizedAccessException ex) {
        return ResponseEntity.ok(ApiResponse.fail("4001", ex.getMessage()));
    }








}