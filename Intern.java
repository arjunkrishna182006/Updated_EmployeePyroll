
public class Intern extends Employee {

    public Intern(String id, String name,
                  String phone, double salary) {
        super(id, name, phone, salary);
    }

    @Override
    public String getEmployeeType() {
        return "Intern";
    }
}
