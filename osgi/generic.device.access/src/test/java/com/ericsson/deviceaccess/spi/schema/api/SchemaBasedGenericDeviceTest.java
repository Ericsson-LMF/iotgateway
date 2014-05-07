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

package com.ericsson.deviceaccess.spi.schema.api;

import com.ericsson.deviceaccess.api.GenericDevice;
import com.ericsson.deviceaccess.spi.schema.*;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class SchemaBasedGenericDeviceTest {
    private Mockery context = new Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Before
    public void setup() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAddSchemaBasedService() {
        final SchemaBasedService service = context.mock(SchemaBasedService.class);

        context.checking(new Expectations() {
            {
                oneOf(service).setParentDevice(with(aNonNull(GenericDevice.class)));
                oneOf(service).getName();
                will(returnValue("myService"));
                oneOf(service).validateSchema();
                oneOf(service).updatePath("undefined/undefined");
            }
        });

        SchemaBasedGenericDevice device = new SchemaBasedGenericDevice() {
            {
                addSchemaBasedService(service);
            }
        };

        context.assertIsSatisfied();

        assertSame(service, device.getService("myService"));
    }

    @Test
    public void testErrors_addAlreadyExisting() {
        final SchemaBasedService service = context.mock(SchemaBasedService.class);

        context.checking(new Expectations() {
            {
                oneOf(service).setParentDevice(with(aNonNull(GenericDevice.class)));
                oneOf(service).getName();
                will(returnValue("alreadyExistingService"));
                oneOf(service).validateSchema();
                oneOf(service).updatePath("undefined/undefined");
                oneOf(service).getName();
                will(returnValue("alreadyExistingService"));
            }
        });

        try {
            SchemaBasedGenericDevice device = new SchemaBasedGenericDevice() {
                {
                    addSchemaBasedService(service);
                    addSchemaBasedService(service);
                }
            };
            fail("should have been exception here");
        } catch (ServiceSchemaError e) {
            // success
        }

        context.assertIsSatisfied();

    }

    @Test
    public void testCreateCustomService() {
        final ServiceSchema serviceSchema = context.mock(ServiceSchema.class);

        context.checking(new Expectations() {
            {
                oneOf(serviceSchema).getName();
                will(returnValue("myService"));
                oneOf(serviceSchema).getName();
                will(returnValue("myService"));
                oneOf(serviceSchema).getPropertiesSchemas();
                will(returnValue(new ParameterSchema[0]));
                oneOf(serviceSchema).getActionSchemas();
                will(returnValue(new ActionSchema[0]));
                oneOf(serviceSchema).getPropertiesSchemas();
                will(returnValue(new ParameterSchema[0]));
                oneOf(serviceSchema).getActionSchemas();
                will(returnValue(new ActionSchema[0]));
            }
        });

        SchemaBasedGenericDevice device = new SchemaBasedGenericDevice() {
            {
                addSchemaBasedService(createService(serviceSchema));
            }
        };

        context.assertIsSatisfied();

        assertEquals("myService", device.getService("myService").getName());
    }
}
