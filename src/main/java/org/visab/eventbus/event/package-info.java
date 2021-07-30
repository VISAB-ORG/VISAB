/**
 * This package contains an abstract API base event, all concrete
 * implementations that inherit from it and two concrete API unrelated events.
 * 
 * <p>
 * The APIEventBase only provides a kind of template of information and
 * functionality that every event published at the bus should have. All specific
 * information and characteristics of events have to be implemented in a
 * specific event class.
 * </p>
 */
package org.visab.eventbus.event;