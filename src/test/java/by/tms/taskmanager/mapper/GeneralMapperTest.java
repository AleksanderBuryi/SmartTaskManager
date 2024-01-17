package by.tms.taskmanager.mapper;

import by.tms.taskmanager.dto.request.*;
import by.tms.taskmanager.dto.response.StepResponseDto;
import by.tms.taskmanager.dto.response.TaskResponseDto;
import by.tms.taskmanager.dto.response.UserResponseDto;
import by.tms.taskmanager.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GeneralMapperTest {

    @Autowired
    private GeneralMapper mapper;

    @Test
    void testRegistrationRequestDtoToUserMapping() {
        RegistrationRequestDto requestDto = RegistrationRequestDto.builder()
                .name("John")
                .surname("Doe")
                .email("johndoe@example.com")
                .password("password")
                .build();

        User user = mapper.mapToUser(requestDto);

        assertNotNull(user);
        assertEquals(requestDto.getName(), user.getName());
        assertEquals(requestDto.getSurname(), user.getSurname());
        assertEquals(requestDto.getEmail(), user.getEmail());
        assertEquals(requestDto.getPassword(), user.getPassword());
    }

    @Test
    void testUpdateUserDtoToUserMapping() {
        UpdateUserDto userDto = UpdateUserDto.builder()
                .name("John")
                .surname("Doe")
                .email("johndoe@example.com")
                .password("password")
                .build();

        User user = mapper.mapToUser(userDto);

        assertNotNull(user);
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getSurname(), user.getSurname());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getPassword(), user.getPassword());
    }

    @Test
    void testUserToUserResponseDtoMapping() {
        User user = User.builder()
                .id(42L)
                .name("John")
                .surname("Doe")
                .email("johndoe@example.com")
                .password("password")
                .roles(Collections.singleton(Role.USER))
                .build();

        UserResponseDto responseDto = mapper.mapToUserResponseDto(user);

        assertNotNull(responseDto);
        assertEquals(user.getId(), responseDto.getId());
        assertEquals(user.getSurname(), responseDto.getSurname());
        assertEquals(user.getEmail(), responseDto.getEmail());
        assertEquals(user.getRoles(), responseDto.getRoles());
    }

    @Test
    void testAuthRequestDtoToUserMapping() {
        AuthRequestDto requestDto = AuthRequestDto.builder()
                .email("johndoe@example.com")
                .password("password")
                .build();

        User user = mapper.mapToUser(requestDto);

        assertNotNull(user);
        assertEquals(requestDto.getEmail(), user.getEmail());
        assertEquals(requestDto.getPassword(), user.getPassword());
    }

    @Test
    void testTaskRequestDtoToTaskMapping() {
        TaskRequestDto requestDto = TaskRequestDto.builder()
                .name("Test task 1")
                .description("Test task for testing test")
                .startDate(LocalDate.now())
                .build();

        Task task = mapper.mapToTask(requestDto);

        assertNotNull(task);
        assertEquals(requestDto.getName(), task.getName());
        assertEquals(requestDto.getDescription(), task.getDescription());
        assertEquals(requestDto.getStartDate(), task.getStartDate());
    }

    @Test
    void testTaskToTaskResponseDtoMapping() {
        Task task = Task.builder()
                .id(42L)
                .name("Test task")
                .description("Test task for testing task")
                .difficulty(Difficulty.DIFFICULT)
                .startDate(LocalDate.now())
                .status(Status.INPROGRESS)
                .build();

        List<Step> steps = Stream.of(
                new Step(1L, "Step 1", "Step description", true, task),
                new Step(2L, "Step 2", "Step description", false, task),
                new Step(3L, "Step 3", "Step description", false, task)
        ).toList();

        task.setSteps(steps);

        TaskResponseDto responseDto = mapper.mapToTaskResponseDto(task);
        LocalDate finishDate = LocalDate.now();
        responseDto.setFinishDate(finishDate);

        assertNotNull(responseDto);
        assertEquals(task.getId(), responseDto.getId());
        assertEquals(task.getName(), responseDto.getName());
        assertEquals(task.getDescription(), responseDto.getDescription());
        assertEquals(task.getDifficulty(), responseDto.getDifficulty());
        assertEquals(task.getStartDate(), responseDto.getStartDate());
        assertEquals(finishDate, responseDto.getFinishDate());
        assertEquals(task.getStatus(), responseDto.getStatus());
    }

    @Test
    void testStepRequestDtoToStepMapping() {
        StepRequestDto requestDto = StepRequestDto.builder()
                .title("Test Step")
                .description("Test step for testing test")
                .completed(true)
                .build();

        Step step = mapper.mapToStep(requestDto);

        assertNotNull(step);
        assertEquals(requestDto.getTitle(), step.getTitle());
        assertEquals(requestDto.getDescription(), step.getDescription());
        assertEquals(requestDto.isCompleted(), step.isCompleted());
    }

    @Test
    void testStepToStepResponseDtoMapping() {
        Step step = Step.builder()
                .id(1L)
                .title("Test step")
                .description("Test step for testing test")
                .completed(true)
                .build();

        Task task = Task.builder()
                .id(42L)
                .name("Test task")
                .description("Test task for testing task")
                .difficulty(Difficulty.DIFFICULT)
                .startDate(LocalDate.now())
                .status(Status.INPROGRESS)
                .steps(Stream.of(step).toList())
                .build();

        step.setTask(task);

        StepResponseDto responseDto = mapper.mapToStepResponseDto(step);

        assertNotNull(responseDto);
        assertEquals(step.getId(), responseDto.getId());
        assertEquals(step.getTitle(), responseDto.getName());
        assertEquals(step.getDescription(), responseDto.getDescription());
        assertEquals(step.isCompleted(), responseDto.isCompleted());
    }

}
