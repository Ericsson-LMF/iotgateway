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
package com.ericsson.deviceaccess.coap.basedriver.api.resources;

import com.ericsson.deviceaccess.coap.basedriver.api.resources.CoAPResource.CoAPResourceType;
import java.net.URI;
import java.net.URISyntaxException;
import junit.framework.TestCase;

public class CoAPResourceTest extends TestCase {

    private CoAPResource resource;

    public CoAPResourceTest() {
        super("CoAPResourceTest");
        try {
            URI uri = new URI("coap://localhost:5683/test");

            resource = new CoAPResource(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void testSetUri() {
        try {
            URI uri = new URI("coap://localhost:5683/test2");
            resource.setUri(uri);
            assertEquals(resource.getUri(), uri);

            uri = new URI("coap://localhost/test2");
            resource.setUri(uri);
            assertEquals(resource.getUri(), uri);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void testSetCoAPResourceType() {
        resource.setCoAPResourceType(CoAPResourceType.HELLO_WORLD);
        assertEquals(resource.getCoAPResourceType(),
                CoAPResourceType.HELLO_WORLD);
        assertFalse(resource.getCoAPResourceType() == CoAPResourceType.CARELESS);
    }

    /*
     public void testAddObserver() {
     Mockery context = new Mockery() {
     {
     setImposteriser(ClassImposteriser.INSTANCE);
     }
     };

     final CoAPResourceObserver observer = context
     .mock(CoAPResourceObserver.class);

     resource.addObserver(observer);
     assertEquals(1, resource.getObservers().size());
     assertTrue(resource.removeObserver(observer));

     }*/
}
