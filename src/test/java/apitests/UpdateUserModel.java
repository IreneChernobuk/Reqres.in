package apitests;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode
public class UpdateUserModel {
    private String name;
    private String job;
}