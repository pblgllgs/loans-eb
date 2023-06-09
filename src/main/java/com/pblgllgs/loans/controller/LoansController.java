package com.pblgllgs.loans.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pblgllgs.loans.config.LoansServiceConfig;
import com.pblgllgs.loans.model.Customer;
import com.pblgllgs.loans.model.Loans;
import com.pblgllgs.loans.model.Properties;
import com.pblgllgs.loans.repository.LoansRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class LoansController {

    private static final Logger logger = LoggerFactory.getLogger(LoansController.class);
    @Autowired
    private LoansRepository loansRepository;

    @Autowired
    LoansServiceConfig loansConfig;

    @PostMapping("/myLoans")
    public List<Loans> getLoansDetails(
            @RequestHeader("pblgllgs-correlation-id") String correlationid,
            @RequestBody Customer customer){
        logger.info("getLoansDetails() method started");
        List<Loans> loans
                = loansRepository.findByCustomerIdOrderByStartDtDesc(customer.getCustomerId());
        logger.info("getLoansDetails() method ended");
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
