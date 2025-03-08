public class FirstRecordCommand implements Command {
    private EmployeeDetails details;

    public FirstRecordCommand(EmployeeDetails details) {
        this.details = details;
    }

    public void execute() {
        details.firstRecord();
        details.displayRecords(details.getCurrentEmployee());
    }
}
