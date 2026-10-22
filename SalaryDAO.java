import java.sql.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class SalaryDAO {

    public void saveSalary(String id, YearMonth month,
                           int leaveDays, double basicSalary,
                           double deduction, double overtimeHours,
                           double overtimePay, int performanceRating,
                           double performanceBonus, double taxRate,
                           double taxAmount, double netSalary)
                           throws SQLException {

        String sql = "INSERT INTO salary_records "
                + "(employee_id, salary_month, leave_days, basic_salary, "
                + "leave_deduction, overtime_hours, overtime_pay, "
                + "performance_rating, performance_bonus, tax_rate, "
                + "tax_amount, net_salary) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE "
                + "leave_days=VALUES(leave_days), "
                + "basic_salary=VALUES(basic_salary), "
                + "leave_deduction=VALUES(leave_deduction), "
                + "overtime_hours=VALUES(overtime_hours), "
                + "overtime_pay=VALUES(overtime_pay), "
                + "performance_rating=VALUES(performance_rating), "
                + "performance_bonus=VALUES(performance_bonus), "
                + "tax_rate=VALUES(tax_rate), "
                + "tax_amount=VALUES(tax_amount), "
                + "net_salary=VALUES(net_salary)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, month.toString());
            ps.setInt(3, leaveDays);
            ps.setDouble(4, basicSalary);
            ps.setDouble(5, deduction);
            ps.setDouble(6, overtimeHours);
            ps.setDouble(7, overtimePay);
            ps.setInt(8, performanceRating);
            ps.setDouble(9, performanceBonus);
            ps.setDouble(10, taxRate);
            ps.setDouble(11, taxAmount);
            ps.setDouble(12, netSalary);

            ps.executeUpdate();
        }
    }

    public void deleteSalary(String id, YearMonth month)
            throws SQLException {

        String sql = "DELETE FROM salary_records "
                + "WHERE employee_id=? AND salary_month=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, month.toString());

            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new SQLException("Salary record not found.");
            }
        }
    }

    public List<Object[]> getAllSalaryRecords() throws SQLException {
        List<Object[]> list = new ArrayList<Object[]>();

        String sql = "SELECT employee_id, salary_month, leave_days, "
                + "basic_salary, leave_deduction, overtime_hours, "
                + "overtime_pay, performance_rating, performance_bonus, "
                + "tax_rate, tax_amount, net_salary FROM salary_records "
                + "ORDER BY salary_month DESC, employee_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                    rs.getString("employee_id"),
                    rs.getString("salary_month"),
                    rs.getInt("leave_days"),
                    rs.getDouble("basic_salary"),
                    rs.getDouble("leave_deduction"),
                    rs.getDouble("overtime_hours"),
                    rs.getDouble("overtime_pay"),
                    rs.getInt("performance_rating"),
                    rs.getDouble("performance_bonus"),
                    rs.getDouble("tax_rate"),
                    rs.getDouble("tax_amount"),
                    rs.getDouble("net_salary")
                };

                list.add(row);
            }
        }

        return list;
    }
}
