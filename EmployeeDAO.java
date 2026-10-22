
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public void addEmployee(Employee e) throws SQLException {
        String sql = "INSERT INTO employee_details "
                + "(employee_id, employee_name, employee_type, "
                + "phone, basic_salary) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getEmployeeId());
            ps.setString(2, e.getEmployeeName());
            ps.setString(3, e.getEmployeeType());
            ps.setString(4, e.getPhone());
            ps.setDouble(5, e.getBasicSalary());

            ps.executeUpdate();
        }
    }

    public void updateEmployee(Employee e) throws SQLException {
        String sql = "UPDATE employee_details SET employee_name=?, "
                + "employee_type=?, phone=?, basic_salary=? "
                + "WHERE employee_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getEmployeeName());
            ps.setString(2, e.getEmployeeType());
            ps.setString(3, e.getPhone());
            ps.setDouble(4, e.getBasicSalary());
            ps.setString(5, e.getEmployeeId());

            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new SQLException("Employee ID not found.");
            }
        }
    }

    public void deleteEmployee(String id) throws SQLException {
        if (id == null || id.trim().isEmpty()) {
            throw new SQLException("Select an employee first.");
        }

        String deleteSalarySql =
                "DELETE FROM salary_records WHERE employee_id=?";
        String deleteEmployeeSql =
                "DELETE FROM employee_details WHERE employee_id=?";

        try (Connection con = DBConnection.getConnection()) {
            boolean oldAutoCommit = con.getAutoCommit();
            con.setAutoCommit(false);

            try {

                try (PreparedStatement salaryPs =
                             con.prepareStatement(deleteSalarySql)) {
                    salaryPs.setString(1, id.trim());
                    salaryPs.executeUpdate();
                }

                int rows;
                try (PreparedStatement employeePs =
                             con.prepareStatement(deleteEmployeeSql)) {
                    employeePs.setString(1, id.trim());
                    rows = employeePs.executeUpdate();
                }

                if (rows == 0) {
                    throw new SQLException(
                            "Employee ID '" + id.trim() + "' was not found.");
                }

                con.commit();
            } catch (SQLException e) {
                try {
                    con.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                throw e;
            } finally {
                try {
                    con.setAutoCommit(oldAutoCommit);
                } catch (SQLException ignored) {
                   
                }
            }
        }
    }

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> list = new ArrayList<Employee>();

        String sql = "SELECT * FROM employee_details ORDER BY employee_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("employee_id");
                String name = rs.getString("employee_name");
                String type = rs.getString("employee_type");
                String phone = rs.getString("phone");
                double salary = rs.getDouble("basic_salary");

                Employee e;

                if (type.equals("Manager")) {
                    e = new Manager(id, name, phone, salary);
                } else if (type.equals("Intern")) {
                    e = new Intern(id, name, phone, salary);
                } else {
                    e = new Regular(id, name, phone, salary);
                }

                list.add(e);
            }
        }

        return list;
    }
}
