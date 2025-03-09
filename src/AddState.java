public class AddState implements EmployeeState {
    private EmployeeDetails details;

    public AddState(EmployeeDetails details) {
        this.details = details;
    }

    public void enterState() {
        details.setEnabled(true);
        new AddRecordDialog(details);
    }

    public void exitState() {
    }
}
