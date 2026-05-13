package com.example.workoutapi.service;

import com.example.workoutapi.dto.request.WorkoutRequestDto;
import com.example.workoutapi.dto.response.WorkoutResponseDto;
import com.example.workoutapi.exception.WorkoutNotFoundException;
import com.example.workoutapi.model.Workout;
import com.example.workoutapi.model.enums.Difficulty;
import com.example.workoutapi.model.enums.MuscleGroup;
import com.example.workoutapi.repository.WorkoutRepository;
import com.example.workoutapi.service.impl.WorkoutServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceImplTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @InjectMocks
    private WorkoutServiceImpl workoutService;

    @Test
    @DisplayName("Should successfully create and save a workout")
    void createWorkout_ShouldSaveAndReturnDto() {
        // Arrange
        WorkoutRequestDto request = WorkoutRequestDto.builder()
                .name("Deadlift")
                .description("Heavy compound movement")
                .muscleGroup(MuscleGroup.BACK)
                .difficulty(Difficulty.ADVANCED)
                .durationMinutes(45)
                .build();

        Workout savedEntity = Workout.builder()
                .id(1L)
                .name("Deadlift")
                .description("Heavy compound movement")
                .muscleGroup(MuscleGroup.BACK)
                .difficulty(Difficulty.ADVANCED)
                .durationMinutes(45)
                .build();

        when(workoutRepository.save(any(Workout.class))).thenReturn(savedEntity);

        WorkoutResponseDto response = workoutService.createWorkout(request);


        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Deadlift", response.getName());

        verify(workoutRepository, times(1)).save(any(Workout.class));
    }

    @Test
    @DisplayName("Should successfully return workout when ID exists")
    void getWorkoutById_WhenIdExists_ShouldReturnDto() {

        Long workoutId = 1L;
        Workout existingWorkout = Workout.builder()
                .id(workoutId)
                .name("Squat")
                .build();

        when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(existingWorkout));


        WorkoutResponseDto response = workoutService.getWorkoutById(workoutId);

        assertNotNull(response);
        assertEquals("Squat", response.getName());
        verify(workoutRepository, times(1)).findById(workoutId);
    }

    @Test
    @DisplayName("Should throw Exception when getting workout with invalid ID")
    void getWorkoutById_WhenIdDoesNotExist_ShouldThrowException() {

        Long invalidId = 999L;
        when(workoutRepository.findById(invalidId)).thenReturn(Optional.empty());


        assertThrows(WorkoutNotFoundException.class, () -> workoutService.getWorkoutById(invalidId));
        verify(workoutRepository, times(1)).findById(invalidId);
    }

    @Test
    @DisplayName("Should throw Exception when deleting workout with invalid ID")
    void deleteWorkout_WhenIdDoesNotExist_ShouldThrowException() {

        Long invalidId = 999L;
        when(workoutRepository.existsById(invalidId)).thenReturn(false);


        assertThrows(WorkoutNotFoundException.class, () -> workoutService.deleteWorkout(invalidId));
        verify(workoutRepository, never()).deleteById(any());
    }
}
