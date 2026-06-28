package com.namira.myhealth.controller;

import jakarta.validation.Valid;
import com.namira.myhealth.model.HealthRecord;
import com.namira.myhealth.repository.HealthRecordRepository;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController  //This class handles web requests, send back whatever the methods return."

/*It sets a base URL for the whole class. So instead of writing the full path on every method,
every endpoint in this class automatically starts with /api/records. It's a prefix.*/

@RequestMapping("/api/records")
public class HealthRecordController {

    //dependency injection. Your controller needs the repository to do its work
    private final HealthRecordRepository repository;


    // Spring gives us the repository automatically (constructor injection)
    public HealthRecordController(HealthRecordRepository repository) {
        this.repository = repository;
    }

    // GET all records
    @GetMapping
    public List<HealthRecord> getAllRecords() {
        return repository.findAll();
    }

    // POST a new record
    @PostMapping
    public ResponseEntity<?> createRecord(@Valid @RequestBody HealthRecord record) {
        String error = customValidate(record);
        if (error != null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", error);
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(repository.save(record));
    }
    // GET one record by id
    @GetMapping("/{id}")
    public HealthRecord getRecordById(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    // PUT - update an existing record
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecord(@PathVariable Long id, @Valid @RequestBody HealthRecord updatedRecord) {
        String error = customValidate(updatedRecord);
        if (error != null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", error);
            return ResponseEntity.badRequest().body(response);
        }
        HealthRecord existing = repository.findById(id).orElse(null);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        existing.setWeight(updatedRecord.getWeight());
        existing.setTemperature(updatedRecord.getTemperature());
        existing.setBloodPressure(updatedRecord.getBloodPressure());
        existing.setNote(updatedRecord.getNote());
        existing.setDate(updatedRecord.getDate());
        return ResponseEntity.ok(repository.save(existing));
    }

    // DELETE a record
    @DeleteMapping("/{id}")
    public String deleteRecord(@PathVariable Long id) {
        repository.deleteById(id);
        return "Record " + id + " deleted.";
    }

    // Custom validation that annotations cannot express
    private String customValidate(HealthRecord record) {
        String weight = record.getWeight() == null ? "" : record.getWeight();
        String temperature = record.getTemperature() == null ? "" : record.getTemperature();
        String bloodPressure = record.getBloodPressure() == null ? "" : record.getBloodPressure();
        String note = record.getNote() == null ? "" : record.getNote();

        // Rule 1: at least one field must be filled
        boolean allEmpty = weight.isBlank() && temperature.isBlank()
                && bloodPressure.isBlank() && note.isBlank();
        if (allEmpty) {
            return "At least one field must be filled.";
        }

        // Rule 2: note must have fewer than 50 words
        if (!note.isBlank()) {
            String[] parts = note.split(" ");
            if (parts.length >= 50) {
                return "Note must have fewer than 50 words. Current: " + parts.length + ".";
            }
        }

        return null; // null means valid
    }
}