package dao;

import model.Payroll;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PayrollDAO {

    public List<Payroll> getAllPayrolls() throws SQLException {
        List<Payroll> list = new ArrayList<>();
        String sql = "SELECT p.*, CONCAT(e.fname, ' ', e.lname) AS emp_name, j.job_dept " +
                "FROM payroll p " +
                "JOIN employee e ON p.emp_id = e.emp_id " +
                "LEFT JOIN job_department j ON p.job_id = j.job_id " +
                "ORDER BY p.payroll_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Payroll p = new Payroll();
                p.setPayrollId(rs.getInt("payroll_id"));
                p.setEmpId(rs.getInt("emp_id"));
                p.setEmployeeName(rs.getString("emp_name"));
                p.setJobDeptName(rs.getString("job_dept"));
                p.setPayrollDate(rs.getDate("payroll_date"));
                p.setTotalAmount(rs.getDouble("total_amount"));
                p.setReport(rs.getString("report"));
                list.add(p);
            }
        }
        return list;
    }

    public void addPayroll(Payroll payroll) throws SQLException {
        String sql = "INSERT INTO payroll (emp_id, job_id, salary_id, leave_id, payroll_date, report, total_amount) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, payroll.getEmpId());
            stmt.setNull(2, Types.INTEGER);
            stmt.setNull(3, Types.INTEGER);
            stmt.setNull(4, Types.INTEGER);
            stmt.setDate(5, new java.sql.Date(payroll.getPayrollDate().getTime()));
            stmt.setString(6, payroll.getReport());
            stmt.setDouble(7, payroll.getTotalAmount());
            stmt.executeUpdate();
        }
    }

    public void deletePayroll(int id) throws SQLException {
        String sql = "DELETE FROM payroll WHERE payroll_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}