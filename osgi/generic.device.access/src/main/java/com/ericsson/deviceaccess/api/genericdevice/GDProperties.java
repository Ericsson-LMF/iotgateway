/*
 * Copyright Ericsson AB 2011-2014. All Rights Reserved.
 *
 * The contents of this file are subject to the Lesser GNU Public License,
 *  (the "License"), either version 2.1 of the License, or
 * (at your option) any later version.; you may not use this file except in
 * compliance with the License. You should have received a copy of the
 * License along with this software. If not, it can be
 * retrieved online at https://www.gnu.org/licenses/lgpl.html. Moreover
 * it could also be requested from Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * BECAUSE THE LIBRARY IS LICENSED FREE OF CHARGE, THERE IS NO
 * WARRANTY FOR THE LIBRARY, TO THE EXTENT PERMITTED BY APPLICABLE LAW.
 * EXCEPT WHEN OTHERWISE STATED IN WRITING THE COPYRIGHT HOLDERS AND/OR
 * OTHER PARTIES PROVIDE THE LIBRARY "AS IS" WITHOUT WARRANTY OF ANY KIND,

 * EITHER EXPRESSED OR IMPLIED, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE. THE ENTIRE RISK AS TO THE QUALITY AND PERFORMANCE OF THE
 * LIBRARY IS WITH YOU. SHOULD THE LIBRARY PROVE DEFECTIVE,
 * YOU ASSUME THE COST OF ALL NECESSARY SERVICING, REPAIR OR CORRECTION.
 *
 * IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN WRITING
 * WILL ANY COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MAY MODIFY AND/OR
 * REDISTRIBUTE THE LIBRARY AS PERMITTED ABOVE, BE LIABLE TO YOU FOR
 * DAMAGES, INCLUDING ANY GENERAL, SPECIAL, INCIDENTAL OR CONSEQUENTIAL
 * DAMAGES ARISING OUT OF THE USE OR INABILITY TO USE THE LIBRARY
 * (INCLUDING BUT NOT LIMITED TO LOSS OF DATA OR DATA BEING RENDERED
 * INACCURATE OR LOSSES SUSTAINED BY YOU OR THIRD PARTIES OR A FAILURE
 * OF THE LIBRARY TO OPERATE WITH ANY OTHER SOFTWARE), EVEN IF SUCH
 * HOLDER OR OTHER PARTY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 */
package com.ericsson.deviceaccess.api.genericdevice;

import com.ericsson.common.util.serialization.View;
import com.ericsson.deviceaccess.api.Serializable;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.Map;

/**
 * A set of named properties.
 */
public interface GDProperties extends Serializable {

    /**
     * Placeholder for Android to replace with the stub implementation for this
     * interface
     *
     * @author knt
     */
    public static abstract class Stub implements GDProperties {
    }

    String CURRENT_VALUE = "currentValue";
    String METADATA = "metadata";

    /**
     * Tester to check if a property identified by the key is in the object or
     * not.
     *
     * @param key Name of the property in question.
     * @return Whether or not the object contains a property with the key
     */
    boolean hasProperty(String key);

    /**
     * Getter for a property identified by the key that returns the value as
     * integer.
     *
     * @param key Name of the property in question.
     * @return Value of the property identified by the parameter "key". Returns
     * default value from service schema if the value is not set.
     */
    int getIntValue(String key);

    /**
     * Getter for a property identified by the key that returns the value as
     * long.
     *
     * @param key Name of the property in question.
     * @return Value of the property identified by the parameter "key". Returns
     * default value from service schema if the value is not set.
     */
    long getLongValue(String key);

    /**
     * Getter for a property identified by the key that returns the value as
     * float.
     *
     * @param key Name of the property in question.
     * @return Value of the property identified by the parameter "key". Returns
     * default value from service schema if the value is not set.
     */
    float getFloatValue(String key);

    /**
     * Setter for an long property identified by the key.
     *
     * @param key Name of the property in question.
     * @param value An integer value to be set for the property.
     */
    void setLongValue(String key, long value);

    /**
     * Setter for an integer property identified by the key.
     *
     * @param key Name of the property in question.
     * @param value An integer value to be set for the property.
     */
    void setIntValue(String key, int value);

    /**
     * Setter for a float property identified by the key.
     *
     * @param key Name of the property in question.
     * @param value A float value to be set for the property.
     */
    void setFloatValue(String key, float value);

    /**
     * Getter for the type of the property identified by the key.
     *
     * @param key Name of the property in question.
     * @return Simple class name of the value identified by the key. Null if no
     * property is found by the key.
     */
    String getValueType(String key);

    /**
     * Getter for a property identified by the key that returns the value as
     * string.
     *
     * @param key Name of the property in question.
     * @return Value of the property identified by the parameter "key". Returns
     * default value from service schema if the value is not set.
     */
    String getStringValue(String key);

    /**
     * Setter for a string property identified by the key. If the metadata
     * indicates that this is a number the string is parsed to that number type.
     *
     * @param key Name of the property in question.
     * @param value A string value to be set for the property.
     */
    void setStringValue(String key, String value);

    /**
     * Getter for a map from name to value.
     *
     * @return map from name to value
     */
    @JsonAnyGetter
    @JsonProperty("properties")
    Map<String, Data> getProperties();

    /**
     * Gets the value with the specified name. The only allowed types are
     * {@link String}, {@link Integer} and {@link Float}
     *
     * @param name
     * @return
     */
    Object getValue(String name);

    /**
     * Adds all elements from the specified properties.
     *
     * @param source
     */
    void addAll(GDProperties source);

    /**
     * Serializes the state (i.e. values of all properties) to JSON
     *
     * @return Example: <code>{"property1" : "99","property2" : "99"}</code>
     *
     */
    String serializeState();

    public static class Data {

        public Object currentValue;
        @JsonView(View.Stateless.class)
        public GDPropertyMetadata metadata;

        public Data(GDPropertyMetadata metadata) {
            this.metadata = metadata;
        }

        public Data setToDefault() {
            if (String.class.equals(metadata.getType())) {
                currentValue = metadata.getDefaultStringValue();
            } else {
                currentValue = metadata.getDefaultNumberValue();
            }
            return this;
        }

        public Data set(Object object) {
            currentValue = object;
            return this;
        }
    }
}
