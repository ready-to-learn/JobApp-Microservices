package com.jobapp.jobms.job;

import com.jobapp.jobms.job.dto.JobWithCompanyDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface JobService {

     List<JobWithCompanyDTO> findAll();
     String createJob(Job job);
     Job findById(long id);
     boolean deleteById(long id);
     boolean updateJob(long id, Job job);
}
