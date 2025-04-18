package com.jobapp.companyms.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company>findAll() {
        return companyRepository.findAll();
    }

    @Override
    public String createCompany(Company company) {
        try {
            companyRepository.save(company);
            return "Company created";
        }catch(Exception e){
            e.printStackTrace();
            return "Error creating company";
        }
    }

    @Override
    public boolean updateCompany(Long id, Company updatedCompany) {
        return companyRepository.findById(id)
                .map(
                        company -> {
                        company.setName(updatedCompany.getName());
                        company.setDescription(updatedCompany.getDescription());
                        companyRepository.save(company);
                        return true;
                        })
                .orElse(false);
    }

    @Override
    public boolean deleteCompany(Long id) {
        try{
            companyRepository.deleteById(id);
            return true;
        }catch (EmptyResultDataAccessException e){
            return false;
        }
    }

    @Override
    public Company findById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }
}
