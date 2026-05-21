package ui;

import dao.EmployeeDAO;
import model.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.PrinterException;
import java.util.List;

public class MainOperationsFrame extends JFrame {
    private EmployeeDAO employeeDAO;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public MainOperationsFrame() {
        employeeDAO = new EmployeeDAO();
        setTitle("EMPLOYEE MANAGEMENT SYSTEM");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        initUI();
        loadAllEmployees();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Employee Operations"));

        JPanel searchAndActionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));

        searchAndActionPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(150, 26));
        searchAndActionPanel.add(searchField);

        JButton btnSearch = createActionButton("SEARCH");
        JButton btnRefresh = createActionButton("REFRESH");
        JButton btnUpdate = createActionButton("UPDATE");
        JButton btnRemove = createActionButton("REMOVE");
        JButton btnPrint = createActionButton("PRINT");

        searchAndActionPanel.add(btnSearch);
        searchAndActionPanel.add(btnRefresh);
        searchAndActionPanel.add(btnUpdate);
        searchAndActionPanel.add(btnRemove);
        searchAndActionPanel.add(btnPrint);

        topPanel.add(searchAndActionPanel, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Full Name","Position", "Gender", "Age", "Email","Address", "Department", "Salary"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        employeeTable = new JTable(tableModel);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < employeeTable.getColumnCount(); i++) {
            employeeTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) employeeTable.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);

        employeeTable.setFont(new Font("Arial", Font.PLAIN, 15));
        employeeTable.setRowHeight(28);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);

        btnSearch.addActionListener(e -> searchEmployees());
        btnRefresh.addActionListener(e -> loadAllEmployees());
        btnUpdate.addActionListener(e -> updateSelectedEmployee());
        btnRemove.addActionListener(e -> deleteSelectedEmployee());
        btnPrint.addActionListener(e -> printTable());
    }

    private JButton createActionButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setBackground(new Color(230, 230, 230));
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(85, 26));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void loadAllEmployees() {
        try {
            List<Employee> employees = employeeDAO.getAllEmployees();
            displayEmployees(employees);
            searchField.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void searchEmployees() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadAllEmployees();
            return;
        }
        try {
            List<Employee> employees = employeeDAO.searchEmployees(keyword);
            displayEmployees(employees);
            if (employees.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No employees found!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Search error: " + e.getMessage());
        }
    }

    private void displayEmployees(List<Employee> employees) {
        tableModel.setRowCount(0);
        for (Employee emp : employees) {
            String salaryStr = (emp.getBasicSalary() > 0)
                    ? String.format("%,.0f", emp.getBasicSalary()) + " VND"
                    : "0 VND";
            tableModel.addRow(new Object[]{
                    emp.getEmpId(),
                    emp.getFullName(),
                    emp.getPosition(),
                    emp.getGender(),
                    emp.getAge(),
                    emp.getEmpEmail(),
                    emp.getContactAdd() != null ? emp.getContactAdd() : "",
                    emp.getJobDeptName() != null ? emp.getJobDeptName() : "",
                    salaryStr
            });
        }
    }

    private void updateSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to update!");
            return;
        }

        int empId = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            Employee emp = employeeDAO.getEmployeeById(empId);
            if (emp != null) {
                UpdateEmployeeDialog dialog = new UpdateEmployeeDialog(this, employeeDAO, emp);
                dialog.setVisible(true);
                loadAllEmployees();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to remove!");
            return;
        }

        String empName = (String) tableModel.getValueAt(selectedRow, 1);
        int empId = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Remove employee: " + empName + "?",
                "Confirm Remove",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                employeeDAO.deleteEmployee(empId);
                JOptionPane.showMessageDialog(this, "Employee removed successfully!");
                loadAllEmployees();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void printTable() {
        try {
            java.text.MessageFormat header = new java.text.MessageFormat("EMPLOYEE LIST REPORT");
            java.text.MessageFormat footer = new java.text.MessageFormat("Page {0}");

            Font originalFont = employeeTable.getFont();
            int originalRowHeight = employeeTable.getRowHeight();

            employeeTable.setFont(new Font("Arial", Font.PLAIN, 16));
            employeeTable.setRowHeight(35);

            boolean complete = employeeTable.print(
                    JTable.PrintMode.FIT_WIDTH,
                    header,
                    footer,
                    true,
                    null,
                    true,
                    null
            );

            employeeTable.setFont(originalFont);
            employeeTable.setRowHeight(originalRowHeight);

            if (complete) {
                JOptionPane.showMessageDialog(this, "Print job sent successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Print cancelled!");
            }
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this, "Print error: " + e.getMessage());
        }
    }
}
