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

package com.ericsson.deviceaccess.api;

import java.util.Dictionary;

/**
 * GDA Property Events are mapped and delivered to applications according to the OSGi whiteboard model.
 * An application that wishes to be notified of events generated by a particular GAD Device registers a
 * service extending this interface.
 * <p/>
 * The execution of the GenericDeviceEventListener object must be done asynchronous (i.e., in a separate thread)
 * with respect to the originator (i.e., the GDA adaptor).
 * <p/>
 * A GDAEvent Listener service filter the events it receives. This event set is limited using a standard
 * framework filter expression (LDAP) which is specified when the listener service is registered.
 * <p/>
 * Events will always be issued when the device online status or device name changes.
 * <p/>
 * The filter is specified in a property with the name specified by the constant
 * {@link GenericDeviceEventListener#GENERICDEVICE_FILTER} and has as a value an object of type
 * {@link org.osgi.framework.Filter}.
 * <p/>
 * When the Filter is evaluated, the following keywords are recognized as defined as literal constants in the
 * GDA Device.
 * <ul>
 * <li>{@link GenericDeviceEventListener#DEVICE_NAME}: specify that the listener only wants events from the device with
 * the specified name; if not specified events will be issued for all devices</li>
 * <li>{@link GenericDeviceEventListener#DEVICE_ID}: specify that the listener only wants events from the device with
 * the specified ID; if not specified events will be issued for all devices</li>
 * <li>{@link GenericDeviceEventListener#DEVICE_URN}: specify that the listener only wants events from the device with
 * the specified URN; if not specified events will be issued for all devices</li>
 * <li>{@link GenericDeviceEventListener#SERVICE_NAME}: specify that the listener only wants events from the service
 * with the specified name; if not specified events will be issued for all services.</li>
 * <li>{@link GenericDeviceEventListener#DEVICE_ONLINE}: specify that the listener only wants events from devices
 * with the specified online status; if not specified events will be issued regardless of online status.</li>
 * <li>{@link GenericDeviceEventListener#DEVICE_STATE}: specify that the listener only wants events from devices
 * with the specified state; if not specified events will be issued regardless of state.</li>
 * <li>{@link GenericDeviceEventListener#DEVICE_PROTOCOL}: specify that the listener only wants events from devices
 * that are created by the specifid protocol adaptor; if not specified events will be issued regardless of protocol.</li>
 * <li><i>&lt;any service property&gt;</i>: specify that the listener only wants events for changes in the specified
 * service property; typically used with '*' but could also be used in conditions like <i>temp>30</i>; if not specified
 * all property changes trigger an event.</li>
 * </ul>
 * <p/>
 * Example:
 * <pre>
 *   org.osgi.framework.BundleContext bc = ...;
 *   GenericDeviceEventListener myListener = ...;
 *   // listen only for changes where temp exceeds 30 or power sinks below 100 on device with ID=zwave31
 *   final org.osgi.framework.Filter filter = org.osgi.framework.FrameworkUtil.createFilter("(&(device.id=zwave31)(|(temp >= 31)(power <= 99)))");
 *   bc.registerService(GenericDeviceEventListener.class.getName(), myListener, new Properties() {{
 *       put(GENERICDEVICE_FILTER, filter);
 *   }});
 * </pre>
 * <p/>
 * Example filters:
 * <ul>
 * <li>(device.name=tempsensor1): listen for changes in any property in any service in the device with name
 * 'tempsensor1' .</li>
 * <li>(device.id=zwave1): listen for changes in any property in any service in the device with ID 'zwave1' .</li>
 * <li>(&(device.id=zwave1)(service.name=TemperatureSensor)): listen for changes in any property in the
 * 'TemperatureSensor' service in the device with ID 'zwave1' .</li>
 * <li>(&(device.id=zwave1)(service.name=TemperatureSensor)(CurrentTemperature >= 30)): be notified each time the
 * 'CurrentTemperature' property, in the
 * 'TemperatureSensor' service, in the device with ID 'zwave1', changes and is greater than or equal to 30.</li>
 * <li>(&(service.name=TemperatureSensor)(CurrentTemperature>=30)): be notified each time the
 * 'CurrentTemperature' property, in the
 * 'TemperatureSensor' service, in any device, changes and is greater than or equal to 30.</li>
 * <li>(&(CurrentTemperature>=30)): be notified each time the
 * 'CurrentTemperature' property, in any service, in any device, changes and is greater than or equal to 30.</li>
 * <li>NOT IMPLEMENTED YET (&(device.online=true)(|(CurrentPower=*)(CurrentTemperature=*))): be notified each time any of the
 * 'CurrentTemperature' or 'CurrentPower' property changes, in any service, in devices that are online.</li>
 * </ul>
 */ 
public interface GenericDeviceEventListener {
    public static final String GENERICDEVICE_FILTER = "genericdevice.filter";
    public static final String DEVICE_ID = "device.id";
    public static final String DEVICE_URN = "device.urn";
    public static final String DEVICE_NAME = "device.name";
    public static final String DEVICE_PROTOCOL = "device.protocol";
    public static final String DEVICE_ONLINE = "device.online";
    public static final String DEVICE_STATE = "device.state";
    public static final String SERVICE_NAME = "service.name";


    /**
     * Called when changes according to the specified filter, specified at service registration, occurs.
     * <p/>
     * NOTE, it must return immediately! Any longer work shall be done in a separate thread.
     *
     * @param deviceId    the ID of the device that the change affect
     * @param serviceName the name of the service that the change affect
     * @param properties  the properties that was changed
     */
    public void notifyGenericDeviceEvent(String deviceId, String serviceName, Dictionary properties);
    public void notifyGenericDevicePropertyRemovedEvent(String deviceId, String serviceName, String propertyId);
    public void notifyGenericDevicePropertyAddedEvent(String deviceId, String serviceName, String propertyId);
}
