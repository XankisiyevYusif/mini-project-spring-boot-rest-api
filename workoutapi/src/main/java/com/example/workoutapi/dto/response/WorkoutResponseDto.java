package com.example.workoutapi.dto.response;

import com.example.workoutapi.model.enums.Difficulty;
import com.example.workoutapi.model.enums.MuscleGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutResponseDto {
    private Long id;
    private String name;
    private String description;
    private MuscleGroup muscleGroup;
    private Difficulty difficulty;
    private Integer durationMinutes;
    private String equipment;
    private LocalDateTime createdAt;
}
