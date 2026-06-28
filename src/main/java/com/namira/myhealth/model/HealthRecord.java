//This model folder will hold your data classes — same idea as your JavaFX model package./
package com.namira.myhealth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class HealthRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^$|^[0-9]+(\\.[0-9]+)?$",
            message = "Weight must be a number (e.g. 65 or 65.5).")
    private String weight;

    @Pattern(regexp = "^$|^[0-9]+(\\.[0-9]+)?$",
            message = "Temperature must be a number (e.g. 36 or 36.8).")
    private String temperature;

    @Pattern(regexp = "^$|^[0-9]+/[0-9]+$",
            message = "Blood pressure must be in format 120/80.")
    private String bloodPressure;

    @Size(max = 249, message = "Note must be less than 250 characters.")
    private String note;

    private String date;

    public HealthRecord() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getWeight() {
        return weight;
    }
    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTemperature() {
        return temperature;
    }
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }
    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}