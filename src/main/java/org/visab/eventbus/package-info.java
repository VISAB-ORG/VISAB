/**
 * This package contains the high level event bus for communication as well as
 * generic interfaces to be implemented for specific features.
 * <p>
 * Basic communication on throughout the publish-subscribe pattern focuses on
 * the concept of a common event bus. Publishers kind of act like a datasource
 * to that bus, while subscribers are some kind of data sinks.
 * 
 * The bus itself handles the distribution of information by notifying
 * subscribers about an event that got put to it by a publisher.
 * </p>
 */
package org.visab.eventbus;