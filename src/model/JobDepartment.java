package model;

public class JobDepartment {
    private int jobId;
    private String jobDept;
    private String name;
    private String description;
    private String salaryRange;

    public JobDepartment() {}

    public JobDepartment(int jobId, String jobDept, String name, String description, String salaryRange) {
        this.jobId = jobId;
        this.jobDept = jobDept;
        this.name = name;
        this.description = description;
        this.salaryRange = salaryRange;
    }

    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }

    public String getJobDept() { return jobDept; }
    public void setJobDept(String jobDept) { this.jobDept = jobDept; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSalaryRange() { return salaryRange; }
    public void setSalaryRange(String salaryRange) { this.salaryRange = salaryRange; }

    @Override
    public String toString() {
        return jobDept + " - " + name;
    }
}