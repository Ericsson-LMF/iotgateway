package com.ericsson.deviceaccess.serviceschema.codegenerator.javabuilder;

/**
 *
 * @author delma
 */
public interface Component extends Javadocable, Accessable {

    /**
     * Gets name of the component
     * @return name
     */
    String getName();

    /**
     * Gets type of the componen
     * @return type
     */
    String getType();
    
}