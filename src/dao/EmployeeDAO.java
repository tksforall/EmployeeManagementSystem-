package dao;

import model.Employee;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.*, j.job_dept, e.basic_salary " +
                "FROM employee e " +
                "LEFT JOIN job_department j ON e.job_id = j.job_id " +
                "ORDER BY e.emp_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Employee emp = extractEmployee(rs);
                emp.setJobDeptName(rs.getString("job_dept"));
                employees.add(emp);
            }
        }
        return employees;
    }

    public Employee getEmployeeById(int id) throws SQLException {
        String sql = "SELECT e.*, j.job_dept, e.basic_salary " +
                "FROM employee e " +
                "LEFT JOIN job_department j ON e.job_id = j.job_id " +
                "WHERE e.emp_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Employee emp = extractEmployee(rs);
                emp.setJobDeptName(rs.getString("job_dept"));
                return emp;
            }
        }
        return null;
    }

    public void addEmployee(Employee emp) throws SQLException {
        String sql = "INSERT INTO employee (fname, lname, position, gender, age, contact_add, emp_email, job_id, hire_date, basic_salary) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, emp.getFname());
            stmt.setString(2, emp.getLname());
            stmt.setString(3, emp.getPosition());
            stmt.setString(4, emp.getGender());
            stmt.setInt(5, emp.getAge());
            stmt.setString(6, emp.getContactAdd());
            stmt.setString(7, emp.getEmpEmail());
            stmt.setInt(8, emp.getJobId());
            stmt.setDate(9, new java.sql.Date(emp.getHireDate().getTime()));
            stmt.setDouble(10, emp.getBasicSalary());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                emp.setEmpId(rs.getInt(1));
            }
        }
    }

    public void updateEmployee(Employee emp) throws SQLException {
        String sql = "UPDATE employee SET fname=?, lname=?, position=?, gender=?, age=?, contact_add=?, emp_email=?, job_id=?, hire_date=?, basic_salary=? " +
                "WHERE emp_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emp.getFname());
            stmt.setString(2, emp.getLname());
            stmt.setString(3, emp.getPosition());
            stmt.setString(4, emp.getGender());
            stmt.setInt(5, emp.getAge());
            stmt.setString(6, emp.getContactAdd());
            stmt.setString(7, emp.getEmpEmail());
            stmt.setInt(8, emp.getJobId());
            stmt.setDate(9, new java.sql.Date(emp.getHireDate().getTime()));
            stmt.setDouble(10, emp.getBasicSalary());
            stmt.setInt(11, emp.getEmpId());

            stmt.executeUpdate();
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

        String sql = "SELECT e.*, j.job_dept, e.basic_salary " +
                "FROM employee e " +
                "LEFT JOIN job_department j ON e.job_id = j.job_id " +
                "WHERE LOWER(e.fname) LIKE LOWER(?) " +
                "OR LOWER(e.lname) LIKE LOWER(?) " +
                "OR LOWER(CONCAT(e.fname, ' ', e.lname)) LIKE LOWER(?) " +
                "ORDER BY e.emp_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String likeKeyword = "%" + keyword.trim() + "%";
            stmt.setString(1, likeKeyword);
            stmt.setString(2, likeKeyword);
            stmt.setString(3, likeKeyword);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Employee emp = extractEmployee(rs);
                emp.setJobDeptName(rs.getString("job_dept"));
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
        emp.setBasicSalary(rs.getDouble("basic_salary"));
        emp.setJobId(rs.getInt("job_id"));
        emp.setHireDate(rs.getDate("hire_date"));
        return emp;
    }
}