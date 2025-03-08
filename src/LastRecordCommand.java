public class LastRecordCommand implements Command {
    private EmployeeDetails details;

    public LastRecordCommand(EmployeeDetails details) {
        this.details = details;
    }

    public void execute() {
        details.lastRecord();
        details.displayRecords(details.getCurrentEmployee());
    }
}
