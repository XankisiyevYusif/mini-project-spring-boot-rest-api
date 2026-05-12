package com.example.workoutapi.exception;

public class WorkoutNotFoundException extends RuntimeException {
    public WorkoutNotFoundException(Long id) {
        super("Workout not found with id: " + id);
    }
}