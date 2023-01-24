package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CompensationServiceImpl implements CompensationService {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);
    @Autowired
    private CompensationRepository compensationRepository;
    @Autowired
    private EmployeeService employeeService;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        try {
            employeeService.read(compensation.getEmployee().getEmployeeId());
        } catch(RuntimeException ex) {
            throw new RuntimeException("Invalid employee ID.");
        }

        //Assign random ID. Could assign this in a more programmatic way but this
        //works for our needs.
        compensation.setCompensationId(UUID.randomUUID().toString());
        //Set effective date to now.
        compensation.setEffectiveDate(LocalDateTime.now());

        //Check for old compensation record. Since we are assigning random IDs we need to look this up
        //manually. It won't be found automatically.
        Compensation oldComp = compensationRepository.findByEmployeeId(compensation.getEmployee().getEmployeeId());
        if(oldComp != null) {
            //If we find an old compensation record for this employee we update it instead
            //of creating a new one.
            oldComp.setSalary(compensation.getSalary());
            oldComp.setEffectiveDate(compensation.getEffectiveDate());
            compensationRepository.save(oldComp);
            return oldComp;
        }

        //We didnt find an old one so we create a new Compensation.
        compensationRepository.save(compensation);

        return compensation;
    }

    @Override
    public Compensation read(Employee employee) {
        LOG.debug("Reading compensation with employeeId [{}]", employee.getEmployeeId());

        Compensation compensation = compensationRepository.findByEmployeeId(employee.getEmployeeId());

        if (compensation == null) {
            throw new RuntimeException("No compensation found for employee ID: " + employee.getEmployeeId());
        }

        return compensation;
    }
}
