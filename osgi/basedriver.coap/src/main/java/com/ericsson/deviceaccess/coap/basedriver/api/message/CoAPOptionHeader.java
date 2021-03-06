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
package com.ericsson.deviceaccess.coap.basedriver.api.message;

import java.util.Arrays;
import java.util.Objects;

/**
 * This class represents an Option header in the CoAP message. At the moment the
 * Options are from core draft 07, block options draft 04 and observe option
 * draft 02.
 *
 */
public class CoAPOptionHeader implements Comparable<CoAPOptionHeader> {

    /**
     * Enum for option names
     */
    // Core draft 07 options + observe option (draft 02) + block options
    // (draft 04)
    // Odd numbers indicate a
    // critical option, while even numbers indicate an elective option.
    // Add more options if needed

    /*
     * Options MUST appear in order of their Option Number (see Section 5.4.5).
     * A delta encoding is used between options, with the Option Number for each
     * Option calculated as the sum of its Option Delta field and the Option
     * Number of the preceding Option in the message, if any, or zero otherwise.
     * Multiple options with the same Option Number can be included by using an
     * Option Delta of zero. Following the Option Delta, each option has a
     * Length field which specifies the length of the Option Value, in bytes.
     * The Length field can be extended by one byte for options with values
     * longer than 14 bytes. The Option Value immediately follows the Length
     * field.
     */

    /*
     * Option Delta: 4-bit unsigned integer. Indicates the difference between
     * the Option Number of this option and the previous option (or zero for the
     * first option). In other words, the Option Number is calculated by simply
     * summing the Option Delta fields of this and previous options before it.
     * The Option Numbers 14, 28, 42, ... are reserved for no-op options when
     * they are sent with an empty value (they are ignored) and can be used as
     * "fenceposts" if deltas larger than 15 would otherwise be required.
     */

    /*
     *
     * 0 1 2 3 4 5 6 7 +---+---+---+---+---+---+---+---+ | Option Delta | Length
     * | for 0..14 +---+---+---+---+---+---+---+---+ | Option Value ...
     * +---+---+---+---+---+---+---+---+ for 15..270:
     * +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+ |
     * Option Delta | 1 1 1 1 | Length - 15 |
     * +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+ |
     * Option Value ...
     * +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
     */
    private final CoAPOptionName optionName;

    /**
     * Length: Indicates the length of the Option Value, in bytes. Normally
     * Length is a 4-bit unsigned integer allowing value lengths of 0-14 bytes.
     * When the Length field is set to 15, another byte is added as an 8-bit
     * unsigned integer whose value is added to the 15, allowing option value
     * lengths of 15-270 bytes.
     */
    private final int length;

    private byte[] value;

    /**
     * Constructor
     *
     * @param name name of the option
     * @param value value of the option in bytes
     */
    public CoAPOptionHeader(CoAPOptionName name, byte[] value) {
        this.optionName = name;
        if (value != null) {
            length = value.length;
        } else {
            length = 0;
        }
        this.value = value;
    }

    /**
     * Constructor
     *
     * @param name name of the option
     */
    public CoAPOptionHeader(CoAPOptionName name) {
        this.optionName = name;
        this.value = null;
        this.length = 0;
    }

    /**
     * Constructor
     *
     * @param optionName name of the option
     * @param value value of the option header in bytes
     */
    public CoAPOptionHeader(String optionName, byte[] value) {
        this.optionName = CoAPOptionName.getFromName(optionName);
        if (value != null) {
            length = value.length;
        } else {
            length = 0;
        }
        this.value = value;
    }

    /**
     * Return the name of the option
     *
     * @return name of the option
     */
    public CoAPOptionName getOptionName() {
        return optionName;
    }

    /**
     * Return the number of the option
     *
     * @return number of the option
     */
    public int getOptionNumber() {
        return optionName.getNo();
    }

    // TODO add setters if needed
    /**
     * Indicates the length of the option value in bytes. Normally Length is a
     * 4-bit unsigned integer allowing value lengths of 0-14 bytes. When the
     * Length field is set to 15, another byte is added as an 8-bit unsigned
     * integer whose value is added to the 15, allowing option value lengths of
     * 15-270 bytes.
     *
     * @return length of the option value in bytes
     */
    public int getLength() {
        return length;
    }

    /**
     * Get the value of the option in bytes
     *
     * @return value of the option in bytes
     */
    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    /**
     * implementation of compareTo method of the Java comparable interface. This
     * is needed when ordering the list of options in a message
     *
     * @param header header to compare this header to
     */
    @Override
    public int compareTo(CoAPOptionHeader header) {
        return getOptionNumber() - header.getOptionNumber();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(optionName);
        hash = 59 * hash + Arrays.hashCode(value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final CoAPOptionHeader other = (CoAPOptionHeader) obj;
        if (optionName != other.optionName) {
            return false;
        }
        return Arrays.equals(value, other.value);
    }
}
