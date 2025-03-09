import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public abstract class AbstractSearchDialog extends JDialog implements ActionListener {
	EmployeeDetails parent;
	JButton search, cancel;
	JTextField searchField;

	public AbstractSearchDialog(EmployeeDetails parent) {
		this.parent = parent;
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		JScrollPane scrollPane = new JScrollPane(createPane());
		setContentPane(scrollPane);
		getRootPane().setDefaultButton(search);
		setSize(500, 190);
		setLocation(350, 250);
	}

	private Container createPane() {
		JPanel searchPanel = new JPanel(new GridLayout(3, 1));
		JPanel textPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JLabel label = new JLabel(getPanelTitle());

		searchPanel.add(label);
		textPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		JLabel prompt = new JLabel(getPromptLabel());
		prompt.setFont(parent.font1);
		textPanel.add(prompt);
		textPanel.add(searchField = new JTextField(20));
		searchField.setFont(parent.font1);
		searchField.setDocument(new JTextFieldLimit(20));
		buttonPanel.add(search = new JButton("Search"));
		search.addActionListener(this);
		search.requestFocus();
		buttonPanel.add(cancel = new JButton("Cancel"));
		cancel.addActionListener(this);
		searchPanel.add(textPanel);
		searchPanel.add(buttonPanel);
		return searchPanel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == search) {
			performSearch();
		} else if (e.getSource() == cancel) {
			dispose();
		}
	}

	protected abstract String getPanelTitle();

	protected abstract String getPromptLabel();

	protected abstract void performSearch();
}
