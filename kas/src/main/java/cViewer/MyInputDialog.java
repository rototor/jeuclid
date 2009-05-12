package cViewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class MyInputDialog extends JDialog implements ActionListener,
        PropertyChangeListener {
    /**
     * 
     */
    private static final long serialVersionUID = 20090512L;

    private String stringInput = null;

    private final JTextField textField;

    private final String solution;

    private final JOptionPane optionPane;

    public String getValidatedText() {
        return this.stringInput;
    }

    public MyInputDialog(final MathFrame mf, final String sol) {
        super(mf, true);
        this.solution = sol;
        this.setTitle("Berechne");
        this.textField = new JTextField(10);
        final Object[] options = { "Ok", "Abbruch" };
        this.optionPane = new JOptionPane(this.textField,
                JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION,
                null, options, options[0]);
        this.setContentPane(this.optionPane);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent we) {
                MyInputDialog.this.optionPane.setValue(new Integer(
                        JOptionPane.CLOSED_OPTION));
            }
        });
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(final ComponentEvent ce) {
                MyInputDialog.this.textField.requestFocusInWindow();
            }
        });
        this.textField.addActionListener(this);
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
                this.stringInput = this.textField.getText();
                if (this.solution.equals(this.stringInput)) {
                    this.textField.setText(null);
                    this.setVisible(false);
                } else {
                    this.textField.selectAll();
                    JOptionPane.showMessageDialog(MyInputDialog.this,
                            "Probiers doch mal mit." + this.solution,
                            "Versuchs nochmal!", JOptionPane.ERROR_MESSAGE);
                    this.stringInput = null;
                    this.textField.requestFocusInWindow();
                }
            } else {
                this.stringInput = null;
                this.textField.setText(null);
                this.setVisible(false);
            }
        }
    }
}
