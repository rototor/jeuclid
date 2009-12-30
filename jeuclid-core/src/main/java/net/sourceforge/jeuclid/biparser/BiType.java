package net.sourceforge.jeuclid.biparser;

/**
 * different types of BiNode.
 *
 * NODE:    composite node, can have children
 * EMTPY:   no valid text node (e.g. between two open tags)
 * TEXT:    text node
 *
 * @author dominik
 */
public enum BiType {

    /** Node Type. */
    NODE,
    /** Empty Type. */
    EMPTY,
    /** Text Type. */
    TEXT
};
