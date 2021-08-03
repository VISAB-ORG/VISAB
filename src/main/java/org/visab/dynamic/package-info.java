/**
 * This package contains classes responsible creating instances of classes at
 * runtime based on their fully classified class name.
 * <p>
 * The DyanmicInstantiator is used by the SessionListenerFactory of the
 * processing package to avoid having to distinct games by a switch case. The
 * DynamicSerializer is used to deserialize json data received by the API
 * controller directly into the games corresponding java object.
 * </p>
 */
package org.visab.dynamic;