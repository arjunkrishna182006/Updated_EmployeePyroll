
public class Regular extends Employee {

    public Regular(String id, String name,
                   String phone, double salary) {
        super(id, name, phone, salary);
    }

    @Override
    public String getEmployeeType() {
        return "Regular";
    }
}
