package by.tms.taskmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepRequestDto {
    @NotEmpty
    @NotBlank
    private String title;

    @NotEmpty
    @NotBlank
    private String description;

    private boolean isCompleted;
}
