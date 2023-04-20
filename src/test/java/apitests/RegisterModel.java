package apitests;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode

public class RegisterModel {
    private String email;
    private String password;
}