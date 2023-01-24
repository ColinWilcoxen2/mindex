package com.mindex.challenge.controller;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportingStructureController {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureController.class);

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ReportingStructureService reportingStructureService;

    @GetMapping("/reportingStructure/{employeeId}")
    public ReportingStructure read(@PathVariable String employeeId) {
        LOG.debug("Received reporting structure read request for employee id [{}]", employeeId);

        ReportingStructure reportingStructure = new ReportingStructure(employeeService.read(employeeId));

        //Needed external service to calculate reports because in some cases our direct reports only contain
        //ids and no data about their direct reports. This requires me to look up the employees complete
        //information which I decided to do outside of ReportingStructure.java and put into a separate class.
        reportingStructure.setNumberOfReports(reportingStructureService.getNumberOfReports(reportingStructure.getEmployee()));

        return reportingStructure;
    }
}
