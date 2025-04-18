package com.jobapp.companyms.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping()
    public ResponseEntity<List<Company>> findAll(){
        List<Company> companies = companyService.findAll();
        if (!companies.isEmpty())
            return new ResponseEntity<>(companies, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> findById(@PathVariable Long id) {
        Company company = companyService.findById(id);
        if (company != null)
            return new ResponseEntity<>(company, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }



    @PostMapping()
    public ResponseEntity<String> create(@RequestBody Company company){
        return new ResponseEntity<>(companyService.createCompany(company),HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id,@RequestBody Company company){
        boolean isUpdated = companyService.updateCompany(id,company);
        if(isUpdated)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        boolean isDeleted = companyService.deleteCompany(id);
        if(isDeleted)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}
