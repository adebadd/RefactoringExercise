public class PreviousRecordCommand implements Command {
    private EmployeeDetails details;

    public PreviousRecordCommand(EmployeeDetails details) {
        this.details = details;
    }

    public void execute() {
        details.previousRecord();
        details.displayRecords(details.getCurrentEmployee());
    }
}
