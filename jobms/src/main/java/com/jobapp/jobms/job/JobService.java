package com.jobapp.jobms.job;

import com.jobapp.jobms.job.dto.JobDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface JobService {

     List<JobDTO> findAll();
     String createJob(Job job);
     JobDTO findById(long id);
     boolean deleteById(long id);
     boolean updateJob(long id, Job job);
}
