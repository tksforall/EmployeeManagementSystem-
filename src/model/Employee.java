package model;

import java.util.Date;

public class Employee {
    private int empId;
    private String fname;
    private String lname;
    private String position;
    private String gender;
    private int age;
    private String contactAdd;
    private String empEmail;
    private int jobId;
    private Date hireDate;

    // Display fields (from JOIN)
    private String jobDeptName;
    private double basicSalary;

    public Employee() {}

    public Employee(int empId, String fname, String lname, String gender, int age,
                    String contactAdd, String empEmail, int jobId, Date hireDate) {
        this.empId = empId;
        this.fname = fname;
        this.lname = lname;
        this.position = position;
        this.gender = gender;
        this.age = age;
        this.contactAdd = contactAdd;
        this.empEmail = empEmail;
        this.jobId = jobId;
        this.hireDate = hireDate;
    }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public int getEmpId() { return empId; }
    public void setEmpId(int empId) { this.empId = empId; }

    public String getFname() { return fname; }
    public void setFname(String fname) { this.fname = fname; }

    public String getLname() { return lname; }
    public void setLname(String lname) { this.lname = lname; }

    public String getFullName() { return fname + " " + lname; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getContactAdd() { return contactAdd; }
    public void setContactAdd(String contactAdd) { this.contactAdd = contactAdd; }

    public String getEmpEmail() { return empEmail; }
    public void setEmpEmail(String empEmail) { this.empEmail = empEmail; }

    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }

    public Date getHireDate() { return hireDate; }
    public void setHireDate(Date hireDate) { this.hireDate = hireDate; }

    public String getJobDeptName() { return jobDeptName; }
    public void setJobDeptName(String jobDeptName) { this.jobDeptName = jobDeptName; }

    public double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }

    @Override
    public String toString() {
        return empId + " | " + getFullName() + " | " + empEmail;
    }
}