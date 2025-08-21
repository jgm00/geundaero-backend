package hexagon_talent.geundaero.exception;

import hexagon_talent.geundaero.common.ErrorStatus;
import lombok.Getter;

@Getter
public class ExternalApiException extends RuntimeException {
    private final ErrorStatus errorStatus;

    public ExternalApiException(ErrorStatus errorStatus, String detailMessage) {
        super(detailMessage);
        this.errorStatus = errorStatus;
    }
}
