package hexagon_talent.geundaero.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String code;
    private String message;
    private Map<String, Object> data;

    public static<T> ApiResponse<T> success(String wrapperName, Object payload) {
        Map<String, Object> map = new HashMap<>();
        map.put(wrapperName, payload);
        return new ApiResponse<>("0", "정상 처리 되었습니다.", map);
    }

    public static<T> ApiResponse<T> fail(String code, String message){
        return new ApiResponse<>(code, message,null);
    }

    public static<T> ApiResponse<T> fail(String code, String message, Map<String, Object> data){
        return new ApiResponse<>(code, message,data);
    }
}

