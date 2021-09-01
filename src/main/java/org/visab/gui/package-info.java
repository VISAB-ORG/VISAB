/**
 * This package contains (currently) prototypical implementations for the
 * revised / enriched GUI of VISAB.
 * <p>
 * The new GUI needs to meet different requirements than the legacy one. To
 * support different games and make the application actually scalable VISAB will
 * feature the MVVM (Model, View, Viewmodel) pattern.
 * 
 * The basic idea is to have a view that is actually only responsible for
 * displaying the information as an access point. The viewmodel is directly
 * connected with the view by "databinding" which enables both of these objects
 * to always have the same value. Furthermore the viewmodel handles
 * modifications to the UI status when information changes. At least there
 * should be a model connected to the viewmodel to further abstract all business
 * logic from the actual views. Any processing of information or access to other
 * sources shall be handled by this underlying model.
 * 
 * <a href="https://github.com/sialcasa/mvvmFX">MVVMFX GitHub project</a>
 *
 * </p>
 */
package org.visab.gui;