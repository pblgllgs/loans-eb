package com.pblgllgs.loans.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pblgllgs.loans.config.LoansServiceConfig;
import com.pblgllgs.loans.model.Customer;
import com.pblgllgs.loans.model.Loans;
import com.pblgllgs.loans.model.Properties;
import com.pblgllgs.loans.repository.LoansRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LoansController {

    @Autowired
    private LoansRepository loansRepository;

    @Autowired
    LoansServiceConfig loansConfig;

    @PostMapping("/myLoans")
    public List<Loans> getLoansDetails(
            @RequestHeader("pblgllgs-correlation-id") String correlationid,
            @RequestBody Customer customer){
        List<Loans> loans
                = loansRepository.findByCustomerIdOrderByStartDtDesc(customer.getCustomerId());
        if(loans != null){
            return loans;
        }else {
            return null;
        }
    }

    @GetMapping("/loans/properties")
    public String getPropertyDetails() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(loansConfig.getMsg(), loansConfig.getBuildVersion(),
                loansConfig.getMailDetails(), loansConfig.getActiveBranches());
        String jsonStr = ow.writeValueAsString(properties);
        return jsonStr;
    }
}
