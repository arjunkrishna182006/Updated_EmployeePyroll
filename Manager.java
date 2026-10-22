
public class Manager extends Regular {

    public Manager(String id, String name,
                   String phone, double salary) {
        super(id, name, phone, salary);
    }

    @Override
    public String getEmployeeType() {
        return "Manager";
    }
}
