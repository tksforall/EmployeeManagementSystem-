package model;

public class SalaryBonus {
    private int salaryId;
    private int jobId;
    private double amount;
    private Double annual;
    private double bonus;

    public SalaryBonus() {}

    public SalaryBonus(int salaryId, int jobId, double amount, Double annual, double bonus) {
        this.salaryId = salaryId;
        this.jobId = jobId;
        this.amount = amount;
        this.annual = annual;
        this.bonus = bonus;
    }

    // Getters and Setters
    public int getSalaryId() { return salaryId; }
    public void setSalaryId(int salaryId) { this.salaryId = salaryId; }

    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Double getAnnual() { return annual; }
    public void setAnnual(Double annual) { this.annual = annual; }

    public double getBonus() { return bonus; }
    public void setBonus(double bonus) { this.bonus = bonus; }

    public double getTotalSalary() {
        return amount + (annual != null ? annual : 0) + bonus;
    }
}