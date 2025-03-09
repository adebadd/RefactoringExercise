import java.text.DecimalFormat;

public class EditState implements EmployeeState {
    private EmployeeDetails details;

    public EditState(EmployeeDetails details) {
        this.details = details;
    }

    @Override
    public void enterState() {
        if (details.isSomeoneToDisplay()) {
            details.getSalaryField().setText(
                new DecimalFormat("0.00").format(details.getCurrentEmployee().getSalary())
            );
            details.setChange(false);
            details.setEnabled(true);
        }
    }

    @Override
    public void exitState() {
    }
}
