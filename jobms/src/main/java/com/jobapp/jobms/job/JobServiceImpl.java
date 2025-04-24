package com.jobapp.jobms.job;

import com.jobapp.jobms.job.dto.JobDTO;
import com.jobapp.jobms.job.external.Company;
import com.jobapp.jobms.job.external.Review;
import com.jobapp.jobms.job.mapper.JobMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
    RestTemplate restTemplate;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public List<JobDTO> findAll(){
        List<Job> jobs = jobRepository.findAll();
        List<JobDTO> jobDTOS = new ArrayList<>();
        return jobs.stream().map(this::getJobWithCompanyDTO).collect(Collectors.toList());
    }

    private JobDTO getJobWithCompanyDTO(Job job) {
       // RestTemplate restTemplate = new RestTemplate();
        JobDTO jobDTO = new JobDTO();
        try {
            Company company = restTemplate.getForObject("http://COMPANYMS:8082/companies/" + job.getCompanyId(), Company.class);
            ResponseEntity<List<Review>> reviewResponse = restTemplate.exchange("http://REVIEWMS:8083/reviews?companyId="+ job.getCompanyId(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Review>>(){} );
            List<Review> reviews = reviewResponse.getBody();

            jobDTO = JobMapper.mapToJobWithCompanyDTO(job, company, reviews);
            jobDTO.setCompany(company);
            return jobDTO;
        }catch (HttpClientErrorException.NotFound e){ // To handle 404 from rest template since it does not return null, null check wont work if company does mot exist
            jobDTO = JobMapper.mapToJobWithCompanyDTO(job, new Company(), new ArrayList<>());
        }
        return jobDTO;
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
    public JobDTO findById(long id) {

        Job job = jobRepository.findById(id).orElse(null);
        return getJobWithCompanyDTO(job);
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
