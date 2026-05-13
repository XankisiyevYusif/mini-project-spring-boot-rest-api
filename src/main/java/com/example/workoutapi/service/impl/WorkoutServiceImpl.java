package com.example.workoutapi.service.impl;

import com.example.workoutapi.dto.request.WorkoutRequestDto;
import com.example.workoutapi.dto.response.WorkoutResponseDto;
import com.example.workoutapi.exception.WorkoutNotFoundException;
import com.example.workoutapi.model.Workout;
import com.example.workoutapi.model.enums.Difficulty;
import com.example.workoutapi.model.enums.MuscleGroup;
import com.example.workoutapi.repository.WorkoutRepository;
import com.example.workoutapi.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;

    @Override
    public WorkoutResponseDto createWorkout(WorkoutRequestDto request) {
        Workout workout = mapToEntity(request);
        Workout saved = workoutRepository.save(workout);
        return mapToResponse(saved);
    }

    @Override
    public WorkoutResponseDto getWorkoutById(Long id) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new WorkoutNotFoundException(id));

        return mapToResponse(workout);
    }

    @Override
    public WorkoutResponseDto updateWorkout(Long id, WorkoutRequestDto request) {
        Workout existing = workoutRepository.findById(id)
                .orElseThrow(() -> new WorkoutNotFoundException(id));

        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setMuscleGroup(request.getMuscleGroup());
        existing.setDifficulty(request.getDifficulty());
        existing.setDurationMinutes(request.getDurationMinutes());
        existing.setEquipment(request.getEquipment());

        Workout updated = workoutRepository.save(existing);

        return mapToResponse(updated);
    }

    @Override
    public void deleteWorkout(Long id) {
        if (!workoutRepository.existsById(id)) {
            throw new WorkoutNotFoundException(id);
        }

        workoutRepository.deleteById(id);
    }

    @Override
    public Page<WorkoutResponseDto> getAllWorkouts(MuscleGroup muscleGroup,
                                                   Difficulty difficulty,
                                                   Pageable pageable) {

        Page<Workout> page;

        if (muscleGroup != null && difficulty != null) {
            page = workoutRepository.findByMuscleGroupAndDifficulty(
                    muscleGroup,
                    difficulty,
                    pageable
            );

        } else if (muscleGroup != null) {
            page = workoutRepository.findByMuscleGroup(
                    muscleGroup,
                    pageable
            );

        } else if (difficulty != null) {
            page = workoutRepository.findByDifficulty(
                    difficulty,
                    pageable
            );

        } else {
            page = workoutRepository.findAll(pageable);
        }

        return page.map(this::mapToResponse);
    }

    @Override
    public List<WorkoutResponseDto> searchWorkouts(String keyword) {
        return workoutRepository.searchByKeyword(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Mapper Helpers

    private Workout mapToEntity(WorkoutRequestDto request) {
        return Workout.builder()
                .name(request.getName())
                .description(request.getDescription())
                .muscleGroup(request.getMuscleGroup())
                .difficulty(request.getDifficulty())
                .durationMinutes(request.getDurationMinutes())
                .equipment(request.getEquipment())
                .build();
    }

    private WorkoutResponseDto mapToResponse(Workout workout) {
        return WorkoutResponseDto.builder()
                .id(workout.getId())
                .name(workout.getName())
                .description(workout.getDescription())
                .muscleGroup(workout.getMuscleGroup())
                .difficulty(workout.getDifficulty())
                .durationMinutes(workout.getDurationMinutes())
                .equipment(workout.getEquipment())
                .createdAt(workout.getCreatedAt())
                .build();
    }
}