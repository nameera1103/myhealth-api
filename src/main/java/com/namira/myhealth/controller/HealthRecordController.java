package com.namira.myhealth.controller;

import com.namira.myhealth.model.HealthRecord;
import com.namira.myhealth.repository.HealthRecordRepository;
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
    public HealthRecord createRecord(@RequestBody HealthRecord record) {
        return repository.save(record);
    }

    // GET one record by id
    @GetMapping("/{id}")
    public HealthRecord getRecordById(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    // PUT - update an existing record
    @PutMapping("/{id}")
    public HealthRecord updateRecord(@PathVariable Long id, @RequestBody HealthRecord updatedRecord) {
        HealthRecord existing = repository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }
        existing.setWeight(updatedRecord.getWeight());
        existing.setTemperature(updatedRecord.getTemperature());
        existing.setBloodPressure(updatedRecord.getBloodPressure());
        existing.setNote(updatedRecord.getNote());
        existing.setDate(updatedRecord.getDate());
        return repository.save(existing);
    }

    // DELETE a record
    @DeleteMapping("/{id}")
    public String deleteRecord(@PathVariable Long id) {
        repository.deleteById(id);
        return "Record " + id + " deleted.";
    }


}