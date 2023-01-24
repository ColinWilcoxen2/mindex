package com.mindex.challenge.data;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class Compensation {
    //Added compensationId to allow updates instead of only inserts.
    @Id
    private String compensationId;
    private Employee employee;
    private double salary;
    private LocalDateTime effectiveDate;
    //In order to look up the object by employeeId I added it here.
    //I would have preferred to pull it from the Employee object but that hasn't
    //worked in the ways I have tried so this is the work around for now.
    //This cannot be set or retrieved by the user. It is only set on creation and when
    //The Employee object is updated.
    private String employeeId;

    public Compensation() {

    }
    public Compensation(Employee employee, double salary) {
        this.employee = employee;
        this.employeeId = employee == null ? null : employee.getEmployeeId();
        this.salary = salary;
        this.effectiveDate = LocalDateTime.now();
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        this.employeeId = employee == null ? null : employee.getEmployeeId();
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public LocalDateTime getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getCompensationId() {
        return compensationId;
    }

    public void setCompensationId(String compensationId) {
        this.compensationId = compensationId;
    }
}
