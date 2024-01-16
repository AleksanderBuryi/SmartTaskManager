package by.tms.taskmanager.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDto {
    @NotBlank(message = "Invalide name")
    @NotNull
    @Size(max = 100, min = 1, message = "The name must be between 1 and 100 characters long")
    private String name;

    @NotBlank(message = "Invalid description")
    @NotNull
    @Size(max = 1000, min = 1, message = "The description must be between 1 and 1000 characters long")
    private String description;

    @NotBlank
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

}
