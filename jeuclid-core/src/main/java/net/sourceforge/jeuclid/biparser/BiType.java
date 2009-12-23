package net.sourceforge.jeuclid.biparser;

/** 
 * different types of BiNode
 *
 * NODE:    composite node, can have children
 * EMTPY:   no valid text node (e.g. between two open tags <tag1>...<tag2>)
 * TEXT:    text node
 * 
 * @author dominik
 */
public enum BiType {

    NODE, EMPTY, TEXT
};
