/*
 * 
 * This is a menu driven system that will allow users to define a data structure representing a collection of 
 * records that can be displayed both by means of a dialog that can be scrolled through and by means of a table
 * to give an overall view of the collection contents.
 * 
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;
import java.io.File;


public class EmployeeDetails extends JFrame implements ActionListener, ItemListener, DocumentListener, WindowListener {
	// decimal format for inactive currency text field
	private static final DecimalFormat format = new DecimalFormat("\u20ac ###,###,##0.00");
	// decimal format for active currency text field
	private static final DecimalFormat fieldFormat = new DecimalFormat("0.00");
	private long currentByteStart = 0;
	private FileManager fileManager = new FileManager();
	private boolean change = false;
	boolean changesMade = false;
	private JMenuItem open, save, saveAs, create, modify, delete, firstItem, lastItem, nextItem, prevItem, searchById,
			searchBySurname, listAll, closeApp;
	private JButton first, previous, next, last, add, edit, deleteButton, displayAll, searchId, searchSurname,
			saveChange, cancelChange;
	private JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;
	private JTextField idField, ppsField, surnameField, firstNameField, salaryField;
	private static EmployeeDetails frame = new EmployeeDetails();
	Font font1 = new Font("SansSerif", Font.BOLD, 16);
	Employee currentEmployee;
	JTextField searchByIdField, searchBySurnameField;
	String[] gender = { "", "M", "F" };
	String[] department = { "", "Administration", "Production", "Transport", "Management" };
	String[] fullTime = { "", "Yes", "No" };

    public FileManager getFileManager() {
        return fileManager;
    }

	// initialize menu bar
	private JMenuBar menuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		JMenu recordMenu = new JMenu("Records");
		recordMenu.setMnemonic(KeyEvent.VK_R);
		JMenu navigateMenu = new JMenu("Navigate");
		navigateMenu.setMnemonic(KeyEvent.VK_N);
		JMenu closeMenu = new JMenu("Exit");
		closeMenu.setMnemonic(KeyEvent.VK_E);

		menuBar.add(fileMenu);
		menuBar.add(recordMenu);
		menuBar.add(navigateMenu);
		menuBar.add(closeMenu);

		fileMenu.add(open = new JMenuItem("Open")).addActionListener(this);
		open.setMnemonic(KeyEvent.VK_O);
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		fileMenu.add(save = new JMenuItem("Save")).addActionListener(this);
		save.setMnemonic(KeyEvent.VK_S);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		fileMenu.add(saveAs = new JMenuItem("Save As")).addActionListener(this);
		saveAs.setMnemonic(KeyEvent.VK_F2);
		saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, ActionEvent.CTRL_MASK));

		recordMenu.add(create = new JMenuItem("Create new Record")).addActionListener(this);
		create.setMnemonic(KeyEvent.VK_N);
		create.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		recordMenu.add(modify = new JMenuItem("Modify Record")).addActionListener(this);
		modify.setMnemonic(KeyEvent.VK_E);
		modify.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		recordMenu.add(delete = new JMenuItem("Delete Record")).addActionListener(this);

		navigateMenu.add(firstItem = new JMenuItem("First"));
		firstItem.addActionListener(this);
		navigateMenu.add(prevItem = new JMenuItem("Previous"));
		prevItem.addActionListener(this);
		navigateMenu.add(nextItem = new JMenuItem("Next"));
		nextItem.addActionListener(this);
		navigateMenu.add(lastItem = new JMenuItem("Last"));
		lastItem.addActionListener(this);
		navigateMenu.addSeparator();
		navigateMenu.add(searchById = new JMenuItem("Search by ID")).addActionListener(this);
		navigateMenu.add(searchBySurname = new JMenuItem("Search by Surname")).addActionListener(this);
		navigateMenu.add(listAll = new JMenuItem("List all Records")).addActionListener(this);

		closeMenu.add(closeApp = new JMenuItem("Close")).addActionListener(this);
		closeApp.setMnemonic(KeyEvent.VK_F4);
		closeApp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.CTRL_MASK));

		return menuBar;
	} // end menuBar

	// initialize search panel
	private JPanel searchPanel() {
		JPanel searchPanel = new JPanel(new MigLayout());
		searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
		searchPanel.add(new JLabel("Search by ID:"), "growx, pushx");
		searchPanel.add(searchByIdField = new JTextField(20), "width 200:200:200, growx, pushx");
		searchByIdField.addActionListener(this);
		searchByIdField.setDocument(new JTextFieldLimit(20));
		searchPanel.add(searchId = new JButton("Go"), "width 35:35:35, height 20:20:20, growx, pushx, wrap");
		searchId.addActionListener(this);
		searchId.setToolTipText("Search Employee By ID");

		searchPanel.add(new JLabel("Search by Surname:"), "growx, pushx");
		searchPanel.add(searchBySurnameField = new JTextField(20), "width 200:200:200, growx, pushx");
		searchBySurnameField.addActionListener(this);
		searchBySurnameField.setDocument(new JTextFieldLimit(20));
		searchPanel.add(searchSurname = new JButton("Go"), "width 35:35:35, height 20:20:20, growx, pushx, wrap");
		searchSurname.addActionListener(this);
		searchSurname.setToolTipText("Search Employee By Surname");

		return searchPanel;
	} // end searchPanel

	// initialize navigation panel
	private JPanel navigPanel() {
		JPanel navigPanel = new JPanel();

		navigPanel.setBorder(BorderFactory.createTitledBorder("Navigate"));
		navigPanel.add(first = new JButton(new ImageIcon(
				new ImageIcon("first.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		first.setPreferredSize(new Dimension(17, 17));
		first.addActionListener(this);
		first.setToolTipText("Display first Record");

		navigPanel.add(previous = new JButton(new ImageIcon(
				new ImageIcon("prev.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		previous.setPreferredSize(new Dimension(17, 17));
		previous.addActionListener(this);
		previous.setToolTipText("Display next Record");

		navigPanel.add(next = new JButton(new ImageIcon(
				new ImageIcon("next.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		next.setPreferredSize(new Dimension(17, 17));
		next.addActionListener(this);
		next.setToolTipText("Display previous Record");

		navigPanel.add(last = new JButton(new ImageIcon(
				new ImageIcon("last.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		last.setPreferredSize(new Dimension(17, 17));
		last.addActionListener(this);
		last.setToolTipText("Display last Record");

		return navigPanel;
	} // end naviPanel

	private JPanel buttonPanel() {
		JPanel buttonPanel = new JPanel();

		buttonPanel.add(add = new JButton("Add Record"), "growx, pushx");
		add.addActionListener(this);
		add.setToolTipText("Add new Employee Record");
		buttonPanel.add(edit = new JButton("Edit Record"), "growx, pushx");
		edit.addActionListener(this);
		edit.setToolTipText("Edit current Employee");
		buttonPanel.add(deleteButton = new JButton("Delete Record"), "growx, pushx, wrap");
		deleteButton.addActionListener(this);
		deleteButton.setToolTipText("Delete current Employee");
		buttonPanel.add(displayAll = new JButton("List all Records"), "growx, pushx");
		displayAll.addActionListener(this);
		displayAll.setToolTipText("List all Registered Employees");

		return buttonPanel;
	} // end buttonPanel

	// initialize main/details panel
	private JPanel detailsPanel() {
		JPanel empDetails = new JPanel(new MigLayout());
		JPanel buttonPanel = new JPanel();
		JTextField field;

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
		empDetails.add(genderCombo = new JComboBox<>(gender), "growx, pushx, wrap");

		empDetails.add(new JLabel("Department:"), "growx, pushx");
		empDetails.add(departmentCombo = new JComboBox<>(department), "growx, pushx, wrap");

		empDetails.add(new JLabel("Salary:"), "growx, pushx");
		empDetails.add(salaryField = new JTextField(20), "growx, pushx, wrap");

		empDetails.add(new JLabel("Full Time:"), "growx, pushx");
		empDetails.add(fullTimeCombo = new JComboBox<>(fullTime), "growx, pushx, wrap");

		buttonPanel.add(saveChange = new JButton("Save"));
		saveChange.addActionListener(this);
		saveChange.setVisible(false);
		saveChange.setToolTipText("Save changes");
		buttonPanel.add(cancelChange = new JButton("Cancel"));
		cancelChange.addActionListener(this);
		cancelChange.setVisible(false);
		cancelChange.setToolTipText("Cancel edit");

		empDetails.add(buttonPanel, "span 2,growx, pushx,wrap");

		// loop through panel components and add fonts/listeners
		for (int i = 0; i < empDetails.getComponentCount(); i++) {
			empDetails.getComponent(i).setFont(font1);
			if (empDetails.getComponent(i) instanceof JTextField) {
				field = (JTextField) empDetails.getComponent(i);
				field.setEditable(false);
				if (field == ppsField)
					field.setDocument(new JTextFieldLimit(9));
				else
					field.setDocument(new JTextFieldLimit(20));
				field.getDocument().addDocumentListener(this);
			} else if (empDetails.getComponent(i) instanceof JComboBox) {
				empDetails.getComponent(i).setBackground(Color.WHITE);
				empDetails.getComponent(i).setEnabled(false);
				((JComboBox<?>) empDetails.getComponent(i)).addItemListener(this);
				((JComboBox<?>) empDetails.getComponent(i)).setRenderer(new DefaultListCellRenderer() {
					public void paint(Graphics g) {
						setForeground(new Color(65, 65, 65));
						super.paint(g);
					}
				});
			}
		}
		return empDetails;
	} // end detailsPanel

	public void displayRecords(Employee e) {
		int gCount = 0;
		int dCount = 0;
		boolean found = false;
		searchByIdField.setText("");
		searchBySurnameField.setText("");
		if (e == null) {
		} else if (e.getEmployeeId() == 0) {
		} else {
			while (!found && gCount < gender.length - 1) {
				if (Character.toString(e.getGender()).equalsIgnoreCase(gender[gCount])) {
					found = true;
				} else {
					gCount++;
				}
			}
			found = false;
			while (!found && dCount < department.length - 1) {
				if (e.getDepartment().trim().equalsIgnoreCase(department[dCount])) {
					found = true;
				} else {
					dCount++;
				}
			}
			idField.setText(Integer.toString(e.getEmployeeId()));
			ppsField.setText(e.getPps().trim());
			surnameField.setText(e.getSurname().trim());
			firstNameField.setText(e.getFirstName());
			genderCombo.setSelectedIndex(gCount);
			departmentCombo.setSelectedIndex(dCount);
			salaryField.setText(format.format(e.getSalary()));
			if (e.getFullTime()) {
				fullTimeCombo.setSelectedIndex(1);
			} else {
				fullTimeCombo.setSelectedIndex(2);
			}
		}
		change = false;
	} // end displayRecords

	private void handleOpenAction() {
		fileManager.openFile(this);
	} // end handleOpenAction

	private void handleSaveAction() {
		if (checkInput() && !checkForChanges()) {
			fileManager.saveFile(this);
			change = false;
		}
	} // end handleSaveAction

	private void handleSaveAsAction() {
		if (checkInput() && !checkForChanges()) {
			fileManager.saveFileAs(this);
			change = false;
		}
	} // end handleSaveAsAction

	private void handleExitAction() {
		if (checkInput() && !checkForChanges()) {
			fileManager.exitApp(this);
		}
	} // end handleExitAction

	private void handleSearchByIdAction() {
		if (checkInput() && !checkForChanges()) {
			new SearchByIdDialog(EmployeeDetails.this);
		}
	} // end handleSearchByIdAction

	private void handleSearchBySurnameAction() {
		if (checkInput() && !checkForChanges()) {
			new SearchBySurnameDialog(EmployeeDetails.this);
		}
	} // end handleSearchBySurnameAction

	private void handleListAllAction() {
		if (checkInput() && !checkForChanges()) {
			if (isSomeoneToDisplay()) {
				new EmployeeSummaryDialog(getAllEmloyees());
			}
		}
	} // end handleListAllAction

	private void handleCreateRecordAction() {
		if (checkInput() && !checkForChanges()) {
			new AddRecordDialog(this);
		}
	} // end handleCreateRecordAction

	private void handleEditRecordAction() {
		if (checkInput() && !checkForChanges()) {
			editDetails();
		}
	} // end handleEditRecordAction

	private void handleDeleteRecordAction() {
		if (checkInput() && !checkForChanges()) {
			deleteRecord();
		}
	} // end handleDeleteRecordAction

	private void handleFirstRecordAction() {
		if (checkInput() && !checkForChanges()) {
			firstRecord();
			displayRecords(currentEmployee);
		}
	} // end handleFirstRecordAction

	private void handlePrevRecordAction() {
		if (checkInput() && !checkForChanges()) {
			previousRecord();
			displayRecords(currentEmployee);
		}
	} // end handlePrevRecordAction

	private void handleNextRecordAction() {
		if (checkInput() && !checkForChanges()) {
			nextRecord();
			displayRecords(currentEmployee);
		}
	} // end handleNextRecordAction

	private void handleLastRecordAction() {
		if (checkInput() && !checkForChanges()) {
			lastRecord();
			displayRecords(currentEmployee);
		}
	} // end handleLastRecordAction

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == closeApp) {
			handleExitAction();
		} else if (e.getSource() == open) {
			handleOpenAction();
		} else if (e.getSource() == save) {
			handleSaveAction();
		} else if (e.getSource() == saveAs) {
			handleSaveAsAction();
		} else if (e.getSource() == searchById) {
			handleSearchByIdAction();
		} else if (e.getSource() == searchBySurname) {
			handleSearchBySurnameAction();
		} else if (e.getSource() == searchId || e.getSource() == searchByIdField) {
			searchEmployeeById();
		} else if (e.getSource() == searchSurname || e.getSource() == searchBySurnameField) {
			searchEmployeeBySurname();
		} else if (e.getSource() == saveChange) {
			if (checkInput() && !checkForChanges()) {
			}
		} else if (e.getSource() == cancelChange) {
			cancelChange();
		} else if (e.getSource() == firstItem || e.getSource() == first) {
			handleFirstRecordAction();
		} else if (e.getSource() == prevItem || e.getSource() == previous) {
			handlePrevRecordAction();
		} else if (e.getSource() == nextItem || e.getSource() == next) {
			handleNextRecordAction();
		} else if (e.getSource() == lastItem || e.getSource() == last) {
			handleLastRecordAction();
		} else if (e.getSource() == listAll || e.getSource() == displayAll) {
			handleListAllAction();
		} else if (e.getSource() == create || e.getSource() == add) {
			handleCreateRecordAction();
		} else if (e.getSource() == modify || e.getSource() == edit) {
			handleEditRecordAction();
		} else if (e.getSource() == delete || e.getSource() == deleteButton) {
			handleDeleteRecordAction();
		} else if (e.getSource() == searchBySurname) {
			if (checkInput() && !checkForChanges()) {
				new SearchBySurnameDialog(this);
			}
		}
	} // end actionPerformed

	public long getCurrentByteStart() {
		return currentByteStart;
	} // end getCurrentByteStart

	public boolean isChange() {
		return change;
	} // end isChange

	public void setCurrentEmployee(Employee e) {
		currentEmployee = e;
	} // end setCurrentEmployee

	public Employee getCurrentEmployee() {
		return currentEmployee;
	} // end getCurrentEmployee

	public JTextField getIdField() {
		return idField;
	} // end getIdField

	public Employee getChangedDetails() {
		boolean ft = false;
		if (((String) fullTimeCombo.getSelectedItem()).equalsIgnoreCase("Yes")) {
			ft = true;
		}
		return new Employee(Integer.parseInt(idField.getText()), ppsField.getText().toUpperCase(),
				surnameField.getText().toUpperCase(), firstNameField.getText().toUpperCase(),
				genderCombo.getSelectedItem().toString().charAt(0), departmentCombo.getSelectedItem().toString(),
				Double.parseDouble(salaryField.getText()), ft);
	} // end getChangedDetails

	public boolean checkFileName(File f) {
		boolean check = false;
		int length = f.toString().length();
		if (f.toString().charAt(length - 4) == '.' && f.toString().charAt(length - 3) == 'd'
				&& f.toString().charAt(length - 2) == 'a' && f.toString().charAt(length - 1) == 't') {
			check = true;
		}
		return check;
	} // end checkFileName

	private boolean checkForChanges() {
		boolean anyChanges = false;
		if (change) {
			saveChanges();
			anyChanges = true;
		} else {
			setEnabled(false);
			displayRecords(currentEmployee);
		}
		return anyChanges;
	} // end checkForChanges

	private void saveChanges() {
		int returnVal = JOptionPane.showOptionDialog(this, "Do you want to save changes to current Employee?", "Save",
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
		if (returnVal == JOptionPane.YES_OPTION) {
			fileManager.getApplication().openWriteFile(fileManager.getFile().getAbsolutePath());
			currentEmployee = getChangedDetails();
			fileManager.getApplication().changeRecords(currentEmployee, currentByteStart);
			fileManager.getApplication().closeWriteFile();
			fileManager.setChangesMade(false);
		}
		displayRecords(currentEmployee);
		setEnabled(false);
	} // end saveChanges

	private boolean checkInput() {
		boolean valid = true;
		if (ppsField.isEditable() && ppsField.getText().trim().isEmpty()) {
			ppsField.setBackground(new Color(255, 150, 150));
			valid = false;
		}
		if (ppsField.isEditable() && correctPps(ppsField.getText().trim(), currentByteStart)) {
			ppsField.setBackground(new Color(255, 150, 150));
			valid = false;
		}
		if (surnameField.isEditable() && surnameField.getText().trim().isEmpty()) {
			surnameField.setBackground(new Color(255, 150, 150));
			valid = false;
		}
		if (firstNameField.isEditable() && firstNameField.getText().trim().isEmpty()) {
			firstNameField.setBackground(new Color(255, 150, 150));
			valid = false;
		}
		if (genderCombo.getSelectedIndex() == 0 && genderCombo.isEnabled()) {
			genderCombo.setBackground(new Color(255, 150, 150));
			valid = false;
		}
		if (departmentCombo.getSelectedIndex() == 0 && departmentCombo.isEnabled()) {
			departmentCombo.setBackground(new Color(255, 150, 150));
			valid = false;
		}
		try {
			Double.parseDouble(salaryField.getText());
			if (Double.parseDouble(salaryField.getText()) < 0) {
				salaryField.setBackground(new Color(255, 150, 150));
				valid = false;
			}
		} catch (NumberFormatException num) {
			if (salaryField.isEditable()) {
				salaryField.setBackground(new Color(255, 150, 150));
				valid = false;
			}
		}
		if (fullTimeCombo.getSelectedIndex() == 0 && fullTimeCombo.isEnabled()) {
			fullTimeCombo.setBackground(new Color(255, 150, 150));
			valid = false;
		}
		if (!valid) {
			JOptionPane.showMessageDialog(this, "Wrong values or format! Please check!");
		}
		if (ppsField.isEditable()) {
			setToWhite();
		}
		return valid;
	} // end checkInput

	private void setToWhite() {
		ppsField.setBackground(UIManager.getColor("TextField.background"));
		surnameField.setBackground(UIManager.getColor("TextField.background"));
		firstNameField.setBackground(UIManager.getColor("TextField.background"));
		salaryField.setBackground(UIManager.getColor("TextField.background"));
		genderCombo.setBackground(UIManager.getColor("TextField.background"));
		departmentCombo.setBackground(UIManager.getColor("TextField.background"));
		fullTimeCombo.setBackground(UIManager.getColor("TextField.background"));
	} // end setToWhite

	public boolean correctPps(String pps, long currentByte) {
		boolean ppsExist = false;
		if (pps.length() == 8 || pps.length() == 9) {
			if (Character.isDigit(pps.charAt(0)) && Character.isDigit(pps.charAt(1)) && Character.isDigit(pps.charAt(2))
					&& Character.isDigit(pps.charAt(3)) && Character.isDigit(pps.charAt(4))
					&& Character.isDigit(pps.charAt(5)) && Character.isDigit(pps.charAt(6))
					&& Character.isLetter(pps.charAt(7)) && (pps.length() == 8 || Character.isLetter(pps.charAt(8)))) {
				fileManager.getApplication().openReadFile(fileManager.getFile().getAbsolutePath());
				ppsExist = fileManager.getApplication().isPpsExist(pps, currentByte);
				fileManager.getApplication().closeReadFile();
			} else {
				ppsExist = true;
			}
		} else {
			ppsExist = true;
		}
		return ppsExist;
	} // end correctPPS

	public void firstRecord() {
		if (isSomeoneToDisplay()) {
			fileManager.getApplication().openReadFile(fileManager.getFile().getAbsolutePath());
			currentByteStart = fileManager.getApplication().getFirst();
			currentEmployee = fileManager.getApplication().readRecords(currentByteStart);
			fileManager.getApplication().closeReadFile();
			if (currentEmployee.getEmployeeId() == 0) {
				nextRecord();
			}
		}
	} // end firstRecord

	public void previousRecord() {
		if (isSomeoneToDisplay()) {
			fileManager.getApplication().openReadFile(fileManager.getFile().getAbsolutePath());
			currentByteStart = fileManager.getApplication().getPrevious(currentByteStart);
			currentEmployee = fileManager.getApplication().readRecords(currentByteStart);
			while (currentEmployee.getEmployeeId() == 0) {
				currentByteStart = fileManager.getApplication().getPrevious(currentByteStart);
				currentEmployee = fileManager.getApplication().readRecords(currentByteStart);
			}
			fileManager.getApplication().closeReadFile();
		}
	} // end previousRecord

	public void nextRecord() {
		if (isSomeoneToDisplay()) {
			fileManager.getApplication().openReadFile(fileManager.getFile().getAbsolutePath());
			currentByteStart = fileManager.getApplication().getNext(currentByteStart);
			currentEmployee = fileManager.getApplication().readRecords(currentByteStart);
			while (currentEmployee.getEmployeeId() == 0) {
				currentByteStart = fileManager.getApplication().getNext(currentByteStart);
				currentEmployee = fileManager.getApplication().readRecords(currentByteStart);
			}
			fileManager.getApplication().closeReadFile();
		}
	} // end nextRecord

	public void lastRecord() {
		if (isSomeoneToDisplay()) {
			fileManager.getApplication().openReadFile(fileManager.getFile().getAbsolutePath());
			currentByteStart = fileManager.getApplication().getLast();
			currentEmployee = fileManager.getApplication().readRecords(currentByteStart);
			fileManager.getApplication().closeReadFile();
			if (currentEmployee.getEmployeeId() == 0) {
				previousRecord();
			}
		}
	} // end lastRecord

	public void searchEmployeeById() {
		boolean found = false;
		try {
			if (isSomeoneToDisplay()) {
				firstRecord();
				int firstId = currentEmployee.getEmployeeId();
				if (searchByIdField.getText().trim().equals(idField.getText().trim())) {
					found = true;
				} else if (searchByIdField.getText().trim().equals(Integer.toString(currentEmployee.getEmployeeId()))) {
					found = true;
					displayRecords(currentEmployee);
				} else {
					nextRecord();
					while (firstId != currentEmployee.getEmployeeId()) {
						if (Integer.parseInt(searchByIdField.getText().trim()) == currentEmployee.getEmployeeId()) {
							found = true;
							displayRecords(currentEmployee);
							break;
						} else {
							nextRecord();
						}
					}
				}
				if (!found) {
					JOptionPane.showMessageDialog(this, "Employee not found!");
				}
			}
		} catch (NumberFormatException ex) {
			searchByIdField.setBackground(new Color(255, 150, 150));
			JOptionPane.showMessageDialog(this, "Wrong ID format!");
		}
		searchByIdField.setBackground(Color.WHITE);
		searchByIdField.setText("");
	} // end searchEmployeeByID

	public void searchEmployeeBySurname() {
		boolean found = false;
		if (isSomeoneToDisplay()) {
			firstRecord();
			String firstSurname = currentEmployee.getSurname().trim();
			if (searchBySurnameField.getText().trim().equalsIgnoreCase(surnameField.getText().trim())) {
				found = true;
			} else if (searchBySurnameField.getText().trim().equalsIgnoreCase(currentEmployee.getSurname().trim())) {
				found = true;
				displayRecords(currentEmployee);
			} else {
				nextRecord();
				while (!firstSurname.trim().equalsIgnoreCase(currentEmployee.getSurname().trim())) {
					if (searchBySurnameField.getText().trim().equalsIgnoreCase(currentEmployee.getSurname().trim())) {
						found = true;
						displayRecords(currentEmployee);
						break;
					} else {
						nextRecord();
					}
				}
			}
			if (!found) {
				JOptionPane.showMessageDialog(this, "Employee not found!");
			}
		}
		searchBySurnameField.setText("");
	} // end searchEmployeeBySurname

	public int getNextFreeId() {
		int nextFreeId = 0;
		if (fileManager.getFile().length() == 0 || !isSomeoneToDisplay()) {
			nextFreeId++;
		} else {
			lastRecord();
			nextFreeId = currentEmployee.getEmployeeId() + 1;
		}
		return nextFreeId;
	} // end getNextFreeId

	public void addRecord(Employee newEmployee) {
		fileManager.getApplication().openWriteFile(fileManager.getFile().getAbsolutePath());
		currentByteStart = fileManager.getApplication().addRecords(newEmployee);
		fileManager.getApplication().closeWriteFile();
	} // end addRecord

	private void deleteRecord() {
		if (isSomeoneToDisplay()) {
			int returnVal = JOptionPane.showOptionDialog(frame, "Do you want to delete record?", "Delete",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
			if (returnVal == JOptionPane.YES_OPTION) {
				fileManager.getApplication().openWriteFile(fileManager.getFile().getAbsolutePath());
				fileManager.getApplication().deleteRecords(currentByteStart);
				fileManager.getApplication().closeWriteFile();
				if (isSomeoneToDisplay()) {
					nextRecord();
					displayRecords(currentEmployee);
				}
			}
		}
	} // end deleteDecord

	private Vector<Object> getAllEmloyees() {
		Vector<Object> allEmployee = new Vector<>();
		Vector<Object> empDetails;
		long byteStart = currentByteStart;
		int firstId;

		firstRecord();
		firstId = currentEmployee.getEmployeeId();
		do {
			empDetails = new Vector<>();
			empDetails.addElement(currentEmployee.getEmployeeId());
			empDetails.addElement(currentEmployee.getPps());
			empDetails.addElement(currentEmployee.getSurname());
			empDetails.addElement(currentEmployee.getFirstName());
			empDetails.addElement(currentEmployee.getGender());
			empDetails.addElement(currentEmployee.getDepartment());
			empDetails.addElement(currentEmployee.getSalary());
			empDetails.addElement(currentEmployee.getFullTime());
			allEmployee.addElement(empDetails);
			nextRecord();
		} while (firstId != currentEmployee.getEmployeeId());
		currentByteStart = byteStart;

		return allEmployee;
	} // end getAllEmployees

	private void editDetails() {
		if (isSomeoneToDisplay()) {
			salaryField.setText(fieldFormat.format(currentEmployee.getSalary()));
			change = false;
			setEnabled(true);
		}
	} // end editDetails

	private void cancelChange() {
		setEnabled(false);
		displayRecords(currentEmployee);
	} // end cancelChange

	public boolean isSomeoneToDisplay() {
		boolean someoneToDisplay;
		fileManager.getApplication().openReadFile(fileManager.getFile().getAbsolutePath());
		someoneToDisplay = fileManager.getApplication().isSomeoneToDisplay();
		fileManager.getApplication().closeReadFile();
		if (!someoneToDisplay) {
			currentEmployee = null;
			idField.setText("");
			ppsField.setText("");
			surnameField.setText("");
			firstNameField.setText("");
			salaryField.setText("");
			genderCombo.setSelectedIndex(0);
			departmentCombo.setSelectedIndex(0);
			fullTimeCombo.setSelectedIndex(0);
			JOptionPane.showMessageDialog(this, "No Employees registered!");
		}
		return someoneToDisplay;
	} // end isSomeoneToDisplay

	private void displayEmployeeSummaryDialog() {
		if (isSomeoneToDisplay()) {
			new EmployeeSummaryDialog(getAllEmloyees());
		}
	} // end displayEmployeeSummaryDialog

	public void setEnabled(boolean boolVal) {
		boolean searchVal = !boolVal;
		ppsField.setEditable(boolVal);
		surnameField.setEditable(boolVal);
		firstNameField.setEditable(boolVal);
		genderCombo.setEnabled(boolVal);
		departmentCombo.setEnabled(boolVal);
		salaryField.setEditable(boolVal);
		fullTimeCombo.setEnabled(boolVal);
		saveChange.setVisible(boolVal);
		cancelChange.setVisible(boolVal);
		searchByIdField.setEnabled(searchVal);
		searchBySurnameField.setEnabled(searchVal);
		searchId.setEnabled(searchVal);
		searchSurname.setEnabled(searchVal);
	} // end setEnabled

	// content pane for main dialog
	private void createContentPane() {
		setTitle("Employee Details");
		JPanel dialog = new JPanel(new MigLayout());

		setJMenuBar(menuBar());// add menu bar to frame
		// add search panel to frame
		dialog.add(searchPanel(), "width 400:400:400, growx, pushx");
		// add navigation panel to frame
		dialog.add(navigPanel(), "width 150:150:150, wrap");
		// add button panel to frame
		dialog.add(buttonPanel(), "growx, pushx, span 2,wrap");
		// add details panel to frame
		dialog.add(detailsPanel(), "gap top 30, gap left 150, center");

		JScrollPane scrollPane = new JScrollPane(dialog);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		addWindowListener(this);
		setSize(760, 600);
		setLocation(250, 200);
	} // end createContentPane

	// create and show main dialog
	private static void createAndShowGUI() {

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.createContentPane();// add content pane to frame
		frame.setSize(760, 600);
		frame.setLocation(250, 200);
		frame.setVisible(true);
	}// end createAndShowGUI

	// main method
	public static void main(String args[]) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}// end main

	public void changedUpdate(DocumentEvent d) {
		change = true;
		new JTextFieldLimit(20);
	} // end changedUpdate

	public void insertUpdate(DocumentEvent d) {
		change = true;
		new JTextFieldLimit(20);
	} // end insertUpdate

	public void removeUpdate(DocumentEvent d) {
		change = true;
		new JTextFieldLimit(20);
	} // end removeUpdate

	@Override
	public void itemStateChanged(ItemEvent e) {
		change = true;
	} // end itemStateChanged

	@Override
	public void windowClosing(WindowEvent e) {
		handleExitAction();
	} // end windowClosing

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}
} // end class EmployeeDetails
