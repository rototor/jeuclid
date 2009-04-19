package cTree;

public class CMessage {

    private boolean message;

    /**
     * Default Constructor.
     */
    public CMessage(final boolean isTrue) {
        this.message = isTrue;
    }

    /**
     * Getter method for message.
     * 
     * @return the message
     */
    public boolean isMessage() {
        return this.message;
    }

    /**
     * Setter method for message.
     * 
     * @param message
     *            the message to set
     */
    public void setMessage(final boolean message) {
        this.message = message;
    }

}
