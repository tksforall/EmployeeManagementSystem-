package ui;

import dao.EmployeeDAO;
import dao.JobDepartmentDAO;
import model.Employee;
import model.JobDepartment;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class UpdateEmployeeDialog extends JDialog {
    private EmployeeDAO employeeDAO;
    private JobDepartmentDAO jobDepartmentDAO;
    private Employee employee;
    private JTextField txtFname, txtLname, txtAge, txtContact, txtEmail;
    private JComboBox<String> cbGender;
    private JComboBox<JobDepartment> cbJob;
    private JSpinner dateSpinner;
    private JTextField txtPosition, txtSalary;

    public UpdateEmployeeDialog(JFrame parent, EmployeeDAO empDAO, Employee emp) {
        super(parent, "Update Employee", true);
        this.employeeDAO = empDAO;
        this.jobDepartmentDAO = new JobDepartmentDAO();
        this.employee = emp;

        setSize(450, 500);
        setLocationRelativeTo(parent);
        initUI();
        loadEmployeeData();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        txtFname = new JTextField(15);
        add(txtFname, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        txtLname = new JTextField(15);
        add(txtLname, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel("Position:"), gbc);
        gbc.gridx = 1;
        txtPosition = new JTextField(15);
        add(txtPosition, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel("Basic Salary:"), gbc);
        gbc.gridx = 1;
        txtSalary = new JTextField(15);
        add(txtSalary, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        cbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        add(cbGender, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        txtAge = new JTextField(15);
        add(txtAge, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        txtContact = new JTextField(15);
        add(txtContact, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(15);
        add(txtEmail, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        try {
            List<JobDepartment> jobs = jobDepartmentDAO.getAllJobDepartments();
            cbJob = new JComboBox<>(jobs.toArray(new JobDepartment[0]));
        } catch (Exception e) {
            cbJob = new JComboBox<>();
        }
        add(cbJob, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel("Hire Date:"), gbc);
        gbc.gridx = 1;
        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
        add(dateSpinner, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnUpdate = new JButton("Update");
        JButton btnCancel = new JButton("Cancel");
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnCancel);
        add(buttonPanel, gbc);

        btnUpdate.addActionListener(e -> updateEmployee());
        btnCancel.addActionListener(e -> dispose());
    }

    private void loadEmployeeData() {
        txtFname.setText(employee.getFname());
        txtLname.setText(employee.getLname());
        txtPosition.setText(employee.getPosition());
        txtSalary.setText(String.format("%.0f", employee.getBasicSalary()));
        cbGender.setSelectedItem(employee.getGender());
        txtAge.setText(String.valueOf(employee.getAge()));
        txtContact.setText(employee.getContactAdd());
        txtEmail.setText(employee.getEmpEmail());
        dateSpinner.setValue(employee.getHireDate());

        for (int i = 0; i < cbJob.getItemCount(); i++) {
            if (cbJob.getItemAt(i).getJobId() == employee.getJobId()) {
                cbJob.setSelectedIndex(i);
                break;
            }
        }
    }

    private void updateEmployee() {
        try {
            String fname = txtFname.getText().trim();
            String lname = txtLname.getText().trim();
            if (fname.isEmpty() || lname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "First name and last name cannot be empty!");
                return;


            }

            employee.setPosition(txtPosition.getText().trim());
            double newSalary = Double.parseDouble(txtSalary.getText().trim());
            employeeDAO.updateSalary(employee.getEmpId(), newSalary);

            int age = Integer.parseInt(txtAge.getText().trim());
            if (age < 18 || age > 100) {
                JOptionPane.showMessageDialog(this, "Age must be between 18 and 100!");
                return;
            }

            employee.setFname(fname);
            employee.setLname(lname);
            employee.setGender((String) cbGender.getSelectedItem());
            employee.setAge(age);
            employee.setContactAdd(txtContact.getText().trim());
            employee.setEmpEmail(txtEmail.getText().trim());
            employee.setJobId(((JobDepartment) cbJob.getSelectedItem()).getJobId());
            employee.setHireDate((Date) dateSpinner.getValue());

            employeeDAO.updateEmployee(employee);
            JOptionPane.showMessageDialog(this, "Employee updated successfully!");
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be a number!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}