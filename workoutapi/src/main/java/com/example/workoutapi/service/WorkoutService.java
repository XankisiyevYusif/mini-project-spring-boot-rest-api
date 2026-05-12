package com.example.workoutapi.service;

import com.example.workoutapi.dto.request.WorkoutRequestDto;
import com.example.workoutapi.dto.response.WorkoutResponseDto;
import com.example.workoutapi.model.enums.Difficulty;
import com.example.workoutapi.model.enums.MuscleGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkoutService {

    WorkoutResponseDto createWorkout(WorkoutRequestDto request);

    WorkoutResponseDto getWorkoutById(Long id);

    WorkoutResponseDto updateWorkout(Long id, WorkoutRequestDto request);

    void deleteWorkout(Long id);

    Page<WorkoutResponseDto> getAllWorkouts(MuscleGroup muscleGroup,
                                            Difficulty difficulty,
                                            Pageable pageable);

    List<WorkoutResponseDto> searchWorkouts(String keyword);
}