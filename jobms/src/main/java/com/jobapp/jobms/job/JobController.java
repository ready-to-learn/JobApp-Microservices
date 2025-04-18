package com.jobapp.jobms.job;

import com.jobapp.jobms.job.dto.JobWithCompanyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
          this.jobService = jobService;
    }

    @GetMapping()
    public ResponseEntity<List<JobWithCompanyDTO>> findAll(){
        return ResponseEntity.ok(jobService.findAll());
    }

    @PostMapping()
    public ResponseEntity<String> createJob(@RequestBody Job job){
        jobService.createJob(job);
        return new ResponseEntity<>("Job created", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> findById(@PathVariable long id){
        Job job = jobService.findById(id);
        if( null != job)
            return new ResponseEntity<>(job, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable long id){
        boolean isDeleted = jobService.deleteById(id);
        if(isDeleted)
            return new ResponseEntity<>("Job deleted", HttpStatus.OK);
        else
            return new ResponseEntity<>("Job not found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateJob(@PathVariable long id,@RequestBody Job job){
        boolean updated = jobService.updateJob(id,job);
        if(updated)
            return new ResponseEntity<>("Job updated", HttpStatus.OK);
        else
            return new ResponseEntity<>("Job not found", HttpStatus.NOT_FOUND);
    }
}
