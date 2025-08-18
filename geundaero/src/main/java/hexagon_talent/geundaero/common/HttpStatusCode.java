package hexagon_talent.geundaero.common;

import lombok.Getter;

@Getter
public enum HttpStatusCode {

    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    INTERNAL_SERVER_ERROR(500);

    private final int code;

    HttpStatusCode(int code){
        this.code = code;
    }
}