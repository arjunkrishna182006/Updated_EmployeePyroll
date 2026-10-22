
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.YearMonth;
import java.util.List;

public class Main extends JFrame {

    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private SalaryDAO salaryDAO = new SalaryDAO();

    private JTextField idField = new JTextField();
    private JTextField nameField = new JTextField();
    private JTextField phoneField = new JTextField();
    private JTextField salaryField = new JTextField();

    private JComboBox<String> typeBox = new JComboBox<String>(
            new String[]{"Regular", "Manager", "Intern"});

    private JTextField payrollIdField = new JTextField();
    private JTextField monthField = new JTextField();
    private JTextField leaveField = new JTextField();
    private JTextField overtimeHoursField = new JTextField("0");

    // Manager enters four separate performance scores (each from 1 to 5).
    private JComboBox<Integer> qualityBox = new JComboBox<Integer>(
            new Integer[]{1, 2, 3, 4, 5});
    private JComboBox<Integer> productivityBox = new JComboBox<Integer>(
            new Integer[]{1, 2, 3, 4, 5});
    private JComboBox<Integer> attendanceBox = new JComboBox<Integer>(
            new Integer[]{1, 2, 3, 4, 5});
    private JComboBox<Integer> teamworkBox = new JComboBox<Integer>(
            new Integer[]{1, 2, 3, 4, 5});

    private DefaultTableModel employeeModel;
    private DefaultTableModel salaryModel;

    private JTable employeeTable;
    private JTable salaryTable;

