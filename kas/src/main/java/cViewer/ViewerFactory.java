package cViewer;

public class ViewerFactory {

    private volatile static ViewerFactory uniqueInstance;

    private JMathComponent math;

    private MathFrame frame;

    private ViewerFactory() {
        // Empty on purpose
    }

    public static ViewerFactory getInst() {
        if (ViewerFactory.uniqueInstance == null) {
            synchronized (ViewerFactory.class) {
                if (ViewerFactory.uniqueInstance == null) {
                    ViewerFactory.uniqueInstance = new ViewerFactory();
                }
            }
        }
        return ViewerFactory.uniqueInstance;
    }

    public JMathComponent getMathComponent() {
        if (this.math == null) {
            this.math = new JMathComponent();
        }
        return this.math;
    }

    public MathFrame getMathFrame() {
        if (this.frame == null) {
            this.frame = new MathFrame();
        }
        return this.frame;
    }

    public MyInputDialog getDialog(final String text) {
        final MyInputDialog dialog = new MyInputDialog(this.getMathFrame(),
                text);
        dialog.pack();
        dialog.setLocationRelativeTo(this.getMathComponent());
        dialog.setVisible(true);
        return dialog;
    }

}
