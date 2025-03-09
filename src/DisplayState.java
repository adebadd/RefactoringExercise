public class DisplayState implements EmployeeState {
    private EmployeeDetails details;

    public DisplayState(EmployeeDetails details) {
        this.details = details;
    }

    public void enterState() {
        details.setEnabled(false);
        details.displayRecords(details.getCurrentEmployee());
    }

    public void exitState() {
    }
}
