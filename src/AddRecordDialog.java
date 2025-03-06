import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

public class AddRecordDialog extends JDialog implements ActionListener {
    JTextField idField, ppsField, surnameField, firstNameField, salaryField;
    JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;
    JButton save, cancel;
    EmployeeDetails parent;
    EmployeeValidator validator = new EmployeeValidator();
    AddRecordService addRecordService;

    public AddRecordDialog(EmployeeDetails parent) {
        setTitle("Add Record");
        setModal(true);
        this.parent = parent;
        this.parent.setEnabled(false);
        addRecordService = new AddRecordService(parent.getFileManager());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JScrollPane scrollPane = new JScrollPane(dialogPane());
        setContentPane(scrollPane);
        getRootPane().setDefaultButton(save);
        setSize(500, 370);
        setLocation(350, 250);
        setVisible(true);
    }

    public Container dialogPane() {
        JPanel empDetails = new JPanel(new MigLayout());
        JPanel buttonPanel = new JPanel();
        empDetails.setBorder(BorderFactory.createTitledBorder("Employee Details"));
        empDetails.add(new JLabel("ID:"), "growx, pushx");
        empDetails.add(idField = new JTextField(20), "growx, pushx, wrap");
        idField.setEditable(false);
        empDetails.add(new JLabel("PPS Number:"), "growx, pushx");
        empDetails.add(ppsField = new JTextField(20), "growx, pushx, wrap");
        empDetails.add(new JLabel("Surname:"), "growx, pushx");
        empDetails.add(surnameField = new JTextField(20), "growx, pushx, wrap");
        empDetails.add(new JLabel("First Name:"), "growx, pushx");
        empDetails.add(firstNameField = new JTextField(20), "growx, pushx, wrap");
        empDetails.add(new JLabel("Gender:"), "growx, pushx");
        empDetails.add(genderCombo = new JComboBox<>(parent.gender), "growx, pushx, wrap");
        empDetails.add(new JLabel("Department:"), "growx, pushx");
        empDetails.add(departmentCombo = new JComboBox<>(parent.department), "growx, pushx, wrap");
        empDetails.add(new JLabel("Salary:"), "growx, pushx");
        empDetails.add(salaryField = new JTextField(20), "growx, pushx, wrap");
        empDetails.add(new JLabel("Full Time:"), "growx, pushx");
        empDetails.add(fullTimeCombo = new JComboBox<>(parent.fullTime), "growx, pushx, wrap");
        buttonPanel.add(save = new JButton("Save"));
        save.addActionListener(this);
        save.requestFocus();
        buttonPanel.add(cancel = new JButton("Cancel"));
        cancel.addActionListener(this);
        empDetails.add(buttonPanel, "span 2,growx, pushx,wrap");
        for (int i = 0; i < empDetails.getComponentCount(); i++) {
            empDetails.getComponent(i).setFont(parent.font1);
            if (empDetails.getComponent(i) instanceof JComboBox) {
                empDetails.getComponent(i).setBackground(Color.WHITE);
            } else if (empDetails.getComponent(i) instanceof JTextField) {
                JTextField f = (JTextField) empDetails.getComponent(i);
                if (f == ppsField) {
                    f.setDocument(new JTextFieldLimit(9));
                } else {
                    f.setDocument(new JTextFieldLimit(20));
                }
            }
        }
        idField.setText(Integer.toString(parent.getNextFreeId()));
        return empDetails;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == save) {
            if (checkInput()) {
                addRecord();
                dispose();
                parent.changesMade = true;
            } else {
                JOptionPane.showMessageDialog(null, "Wrong values or format! Please check!");
                setToWhite();
            }
        } else if (e.getSource() == cancel) {
            dispose();
        }
    }

    public void addRecord() {
        boolean full = false;
        if (fullTimeCombo.getSelectedItem().toString().equalsIgnoreCase("Yes")) {
            full = true;
        }
        Employee emp = addRecordService.createEmployee(
            Integer.parseInt(idField.getText()),
            ppsField.getText().toUpperCase(),
            surnameField.getText().toUpperCase(),
            firstNameField.getText().toUpperCase(),
            genderCombo.getSelectedItem().toString().charAt(0),
            departmentCombo.getSelectedItem().toString(),
            Double.parseDouble(salaryField.getText()),
            full
        );
        addRecordService.saveEmployee(emp);
        parent.setCurrentEmployee(emp);
        parent.displayRecords(emp);
    }

    public boolean checkInput() {
        boolean valid = true;
        if (ppsField.getText().trim().isEmpty()) {
            ppsField.setBackground(new Color(255, 150, 150));
            valid = false;
        }
        if (!validator.isValidPps(ppsField.getText().trim())) {
            ppsField.setBackground(new Color(255, 150, 150));
            valid = false;
        }
        if (surnameField.getText().trim().isEmpty()) {
            surnameField.setBackground(new Color(255, 150, 150));
            valid = false;
        }
        if (firstNameField.getText().trim().isEmpty()) {
            firstNameField.setBackground(new Color(255, 150, 150));
            valid = false;
        }
        if (genderCombo.getSelectedIndex() == 0) {
            genderCombo.setBackground(new Color(255, 150, 150));
            valid = false;
        }
        if (departmentCombo.getSelectedIndex() == 0) {
            departmentCombo.setBackground(new Color(255, 150, 150));
            valid = false;
        }
        try {
            double sal = Double.parseDouble(salaryField.getText());
            if (sal < 0) {
                salaryField.setBackground(new Color(255, 150, 150));
                valid = false;
            }
        } catch (NumberFormatException ex) {
            salaryField.setBackground(new Color(255, 150, 150));
            valid = false;
        }
        if (fullTimeCombo.getSelectedIndex() == 0) {
            fullTimeCombo.setBackground(new Color(255, 150, 150));
            valid = false;
        }
        return valid;
    }

    public void setToWhite() {
        ppsField.setBackground(Color.WHITE);
        surnameField.setBackground(Color.WHITE);
        firstNameField.setBackground(Color.WHITE);
        salaryField.setBackground(Color.WHITE);
        genderCombo.setBackground(Color.WHITE);
        departmentCombo.setBackground(Color.WHITE);
        fullTimeCombo.setBackground(Color.WHITE);
    }
}
