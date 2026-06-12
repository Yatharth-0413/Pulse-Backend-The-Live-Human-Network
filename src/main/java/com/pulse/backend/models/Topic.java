package com.pulse.backend.models;
import jakarta.persistence.*;

@Entity
@Table(name = "topics")
public class Topic {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String name;
    private int activeUsers;
    private int heatScore;

    public Topic() {
    }

    public Topic(String name, int activeUsers, int heatScore) {
        this.name = name;
        this.activeUsers = activeUsers;
        this.heatScore = heatScore;
    }

    public String getName() {
        return name;
    }

    public int getActiveUsers() {
        return activeUsers;
    }

    public int getHeatScore() {
        return heatScore;
    }
    public void setActiveUsers(int activeUsers) {
        this.activeUsers = activeUsers;
    }

    public void setHeatScore(int heatScore) {
        this.heatScore = heatScore;
    }

    public void setName(String name) {
        this.name = name;
    }

     public Long getId() {
        return id;
    }

     public void setId(Long id) {
        this.id = id;
    }
}