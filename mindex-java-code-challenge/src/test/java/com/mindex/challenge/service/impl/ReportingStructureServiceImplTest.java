package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.ReportingStructure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    public static final String DIRECT_REPORTS_EMPLOYEE_ID = "16a596ae-edd3-4847-99fe-c4518e82c86f";
    public static final String NO_DIRECT_REPORTS_EMPLOYEE_ID = "b7839309-3348-463b-a7e3-5de1c168beb3";
    private String reportingStructureURL;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Before
    public void setup() {
        reportingStructureURL = "http://localhost:" + port + "/reportingStructure/{employeeId}";
    }

    @Test
    public void testGetNumberOfReports_nonZero() {
        ReportingStructure reportingStructure = restTemplate.getForEntity(reportingStructureURL, ReportingStructure.class, DIRECT_REPORTS_EMPLOYEE_ID).getBody();

        assertEquals(4,reportingStructure.getNumberOfReports());
    }

    @Test
    public void testGetNumberOfReports_zero() {
        ReportingStructure reportingStructure = restTemplate.getForEntity(reportingStructureURL, ReportingStructure.class, NO_DIRECT_REPORTS_EMPLOYEE_ID).getBody();

        assertEquals(0,reportingStructure.getNumberOfReports());
    }
}
