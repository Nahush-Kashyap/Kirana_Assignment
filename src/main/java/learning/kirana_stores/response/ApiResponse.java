package learning.kirana_stores.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude (JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private Object Data;
    private String Status;
    private String StatusCode;
    private String Error;
    private String ErrorMsg;
    private String DisplayMsg;
}
