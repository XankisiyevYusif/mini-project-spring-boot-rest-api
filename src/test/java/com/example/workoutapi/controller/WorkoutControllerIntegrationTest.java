package com.example.workoutapi.controller;

import com.example.workoutapi.repository.WorkoutRepository;
import tools.jackson.databind.ObjectMapper;
import com.example.workoutapi.dto.request.WorkoutRequestDto;
import com.example.workoutapi.model.enums.Difficulty;
import com.example.workoutapi.model.enums.MuscleGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test ;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class  WorkoutControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WorkoutRepository workoutRepository;

    @BeforeEach
    void setUp() {
        workoutRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /api/workouts — should create workout and return 201")
    void shouldCreateWorkoutAndReturn201() throws Exception {
        // Arrange
        WorkoutRequestDto request = WorkoutRequestDto.builder()
                .name("Push Ups")
                .description("Classic chest bodyweight exercise")
                .muscleGroup(MuscleGroup.CHEST)
                .difficulty(Difficulty.BEGINNER)
                .durationMinutes(15)
                .equipment("None")
                .build();



        mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Push Ups")))
                .andExpect(jsonPath("$.muscleGroup", is("CHEST")))
                .andExpect(jsonPath("$.difficulty", is("BEGINNER")))
                .andExpect(jsonPath("$.durationMinutes", is(15)))
                .andExpect(jsonPath("$.createdAt", notNullValue()));
    }


    @Test
    @DisplayName("GET /api/workouts/{id} — should return workout by ID")
    void shouldReturnWorkoutById() throws Exception {
        // Create initial workout
        WorkoutRequestDto  request = WorkoutRequestDto.builder()
                .name("Squats")
                .description("Fundamental lower body compound movement")
                .muscleGroup(MuscleGroup.LEGS)
                .difficulty(Difficulty.INTERMEDIATE)
                .durationMinutes(30)
                .equipment("Barbell")
                .build();

        String createResponse = mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(createResponse).get("id").asLong();

        // Fetch the created workout by its ID
        mockMvc.perform(get("/api/workouts/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.name", is("Squats")))
                .andExpect(jsonPath("$.muscleGroup", is("LEGS")))
                .andExpect(jsonPath("$.durationMinutes", is(30)));
    }




    @Test
    @DisplayName("PUT /api/workouts/{id} — should update workout and return 200")
    void shouldUpdateWorkout() throws Exception  {
        // 1. Create a workout first
        WorkoutRequestDto createRequest = WorkoutRequestDto.builder()
                .name("Pull Ups")
                .description("Back and biceps bodyweight exercise")
                .muscleGroup(MuscleGroup.BACK)
                .difficulty(Difficulty.INTERMEDIATE)
                .durationMinutes(20)
                .equipment("Pull-up bar")
                .build();

        String createResponse = mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(createResponse).get("id").asLong();

        // 2. Prepare update data
        WorkoutRequestDto updateRequest = WorkoutRequestDto.builder()
                .name("Weighted Pull Ups" )
                .description("Advanced back and biceps exercise with added weight")
                .muscleGroup(MuscleGroup.BACK)
                .difficulty(Difficulty.ADVANCED)
                .durationMinutes(25)
                .equipment("Pull-up bar, weight belt")
                .build();

        mockMvc.perform(put("/api/workouts/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Weighted Pull Ups")))
                .andExpect(jsonPath("$.difficulty", is("ADVANCED")))
                .andExpect(jsonPath("$.durationMinutes", is(25)));
    }


    @Test
    @DisplayName("DELETE /api/workouts/{id} — should delete workout and return 204")
    void shouldDeleteWorkout() throws Exception {
        WorkoutRequestDto request  = WorkoutRequestDto.builder()
                .name("Plank")
                .description("Core stability exercise")
                .muscleGroup(MuscleGroup.CORE)
                .difficulty(Difficulty.BEGINNER)
                .durationMinutes(10)
                .equipment("None")
                .build();

        String createResponse = mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(createResponse).get("id").asLong();

        // Delete the workout
        mockMvc.perform(delete("/api/workouts/" + id))
                .andExpect(status().isNoContent());

        // Verify it no longer exists
        mockMvc.perform(get("/api/workouts/" + id))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("POST /api/workouts — should return 400 when validation fails")
    void shouldReturn400WhenValidationFails() throws Exception {
        // Build invalid request
        WorkoutRequestDto invalidRequest = WorkoutRequestDto.builder()
                .name("AB")
                .description("")
                .muscleGroup(MuscleGroup.CHEST)
                .difficulty(Difficulty.BEGINNER)
                .durationMinutes(500)
                .build();

        mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("GET /api/workouts?muscleGroup=CHEST — should return filtered results")
    void shouldFilterByMuscleGroup() throws Exception {
        WorkoutRequestDto  chest = WorkoutRequestDto.builder()
                .name("Bench Press")
                .description("Chest compound movement")
                .muscleGroup(MuscleGroup.CHEST)
                .difficulty(Difficulty.INTERMEDIATE)
                .durationMinutes(40)
                .equipment("Barbell, bench")
                .build();

        WorkoutRequestDto legs = WorkoutRequestDto.builder()
                .name("Lunges")
                .description("Leg isolation movement")
                .muscleGroup(MuscleGroup.LEGS)
                .difficulty(Difficulty.BEGINNER)
                .durationMinutes(20)
                .equipment("None")
                .build();

        mockMvc.perform(post("/api/workouts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(chest)));

        mockMvc.perform( post("/api/workouts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(legs)));

        mockMvc.perform(get("/api/workouts?muscleGroup=CHEST"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].muscleGroup", is("CHEST")));
    }


    @Test
    @DisplayName("GET /api/workouts/search?keyword=push — should return matching workouts")
    void shouldSearchWorkoutsByKeyword() throws Exception {
        WorkoutRequestDto request = WorkoutRequestDto.builder()
                .name("Push Ups")
                .description("Classic chest exercise")
                .muscleGroup(MuscleGroup.CHEST)
                .difficulty(Difficulty.BEGINNER)
                .durationMinutes(15)
                .equipment("None")
                .build();

        mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/workouts/search?keyword=push"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name", containsStringIgnoringCase("push")));
    }
}
