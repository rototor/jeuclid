package cViewer;

public class ViewerFactory {

    private volatile static ViewerFactory uniqueInstance;

    private JMathComponent math;

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
}
