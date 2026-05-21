package ui;

import dao.EmployeeDAO;
import dao.JobDepartmentDAO;
import model.Employee;
import model.JobDepartment;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class AddEmployeeDialog extends JDialog {
    private EmployeeDAO employeeDAO;
    private JobDepartmentDAO jobDepartmentDAO;
    private JTextField txtFname, txtLname, txtPosition, txtAge, txtContact, txtEmail,txtSalary;
    private JComboBox<String> cbGender;
    private JComboBox<JobDepartment> cbJob;
    private JSpinner dateSpinner;

    public AddEmployeeDialog(JFrame parent, EmployeeDAO empDAO) {
        super(parent, "Add New Employee", true);
        this.employeeDAO = empDAO;
        this.jobDepartmentDAO = new JobDepartmentDAO();

        setSize(450, 500);
        setLocationRelativeTo(parent);
        initUI();
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
        add(new JLabel("Basic Salary:"), gbc);
        gbc.gridx = 1;
        txtSalary = new JTextField(15);
        add(txtSalary, gbc);
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
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, gbc);

        btnSave.addActionListener(e -> saveEmployee());
        btnCancel.addActionListener(e -> dispose());
    }

    private void saveEmployee() {
        try {
            String fname = txtFname.getText().trim();
            String lname = txtLname.getText().trim();
            String position = txtPosition.getText().trim();
            if (fname.isEmpty() || lname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "First name and last name cannot be empty!");
                return;
            }

            int age = Integer.parseInt(txtAge.getText().trim());
            if (age < 18 || age > 100) {
                JOptionPane.showMessageDialog(this, "Age must be between 18 and 100!");
                return;
            }

            double salary = Double.parseDouble(txtSalary.getText().trim());
            if (salary <= 0) {
                JOptionPane.showMessageDialog(this, "Salary must be greater than 0!");
                return;
            }

            Employee emp = new Employee();
            emp.setFname(fname);
            emp.setLname(lname);
            emp.setPosition(position);
            emp.setGender((String) cbGender.getSelectedItem());
            emp.setAge(age);
            emp.setContactAdd(txtContact.getText().trim());
            emp.setEmpEmail(txtEmail.getText().trim());
            emp.setJobId(((JobDepartment) cbJob.getSelectedItem()).getJobId());
            emp.setHireDate((Date) dateSpinner.getValue());
            emp.setBasicSalary(salary);

            employeeDAO.addEmployee(emp);
            JOptionPane.showMessageDialog(this, "Employee added successfully!");
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age and Salary must be numbers!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}