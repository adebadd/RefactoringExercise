public class NextRecordCommand implements Command {
    private EmployeeDetails details;

    public NextRecordCommand(EmployeeDetails details) {
        this.details = details;
    }

    public void execute() {
        details.nextRecord();
        details.displayRecords(details.getCurrentEmployee());
    }
}
