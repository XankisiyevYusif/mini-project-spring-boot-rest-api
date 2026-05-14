package com.example.workoutapi.controller;

import com.example.workoutapi.dto.request.WorkoutRequestDto;
import com.example.workoutapi.dto.response.WorkoutResponseDto;
import com.example.workoutapi.model.enums.Difficulty;
import com.example.workoutapi.model.enums.MuscleGroup;
import com.example.workoutapi.service.WorkoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
@Tag(name = "Workouts", description = "Workout management endpoints")
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new workout")
    public ResponseEntity<WorkoutResponseDto> createWorkout(@Valid @RequestBody WorkoutRequestDto request) {
        WorkoutResponseDto created = workoutService.createWorkout(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a workout by ID")
    public ResponseEntity<WorkoutResponseDto> getWorkoutById(@PathVariable Long id) {
        return ResponseEntity.ok(workoutService.getWorkoutById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing workout")
    public ResponseEntity<WorkoutResponseDto> updateWorkout(
            @PathVariable Long id,
            @Valid @RequestBody WorkoutRequestDto request) {
        return ResponseEntity.ok(workoutService.updateWorkout(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a workout")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        workoutService.deleteWorkout(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all workouts with optional filters and pagination")
    public ResponseEntity<Page<WorkoutResponseDto>> getAllWorkouts(
            @RequestParam(required = false) MuscleGroup muscleGroup,
            @RequestParam(required = false) Difficulty difficulty,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(workoutService.getAllWorkouts(muscleGroup, difficulty, pageable));
    }

    @GetMapping("/search")
    @Operation(summary = "Search workouts by keyword")
    public ResponseEntity<List<WorkoutResponseDto>> searchWorkouts(@RequestParam String keyword) {
        return ResponseEntity.ok(workoutService.searchWorkouts(keyword));
    }
}
