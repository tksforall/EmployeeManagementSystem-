package dao;

import model.JobDepartment;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobDepartmentDAO {

    public List<JobDepartment> getAllJobDepartments() throws SQLException {
        List<JobDepartment> list = new ArrayList<>();
        String sql = "SELECT * FROM job_department ORDER BY job_id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                JobDepartment jd = new JobDepartment();
                jd.setJobId(rs.getInt("job_id"));
                jd.setJobDept(rs.getString("job_dept"));
                jd.setName(rs.getString("name"));
                jd.setDescription(rs.getString("description"));
                jd.setSalaryRange(rs.getString("salary_range"));
                list.add(jd);
            }
        }
        return list;
    }

    public JobDepartment getJobDepartmentById(int id) throws SQLException {
        String sql = "SELECT * FROM job_department WHERE job_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JobDepartment jd = new JobDepartment();
                jd.setJobId(rs.getInt("job_id"));
                jd.setJobDept(rs.getString("job_dept"));
                jd.setName(rs.getString("name"));
                jd.setDescription(rs.getString("description"));
                jd.setSalaryRange(rs.getString("salary_range"));
                return jd;
            }
        }
        return null;
    }
}