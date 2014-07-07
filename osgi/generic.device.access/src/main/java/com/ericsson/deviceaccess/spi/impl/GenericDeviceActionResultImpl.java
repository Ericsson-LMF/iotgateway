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

package com.ericsson.deviceaccess.spi.impl;

import com.ericsson.deviceaccess.api.GenericDeviceActionResult;
import com.ericsson.deviceaccess.api.GenericDeviceException;
import com.ericsson.deviceaccess.api.GenericDeviceProperties;
import com.ericsson.deviceaccess.api.Serializable;
import com.ericsson.deviceaccess.spi.GenericDeviceAccessSecurity;

public class GenericDeviceActionResultImpl implements GenericDeviceActionResult {
    private int code;
    private String reason;
    private GenericDeviceProperties value;

    /**
     * @param result
     */
    public GenericDeviceActionResultImpl(GenericDeviceProperties result) {
        value = result;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * {@inheritDoc}
     */
    public int getCode() {
        return code;
    }

    /**
     * {@inheritDoc}
     */
    public GenericDeviceProperties getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * {@inheritDoc}
     */
    public String getReason() {
        return reason;
    }

    //@Override
	public String serialize(int format) throws GenericDeviceException {
		GenericDeviceAccessSecurity.checkGetPermission(getClass().getName());
        if (format == Serializable.FORMAT_JSON
                || format == Serializable.FORMAT_JSON_WDC) {
            StringBuffer sb = new StringBuffer("{");
            sb.append("\"code\":\"").append(getCode()).append("\",");
            sb.append("\"reason\":\"").append(getReason()).append("\",");
            if(value != null){
            	sb.append("\"value\":").append(getValue().serialize(format));
            } else {
            	sb.append("\"value\":null");
            }
            sb.append("}");
            
            return sb.toString();
        } else {
            throw new GenericDeviceException(405, "No such format supported");
        }	
	}
	

}