package by.tms.taskmanager.mapper;

import by.tms.taskmanager.dto.request.*;
import by.tms.taskmanager.dto.response.AuthResponseDto;
import by.tms.taskmanager.dto.response.StepResponseDto;
import by.tms.taskmanager.dto.response.TaskResponseDto;
import by.tms.taskmanager.dto.response.UserResponseDto;
import by.tms.taskmanager.entity.Step;
import by.tms.taskmanager.entity.Task;
import by.tms.taskmanager.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GeneralMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User mapToUser(RegistrationRequestDto registrationRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User mapToUser(UpdateUserDto updateUserDto);

    UserResponseDto mapToUserResponseDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "surname", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User mapToUser(AuthRequestDto authRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "difficulty", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "steps", ignore = true)
    Task mapToTask(TaskRequestDto taskRequestDto);

    @Mapping(target = "finishDate", ignore = true)
    TaskResponseDto mapToTaskResponseDto(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "task", ignore = true)
    Step mapToStep(StepRequestDto stepRequestDto);

    @Mapping(target = "name", source = "title")
    @Mapping(target = "isCompleted", source = "completed")
    StepResponseDto mapToStepResponseDto(Step step);

}
