/**
 * This package contains the an event bus implementation, that is used for
 * communicating API related events aswell as API unreleated events.
 * <p>
 * Basic communication on throughout the publish-subscribe pattern focuses on
 * the concept of a common event bus. Publishers act like a datasource to that
 * bus, while subscribers are data sinks.
 * 
 * The bus itself handles the distribution of information by notifying
 * subscribers about an event that got was published by a publisher.
 * </p>
 */
package org.visab.eventbus;