import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class NavigationPanel extends JPanel implements ActionListener {
    EmployeeDetails parent;
    JButton first, previous, next, last;
    
    public NavigationPanel(EmployeeDetails parent) {
        this.parent = parent;
        setBorder(javax.swing.BorderFactory.createTitledBorder("Navigate"));
        
        first = new JButton(new ImageIcon(
                new ImageIcon("first.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH)));
        first.setPreferredSize(new Dimension(17, 17));
        first.addActionListener(this);
        first.setToolTipText("Display first Record");
        add(first);
        
        previous = new JButton(new ImageIcon(
                new ImageIcon("prev.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH)));
        previous.setPreferredSize(new Dimension(17, 17));
        previous.addActionListener(this);
        previous.setToolTipText("Display previous Record");
        add(previous);
        
        next = new JButton(new ImageIcon(
                new ImageIcon("next.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH)));
        next.setPreferredSize(new Dimension(17, 17));
        next.addActionListener(this);
        next.setToolTipText("Display next Record");
        add(next);
        
        last = new JButton(new ImageIcon(
                new ImageIcon("last.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH)));
        last.setPreferredSize(new Dimension(17, 17));
        last.addActionListener(this);
        last.setToolTipText("Display last Record");
        add(last);

        parent.first = first;
        parent.previous = previous;
        parent.next = next;
        parent.last = last;
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == first) {
            parent.actionPerformed(new ActionEvent(parent.first, 0, ""));
        } else if (e.getSource() == previous) {
            parent.actionPerformed(new ActionEvent(parent.previous, 0, ""));
        } else if (e.getSource() == next) {
            parent.actionPerformed(new ActionEvent(parent.next, 0, ""));
        } else if (e.getSource() == last) {
            parent.actionPerformed(new ActionEvent(parent.last, 0, ""));
        }
    }
}
