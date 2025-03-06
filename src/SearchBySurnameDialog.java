/*
 * 
 * This is a dialog for searching Employees by their surname.
 * 
 * */

public class SearchBySurnameDialog extends AbstractSearchDialog {
    public SearchBySurnameDialog(EmployeeDetails parent) {
        super(parent);
        setTitle("Search by Surname");
        setVisible(true);
    }

    protected String getPanelTitle() {
        return "Search by Surname";
    }

    protected String getPromptLabel() {
        return "Enter Surname:";
    }

    protected void performSearch() {
        parent.searchBySurnameField.setText(searchField.getText());
        parent.searchEmployeeBySurname();
        dispose();
    }
}
