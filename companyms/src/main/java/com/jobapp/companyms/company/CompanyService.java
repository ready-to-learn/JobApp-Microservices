package com.jobapp.companyms.company;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CompanyService {
    List<Company>  findAll();
    String createCompany(Company company);
    boolean updateCompany(Long id,Company company);
    boolean deleteCompany(Long id);
    Company findById(Long id);
}
