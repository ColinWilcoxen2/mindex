package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationCreateURL;
    private String compensationReadURL;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private Employee employee;

    @Before
    public void setup() {
        compensationCreateURL = "http://localhost:" + port + "/compensation";
        compensationReadURL = "http://localhost:" + port + "/compensation/{employeeId}";
        employee = new Employee();
        employee.setFirstName("Paul");
        employee.setLastName("McCartney");
        employee.setDepartment("Engineering");
        employee.setPosition("Developer I");
        employee.setEmployeeId("b7839309-3348-463b-a7e3-5de1c168beb3");
    }

    @Test
    public void testCreate_success() {
        Compensation compensation = new Compensation(employee,100000);

        Compensation createdComp = restTemplate.postForEntity(compensationCreateURL, compensation, Compensation.class).getBody();

        assertNotNull(createdComp.getCompensationId());
        assertCompensationEquivalence(compensation,createdComp);

    }

    @Test
    public void testCreate_fail() {
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setDepartment("Engineering");
        employee.setPosition("Developer");

        Compensation compensation = new Compensation(employee,100000);

        Compensation createdComp = restTemplate.postForEntity(compensationCreateURL, compensation, Compensation.class).getBody();

        assertNull(createdComp.getCompensationId());
    }

    @Test
    public void testCreateDuplicate() {
        Compensation compensation = new Compensation(employee,100000);
        Compensation compensation2 = new Compensation(employee, 100001);

        Compensation createdComp = restTemplate.postForEntity(compensationCreateURL, compensation, Compensation.class).getBody();
        createdComp = restTemplate.postForEntity(compensationCreateURL, compensation2, Compensation.class).getBody();

        assertNotNull(createdComp.getCompensationId());
        assertCompensationEquivalence(compensation2,createdComp);
    }

    @Test
    public void testRead_success() {
        Compensation compensation = new Compensation(employee,100000);

        Compensation createdComp = restTemplate.postForEntity(compensationCreateURL, compensation, Compensation.class).getBody();

        Compensation readComp = restTemplate.getForEntity(compensationReadURL, Compensation.class, createdComp.getEmployee().getEmployeeId()).getBody();

        assertNotNull(readComp.getCompensationId());
        assertCompensationEquivalence(createdComp,readComp);

    }

    @Test
    public void testRead_fail() {
        Compensation readComp = restTemplate.getForEntity(compensationReadURL, Compensation.class, "00aa1462-ffa9-4978-901b-7c001562cf6f").getBody();

        assertNull(readComp.getCompensationId());
    }

    private static void assertCompensationEquivalence(Compensation first, Compensation second) {
        assertEquals(first.getSalary(), second.getSalary(), 0);
        assertEquals(first.getEmployee().getEmployeeId(), second.getEmployee().getEmployeeId());
    }

}
