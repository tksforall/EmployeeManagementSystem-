package dao;

import model.Employee;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.*, j.job_dept, s.amount " +
                "FROM employee e " +
                "LEFT JOIN job_department j ON e.job_id = j.job_id " +
                "LEFT JOIN salary_bonus s ON j.job_id = s.job_id " +
                "ORDER BY e.emp_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Employee emp = extractEmployee(rs);
                emp.setJobDeptName(rs.getString("job_dept"));
                emp.setBasicSalary(rs.getDouble("amount"));
                employees.add(emp);
            }
        }
        return employees;
    }

    public Employee getEmployeeById(int id) throws SQLException {
        String sql = "SELECT e.*, j.job_dept, s.amount " +
                "FROM employee e " +
                "LEFT JOIN job_department j ON e.job_id = j.job_id " +
                "LEFT JOIN salary_bonus s ON j.job_id = s.job_id " +
                "WHERE e.emp_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Employee emp = extractEmployee(rs);
                emp.setJobDeptName(rs.getString("job_dept"));
                emp.setBasicSalary(rs.getDouble("amount"));
                return emp;
            }
        }
        return null;
    }

    public void addEmployee(Employee emp) throws SQLException {
        String sql = "INSERT INTO employee (fname, lname, gender, age, contact_add, emp_email, job_id, hire_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, emp.getFname());
            stmt.setString(2, emp.getLname());
            stmt.setString(3, emp.getPosition());   // ← THÊM
            stmt.setString(4, emp.getGender());
            stmt.setInt(5, emp.getAge());
            stmt.setString(6, emp.getContactAdd());
            stmt.setString(7, emp.getEmpEmail());
            stmt.setInt(8, emp.getJobId());
            stmt.setDate(9, new java.sql.Date(emp.getHireDate().getTime()));

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                emp.setEmpId(rs.getInt(1));
            }
        }
    }

    public void updateEmployee(Employee emp) throws SQLException {
        String sql = "UPDATE employee SET fname=?, lname=?, gender=?, age=?, contact_add=?, emp_email=?, job_id=?, hire_date=? " +
                "WHERE emp_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emp.getFname());
            stmt.setString(2, emp.getLname());
            stmt.setString(3, emp.getPosition());   // ← THÊM
            stmt.setString(4, emp.getGender());
            stmt.setInt(5, emp.getAge());
            stmt.setString(6, emp.getContactAdd());
            stmt.setString(7, emp.getEmpEmail());
            stmt.setInt(8, emp.getJobId());
            stmt.setDate(9, new java.sql.Date(emp.getHireDate().getTime()));
            stmt.setInt(10, emp.getEmpId());

            stmt.executeUpdate();
        }
    }

    public void updateSalary(int empId, double newSalary) throws SQLException {
        String getJobSql = "SELECT job_id FROM employee WHERE emp_id = ?";
        int jobId = -1;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(getJobSql)) {
            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                jobId = rs.getInt("job_id");
            }
        }
        if (jobId != -1) {
            String updateSql = "UPDATE salary_bonus SET amount = ? WHERE job_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setDouble(1, newSalary);
                ps.setInt(2, jobId);
                ps.executeUpdate();
            }
        }
    }

    public void deleteEmployee(int id) throws SQLException {
        String sql = "DELETE FROM employee WHERE emp_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Employee> searchEmployees(String keyword) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllEmployees();
        }

        String sql = "SELECT e.*,e.position, j.job_dept, s.amount " +
                "FROM employee e " +
                "LEFT JOIN job_department j ON e.job_id = j.job_id " +
                "LEFT JOIN salary_bonus s ON j.job_id = s.job_id " +
                "WHERE LOWER(e.fname) LIKE LOWER(?) OR LOWER(e.lname) LIKE LOWER(?) " +
                "ORDER BY e.emp_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String likeKeyword = "%" + keyword.trim() + "%";
            stmt.setString(1, likeKeyword);
            stmt.setString(2, likeKeyword);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Employee emp = extractEmployee(rs);
                emp.setJobDeptName(rs.getString("job_dept"));
                emp.setBasicSalary(rs.getDouble("amount"));
                employees.add(emp);
            }
        }
        return employees;
    }

    private Employee extractEmployee(ResultSet rs) throws SQLException {
        Employee emp = new Employee();
        emp.setEmpId(rs.getInt("emp_id"));
        emp.setFname(rs.getString("fname"));
        emp.setLname(rs.getString("lname"));
        emp.setPosition(rs.getString("position"));
        emp.setGender(rs.getString("gender"));
        emp.setAge(rs.getInt("age"));
        emp.setContactAdd(rs.getString("contact_add"));
        emp.setEmpEmail(rs.getString("emp_email"));
        emp.setJobId(rs.getInt("job_id"));
        emp.setHireDate(rs.getDate("hire_date"));
        return emp;
    }
}