/*
 * 
 * This is the dialog for Employee search by ID
 * 
 * */

import java.awt.Color;
import javax.swing.JOptionPane;

public class SearchByIdDialog extends AbstractSearchDialog {
    public SearchByIdDialog(EmployeeDetails parent) {
        super(parent);
        setTitle("Search by ID");
        setVisible(true);
    }

    protected String getPanelTitle() {
        return "Search by ID";
    }

    protected String getPromptLabel() {
        return "Enter ID:";
    }

    protected void performSearch() {
        try {
            Double.parseDouble(searchField.getText());
            parent.searchByIdField.setText(searchField.getText());
            parent.searchEmployeeById();
            dispose();
        } catch (NumberFormatException num) {
            searchField.setBackground(new Color(255, 150, 150));
            JOptionPane.showMessageDialog(null, "Wrong ID format!");
        }
    }
}
