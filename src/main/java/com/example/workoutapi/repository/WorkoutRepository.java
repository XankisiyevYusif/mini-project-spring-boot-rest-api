package com.example.workoutapi.repository;

import com.example.workoutapi.model.Workout;
import com.example.workoutapi.model.enums.Difficulty;
import com.example.workoutapi.model.enums.MuscleGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    Page<Workout> findByMuscleGroup(MuscleGroup muscleGroup, Pageable pageable);

    Page<Workout> findByDifficulty(Difficulty difficulty, Pageable pageable);

    Page<Workout> findByMuscleGroupAndDifficulty(MuscleGroup muscleGroup, Difficulty difficulty, Pageable pageable);

    @Query("SELECT w FROM Workout w WHERE " +
            "LOWER(w.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(w.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Workout> searchByKeyword(@Param("keyword") String keyword);
}

