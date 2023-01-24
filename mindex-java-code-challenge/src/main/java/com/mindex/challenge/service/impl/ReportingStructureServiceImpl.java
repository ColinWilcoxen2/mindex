package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    @Autowired
    EmployeeService employeeService;

    /**
     * Function to recursively determine the number of reports for an employee.
     * @param emp The employee who we are calculating reports for.
     * @return The number of reports for emp.
     */
    public int getNumberOfReports(Employee emp) {
        int totalReports = 0;

        //If we get to an employee with no reports we are at the end of our tree so we return.
        if(emp == null || emp.getDirectReports() == null || emp.getDirectReports().isEmpty()) {
            return totalReports;
        } else { //If this employee has reports we add the number to our total.
            totalReports += emp.getDirectReports().size();
        }

        //Loop through reports to look for their reports.
        for(Employee e : emp.getDirectReports()) {
            //Add each report in the trees reports to the total.
            //Requires look up of employee to make sure we have direct report data.
            totalReports += getNumberOfReports(employeeService.read(e.getEmployeeId()));
        }

        return totalReports;
    }

}
