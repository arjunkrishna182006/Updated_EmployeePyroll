
public class Employee {

    private String employeeId;
    private String employeeName;
    private String phone;
    private double basicSalary;

    public Employee(String employeeId, String employeeName,
                    String phone, double basicSalary) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.phone = phone;
        this.basicSalary = basicSalary;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getPhone() {
        return phone;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public String getEmployeeType() {
        return "Employee";
    }
}