    public Main() {
        setTitle("Employee Payroll Management System");
        setSize(1350, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Employee Details", employeePanel());
        tabs.addTab("Monthly Payroll", payrollPanel());

        add(tabs);

        loadEmployees();
        loadSalaryRecords();

        setVisible(true);
    }

    private JPanel employeePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));

        form.add(new JLabel("Employee ID:"));
        form.add(idField);
        form.add(new JLabel("Employee Name:"));
        form.add(nameField);
        form.add(new JLabel("Phone:"));
        form.add(phoneField);
        form.add(new JLabel("Basic Salary:"));
        form.add(salaryField);
        form.add(new JLabel("Employee Type:"));
        form.add(typeBox);

        JPanel buttons = new JPanel();

        addButton(buttons, "Add", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addEmployee();
            }
        });

        addButton(buttons, "Update", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateEmployee();
            }
        });

        addButton(buttons, "Delete", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteEmployee();
            }
        });

        addButton(buttons, "Clear", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearEmployeeFields();
            }
        });

        employeeModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Type", "Phone", "Basic Salary"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeeTable = new JTable(employeeModel);

        employeeTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int viewRow = employeeTable.getSelectedRow();

                if (viewRow >= 0) {
                    int row = employeeTable.convertRowIndexToModel(viewRow);
                    idField.setText(employeeModel.getValueAt(row, 0).toString());
                    nameField.setText(employeeModel.getValueAt(row, 1).toString());
                    typeBox.setSelectedItem(
                            employeeModel.getValueAt(row, 2).toString());

                    Object phone = employeeModel.getValueAt(row, 3);
                    phoneField.setText(phone == null ? "" : phone.toString());

                    salaryField.setText(
                            employeeModel.getValueAt(row, 4).toString());

                    idField.setEditable(false);
                }
            }
        });

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(employeeTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel payrollPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel form = new JPanel(new GridLayout(8, 2, 5, 5));

        form.add(new JLabel("Employee ID:"));
        form.add(payrollIdField);
        form.add(new JLabel("Month (YYYY-MM):"));
        form.add(monthField);
        form.add(new JLabel("Unpaid Leave Days:"));
        form.add(leaveField);
        form.add(new JLabel("Overtime Hours:"));
        form.add(overtimeHoursField);
        form.add(new JLabel("Quality (1-5):"));
        form.add(qualityBox);
        form.add(new JLabel("Productivity (1-5):"));
        form.add(productivityBox);
        form.add(new JLabel("Attendance (1-5):"));
        form.add(attendanceBox);
        form.add(new JLabel("Teamwork (1-5):"));
        form.add(teamworkBox);

        JPanel buttons = new JPanel();

        addButton(buttons, "Calculate & Save", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveSalary();
            }
        });

        addButton(buttons, "Delete Record", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSalary();
            }
        });

        salaryModel = new DefaultTableModel(
                new String[]{"Employee ID", "Month", "Leave Days",
                    "Basic Salary", "Leave Deduction", "OT Hours",
                    "OT Payment", "Rating", "Performance Bonus",
                    "Tax Rate (%)", "Tax Amount", "Net Salary"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        salaryTable = new JTable(salaryModel);

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(salaryTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private void addButton(JPanel panel, String text,
                           ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        panel.add(button);
    }

    private Employee makeEmployee() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String salaryText = salaryField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || salaryText.isEmpty()) {
            throw new IllegalArgumentException(
                    "Employee ID, name and salary are required.");
        }

        double salary = Double.parseDouble(salaryText);

        if (salary < 0) {
            throw new IllegalArgumentException(
                    "Salary cannot be negative.");
        }

        String type = typeBox.getSelectedItem().toString();

        if (type.equals("Manager")) {
            return new Manager(id, name, phone, salary);
        } else if (type.equals("Intern")) {
            return new Intern(id, name, phone, salary);
        } else {
            return new Regular(id, name, phone, salary);
        }
    }

    private void addEmployee() {
        try {
            employeeDAO.addEmployee(makeEmployee());
            loadEmployees();
            clearEmployeeFields();

            JOptionPane.showMessageDialog(this,
                    "Employee added successfully.");
        } catch (Exception e) {
            showError(e);
        }
    }

    private void updateEmployee() {
        try {
            employeeDAO.updateEmployee(makeEmployee());
            loadEmployees();
            clearEmployeeFields();

            JOptionPane.showMessageDialog(this,
                    "Employee updated successfully.");
        } catch (Exception e) {
            showError(e);
        }
    }

    private void deleteEmployee() {
        try {
            String id = idField.getText().trim();

            if (id.isEmpty()) {
                throw new IllegalArgumentException(
                        "Select an employee first.");
            }

            int choice = JOptionPane.showConfirmDialog(
                    this, "Delete employee " + id + "?",
                    "Confirm", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                employeeDAO.deleteEmployee(id);
                loadEmployees();
                loadSalaryRecords();
                clearEmployeeFields();

                JOptionPane.showMessageDialog(this,
                        "Employee deleted successfully.");
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    private void saveSalary() {
        try {
            String id = payrollIdField.getText().trim();

            if (id.isEmpty()) {
                throw new IllegalArgumentException(
                        "Enter the employee ID.");
            }

            YearMonth month = YearMonth.parse(
                    monthField.getText().trim());

            int leaveDays = Integer.parseInt(
                    leaveField.getText().trim());

            if (leaveDays < 0 || leaveDays > 30) {
                throw new IllegalArgumentException(
                        "Leave days must be between 0 and 30.");
            }

            double basicSalary = getBasicSalary(id);

            double overtimeHours = Double.parseDouble(
                    overtimeHoursField.getText().trim());
            if (overtimeHours < 0 || overtimeHours > 200) {
                throw new IllegalArgumentException(
                        "Overtime hours must be between 0 and 200.");
            }

            int quality = (Integer) qualityBox.getSelectedItem();
            int productivity = (Integer) productivityBox.getSelectedItem();
            int attendance = (Integer) attendanceBox.getSelectedItem();
            int teamwork = (Integer) teamworkBox.getSelectedItem();

            double averageRating = PayrollCalculator.calculateAverageRating(
                    quality, productivity, attendance, teamwork);
            int performanceRating =
                    PayrollCalculator.convertAverageToRating(averageRating);

            double deduction = PayrollCalculator.calculateDeduction(
                    basicSalary, leaveDays);
            double overtimePay = PayrollCalculator.calculateOvertimePay(
                    basicSalary, overtimeHours);
            double performanceBonus =
                    PayrollCalculator.calculatePerformanceBonus(
                            basicSalary, performanceRating);

            // Overtime and bonus are added before tax is calculated.
            double taxableSalary = basicSalary - deduction
                    + overtimePay + performanceBonus;
            double taxRate = PayrollCalculator.getTaxRate(taxableSalary);
            double taxAmount = PayrollCalculator.calculateTax(taxableSalary);
            double netSalary = taxableSalary - taxAmount;

            salaryDAO.saveSalary(id, month, leaveDays, basicSalary,
                    deduction, overtimeHours, overtimePay,
                    performanceRating, performanceBonus, taxRate,
                    taxAmount, netSalary);

            loadSalaryRecords();

            JOptionPane.showMessageDialog(this,
                    "Salary saved successfully.\n"
                    + "Basic Salary: " + String.format("%.2f", basicSalary)
                    + "\nLeave Deduction: " + String.format("%.2f", deduction)
                    + "\nOvertime Hours: " + String.format("%.2f", overtimeHours)
                    + "\nOvertime Payment: " + String.format("%.2f", overtimePay)
                    + "\nQuality Score: " + quality
                    + "\nProductivity Score: " + productivity
                    + "\nAttendance Score: " + attendance
                    + "\nTeamwork Score: " + teamwork
                    + "\nAverage Performance Score: " + String.format("%.2f", averageRating)
                    + "\nFinal Performance Rating: " + performanceRating
                    + "\nPerformance Bonus: " + String.format("%.2f", performanceBonus)
                    + "\nTaxable Salary: " + String.format("%.2f", taxableSalary)
                    + "\nTax Rate: " + String.format("%.2f", taxRate) + "%"
                    + "\nTax Amount: " + String.format("%.2f", taxAmount)
                    + "\nNet Salary: " + String.format("%.2f", netSalary));

        } catch (Exception e) {
            showError(e);
        }
    }

    private double getBasicSalary(String id) throws Exception {
        List<Employee> employees = employeeDAO.getAllEmployees();

        for (Employee employee : employees) {
            if (employee.getEmployeeId().equals(id)) {
                return employee.getBasicSalary();
            }
        }

        throw new IllegalArgumentException("Employee ID not found.");
    }

    private void deleteSalary() {
        try {
            String id = payrollIdField.getText().trim();

            if (id.isEmpty()) {
                throw new IllegalArgumentException(
                        "Enter the employee ID.");
            }

            YearMonth month = YearMonth.parse(
                    monthField.getText().trim());

            salaryDAO.deleteSalary(id, month);
            loadSalaryRecords();

            JOptionPane.showMessageDialog(this,
                    "Salary record deleted successfully.");
        } catch (Exception e) {
            showError(e);
        }
    }

    private void loadEmployees() {
        try {
            employeeModel.setRowCount(0);

            List<Employee> employees = employeeDAO.getAllEmployees();

            for (Employee employee : employees) {
                employeeModel.addRow(new Object[]{
                    employee.getEmployeeId(),
                    employee.getEmployeeName(),
                    employee.getEmployeeType(),
                    employee.getPhone(),
                    employee.getBasicSalary()
                });
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    private void loadSalaryRecords() {
        try {
            salaryModel.setRowCount(0);

            List<Object[]> records = salaryDAO.getAllSalaryRecords();

            for (Object[] record : records) {
                salaryModel.addRow(record);
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    private void clearEmployeeFields() {
        idField.setText("");
        nameField.setText("");
        phoneField.setText("");
        salaryField.setText("");
        typeBox.setSelectedIndex(0);
        idField.setEditable(true);
        employeeTable.clearSelection();
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this,
                e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }
}
