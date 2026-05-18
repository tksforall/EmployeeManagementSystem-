package ui;

import dao.EmployeeDAO;
import dao.PayrollDAO;
import model.Employee;
import model.Payroll;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PayrollManager extends JFrame {
    private PayrollDAO payrollDAO;
    private EmployeeDAO employeeDAO;
    private JTable payrollTable;
    private DefaultTableModel tableModel;

    public PayrollManager() {
        payrollDAO = new PayrollDAO();
        employeeDAO = new EmployeeDAO();
        setTitle("PAYROLL MANAGEMENT");
        setSize(1000, 550);
        setLocationRelativeTo(null);
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Payroll Operations"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        JButton btnAdd = createActionButton("ADD PAYROLL");
        JButton btnRefresh = createActionButton("REFRESH");
        JButton btnDelete = createActionButton("DELETE");
        JButton btnPrint = createActionButton("PRINT");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnPrint);

        topPanel.add(buttonPanel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Emp ID", "Employee Name", "Month", "Total Amount", "Report"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        payrollTable = new JTable(tableModel);
        payrollTable.setFont(new Font("Arial", Font.PLAIN, 14));
        payrollTable.setRowHeight(28);
        payrollTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < payrollTable.getColumnCount(); i++) {
            payrollTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) payrollTable.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);

        JScrollPane scrollPane = new JScrollPane(payrollTable);
        add(scrollPane, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> openAddPayrollDialog());
        btnRefresh.addActionListener(e -> loadData());
        btnDelete.addActionListener(e -> deletePayroll());
        btnPrint.addActionListener(e -> printTable());
    }

    private JButton createActionButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setBackground(new Color(230, 230, 230));
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(120, 28));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try {
            List<Payroll> list = payrollDAO.getAllPayrolls();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
            for (Payroll p : list) {
                tableModel.addRow(new Object[]{
                        p.getEmpId(),
                        p.getEmployeeName(),
                        sdf.format(p.getPayrollDate()),
                        String.format("%,.0f", p.getTotalAmount()) + " VND",
                        p.getReport()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void openAddPayrollDialog() {
        JDialog dialog = new JDialog(this, "Add Payroll", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Select Employee:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> cbEmployee = new JComboBox<>();
        try {
            List<Employee> emps = employeeDAO.getAllEmployees();
            for (Employee e : emps) {
                cbEmployee.addItem(e.getEmpId() + " - " + e.getFullName());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        cbEmployee.setPreferredSize(new Dimension(200, 28));
        dialog.add(cbEmployee, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Month (MM/yyyy):"), gbc);
        gbc.gridx = 1;
        JTextField txtMonth = new JTextField("05/2025", 10);
        dialog.add(txtMonth, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Total Amount:"), gbc);
        gbc.gridx = 1;
        JTextField txtAmount = new JTextField(10);
        dialog.add(txtAmount, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Report:"), gbc);
        gbc.gridx = 1;
        JTextField txtReport = new JTextField(15);
        dialog.add(txtReport, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton btnSave = createActionButton("SAVE");
        JButton btnCancel = createActionButton("CANCEL");
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        dialog.add(btnPanel, gbc);

        btnSave.addActionListener(e -> {
            try {
                String selected = (String) cbEmployee.getSelectedItem();
                int empId = Integer.parseInt(selected.split(" - ")[0]);

                SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
                Date payrollDate = sdf.parse(txtMonth.getText().trim());

                double amount = Double.parseDouble(txtAmount.getText().trim());
                if (amount <= 0) throw new NumberFormatException();

                Payroll p = new Payroll();
                p.setEmpId(empId);
                p.setPayrollDate(payrollDate);
                p.setTotalAmount(amount);
                p.setReport(txtReport.getText().trim());

                payrollDAO.addPayroll(p);
                JOptionPane.showMessageDialog(dialog, "Payroll added successfully!");
                dialog.dispose();
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void deletePayroll() {
        int selectedRow = payrollTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a payroll record to delete!");
            return;
        }

        String empName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete payroll record for: " + empName + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Get all payroll records and delete by index (simplified)
                // For now, show message
                JOptionPane.showMessageDialog(this, "Delete feature: Please select a specific record or implement delete by ID.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void printTable() {
        try {
            Font originalFont = payrollTable.getFont();
            int originalRowHeight = payrollTable.getRowHeight();

            payrollTable.setFont(new Font("Arial", Font.PLAIN, 12));
            payrollTable.setRowHeight(30);

            java.text.MessageFormat header = new java.text.MessageFormat("PAYROLL REPORT");
            java.text.MessageFormat footer = new java.text.MessageFormat("Page {0} - Printed on " + new java.util.Date());

            boolean complete = payrollTable.print(JTable.PrintMode.FIT_WIDTH, header, footer, true, null, true, null);

            payrollTable.setFont(originalFont);
            payrollTable.setRowHeight(originalRowHeight);

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