package model;

import java.util.Date;

public class Payroll {
    private int payrollId;
    private int empId;
    private String employeeName;
    private String jobDeptName;
    private Date payrollDate;
    private double totalAmount;
    private String report;

    public Payroll() {}

    public Payroll(int payrollId, int empId, String employeeName, String jobDeptName,
                   Date payrollDate, double totalAmount, String report) {
        this.payrollId = payrollId;
        this.empId = empId;
        this.employeeName = employeeName;
        this.jobDeptName = jobDeptName;
        this.payrollDate = payrollDate;
        this.totalAmount = totalAmount;
        this.report = report;
    }

    public int getPayrollId() { return payrollId; }
    public void setPayrollId(int payrollId) { this.payrollId = payrollId; }

    public int getEmpId() { return empId; }
    public void setEmpId(int empId) { this.empId = empId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getJobDeptName() { return jobDeptName; }
    public void setJobDeptName(String jobDeptName) { this.jobDeptName = jobDeptName; }

    public Date getPayrollDate() { return payrollDate; }
    public void setPayrollDate(Date payrollDate) { this.payrollDate = payrollDate; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getReport() { return report; }
    public void setReport(String report) { this.report = report; }
}