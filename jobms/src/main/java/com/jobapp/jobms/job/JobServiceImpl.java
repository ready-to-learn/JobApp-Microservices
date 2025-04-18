package com.jobapp.jobms.job;

import com.jobapp.jobms.job.dto.JobWithCompanyDTO;
import com.jobapp.jobms.job.external.Company;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public List<JobWithCompanyDTO> findAll(){
        List<Job> jobs = jobRepository.findAll();
        List<JobWithCompanyDTO> jobWithCompanyDTOs = new ArrayList<>();
        return jobs.stream().map(this::getJobWithCompanyDTO).collect(Collectors.toList());
    }

    private JobWithCompanyDTO getJobWithCompanyDTO(Job job) {
        JobWithCompanyDTO jobWithCompanyDTO = new JobWithCompanyDTO();
        jobWithCompanyDTO.setJob(job);
        RestTemplate restTemplate = new RestTemplate();
        try {
            Company company = restTemplate.getForObject("http://localhost:8082/companies/" + job.getCompanyId(), Company.class);
            jobWithCompanyDTO.setCompany(company);
        }catch (HttpClientErrorException.NotFound e){ // To handle 404 from rest template since it does not return null, null check wont work if company does mot exist
            jobWithCompanyDTO.setCompany(new Company());
        }
        return jobWithCompanyDTO;
    }
    @Override
    @Transactional
    public String createJob(Job job){
        try{
            jobRepository.save(job);
            return "Job created";
        }catch (Exception e){
            e.printStackTrace();
            return "Job could not be created";
        }
    }

    @Override
    public Job findById(long id) {
        return jobRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteById(long id) {
        try {
            jobRepository.deleteById(id);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public boolean updateJob(long id, Job updatedJob) {
       return jobRepository.findById(id)
               .map(
                       job -> {
                           job.setTitle(updatedJob.getTitle());
                           job.setDescription(updatedJob.getDescription());
                           job.setMinSalary(updatedJob.getMinSalary());
                           job.setMaxSalary(updatedJob.getMaxSalary());
                           job.setLocation(updatedJob.getLocation());
                           jobRepository.save(job);
                           return true;
                       }
               ).orElse(false);

    }

}
