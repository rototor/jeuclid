package cViewer;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class MyComboDialog extends JDialog implements ActionListener,
        PropertyChangeListener {

    private static final long serialVersionUID = 20090512L;

    private final JComboBox jCombobox;

    private final JOptionPane optionPane;

    private final TransferObject transfer;

    public MyComboDialog(final MathFrame mf, final TransferObject transfer) {

        super(mf, true);
        this.transfer = transfer;
        this.setTitle("Wähle: ");
        final String[] strArray = new String[3];
        strArray[0] = "Vorzeichen";
        strArray[1] = "erster Faktor";
        strArray[2] = "letzter Faktor";
        this.jCombobox = new JComboBox(strArray);
        this.jCombobox.setFont(new Font("Dialog", 1, 16));
        final Object[] options = { "Ok", "Abbruch" };
        this.optionPane = new JOptionPane(this.jCombobox,
                JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION,
                null, options, options[0]);
        this.setContentPane(this.optionPane);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent we) {
                MyComboDialog.this.optionPane.setValue(new Integer(
                        JOptionPane.CLOSED_OPTION));
            }
        });
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(final ComponentEvent ce) {
                MyComboDialog.this.jCombobox.requestFocusInWindow();
            }
        });
        this.jCombobox.addActionListener(this);
        this.optionPane.addPropertyChangeListener(this);
    }

    public void actionPerformed(final ActionEvent e) {
        this.optionPane.setValue("Ok");
    }

    public void propertyChange(final PropertyChangeEvent e) {
        final String prop = e.getPropertyName();
        if (this.isVisible()
                && (e.getSource() == this.optionPane)
                && (JOptionPane.VALUE_PROPERTY.equals(prop) || JOptionPane.INPUT_VALUE_PROPERTY
                        .equals(prop))) {
            final Object value = this.optionPane.getValue();

            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                return;
            }

            this.optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

            if ("Ok".equals(value)) {
                this.transfer.setContent((String) this.jCombobox
                        .getSelectedItem());
                this.setVisible(false);
            } else {
                this.setVisible(false);
            }
        }
    }
}
