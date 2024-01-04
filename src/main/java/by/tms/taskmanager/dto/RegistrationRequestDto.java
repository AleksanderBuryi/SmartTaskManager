package by.tms.taskmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequestDto {
    @NotNull(message = "This field shouldn't be null")
    private String name;

    @NotNull(message = "This field shouldn't be null")
    private String surname;

    @NotNull(message = "This field shouldn't be null")
    @Email
    private String email;

    @NotNull(message = "This field shouldn't be null")
    @Range(min = 4, max = 16)
    private String password;
}
