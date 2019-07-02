package com.jando.fitness_app;

// Listens to step alerts
public interface StepCounterActivity {
    void step(long timeNs);
}