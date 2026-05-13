package com.example.workoutapi.repository;

import com.example.workoutapi.model.Workout;
import com.example.workoutapi.model.enums.Difficulty;
import com.example.workoutapi.model.enums.MuscleGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class WorkoutRepositoryTest {

    @Autowired
    private WorkoutRepository workoutRepository;

    @BeforeEach
    void setUp() {
        // Clear db and setup dummy data for testing
        workoutRepository.deleteAll();

        Workout workout1 = Workout.builder()
                .name("Bench Press")
                .description("Push exercise")
                .muscleGroup(MuscleGroup.CHEST)
                .difficulty(Difficulty.INTERMEDIATE)
                .durationMinutes(45)
                .build();

        Workout workout2 = Workout.builder()
                .name("Squat")
                .description("Leg exercise")
                .muscleGroup(MuscleGroup.LEGS)
                .difficulty(Difficulty.ADVANCED)
                .durationMinutes(60)
                .build();

        Workout workout3 = Workout.builder()
                .name("Push Ups")
                .description("Bodyweight chest exercise")
                .muscleGroup(MuscleGroup.CHEST)
                .difficulty(Difficulty.BEGINNER)
                .durationMinutes(15)
                .build();

        workoutRepository.saveAll(List.of(workout1, workout2, workout3));
    }

    @Test
    @DisplayName("Should find workouts by muscle group")
    void findByMuscleGroup() {

        Page<Workout> chestWorkouts = workoutRepository.findByMuscleGroup(
                MuscleGroup.CHEST, PageRequest.of(0, 10));

        assertThat(chestWorkouts.getTotalElements()).isEqualTo(2);
        assertThat(chestWorkouts.getContent()).extracting(Workout::getName)
                .containsExactlyInAnyOrder("Bench Press", "Push Ups");
    }

    @Test
    @DisplayName("Should find workouts by difficulty")
    void findByDifficulty() {

        Page<Workout> advancedWorkouts = workoutRepository.findByDifficulty(
                Difficulty.ADVANCED, PageRequest.of(0, 10));


        assertThat(advancedWorkouts.getTotalElements()).isEqualTo(1);
        assertThat(advancedWorkouts.getContent().get(0).getName()).isEqualTo("Squat");
    }

    @Test
    @DisplayName("Should search workouts by keyword ignoring case")
    void searchByKeyword() {

        List<Workout> searchResults = workoutRepository.searchByKeyword("push");

        assertThat(searchResults).hasSize(2);
        assertThat(searchResults).extracting(Workout::getName)
                .containsExactlyInAnyOrder("Bench Press", "Push Ups");
    }
}
