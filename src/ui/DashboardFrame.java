package ui;

import dao.EmployeeDAO;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class DashboardFrame extends JFrame {
    private Image backgroundImage;

    public DashboardFrame() {
        setTitle("Employee Management System");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        try {
            URL imgUrl = getClass().getResource("/background.jpg");
            if (imgUrl != null) {
                backgroundImage = new ImageIcon(imgUrl).getImage();
            }
        } catch (Exception e) {
            backgroundImage = null;
        }

        initUI();
    }

    private void initUI() {
        JPanel contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    Graphics2D g2d = (Graphics2D) g;
                    GradientPaint gp = new GradientPaint(0, 0, new Color(0, 102, 204),
                            getWidth(), getHeight(), new Color(0, 51, 102));
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 15, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setBounds(getWidth() - 260, 150, 220, 350);

        JButton btnAdd = createMenuButton("ADD EMPLOYEE");
        btnAdd.addActionListener(e -> openAddEmployee());

        JButton btnList = createMenuButton("EMPLOYEE LIST");
        btnList.addActionListener(e -> openMainOperations());

        JButton btnRemove = createMenuButton("REMOVE");
        btnRemove.addActionListener(e -> openMainOperations());

        JButton btnUpdate = createMenuButton("UPDATE");
        btnUpdate.addActionListener(e -> openMainOperations());


        buttonPanel.add(btnAdd);
        buttonPanel.add(btnList);
        buttonPanel.add(btnRemove);
        buttonPanel.add(btnUpdate);

        contentPane.add(buttonPanel);

        JLabel titleLabel = new JLabel("EMPLOYEE MANAGEMENT SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(40, 40, 550, 50);
        contentPane.add(titleLabel);


    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setForeground(Color.BLACK);
        btn.setBackground(new Color(255, 255, 255, 200));
        btn.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    private void openAddEmployee() {
        new AddEmployeeDialog(this, new EmployeeDAO()).setVisible(true);
    }

    private void openMainOperations() {
        new MainOperationsFrame().setVisible(true);
    }

}