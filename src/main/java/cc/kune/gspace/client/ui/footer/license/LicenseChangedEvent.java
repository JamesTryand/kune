/*
 *
 * Copyright (C) 2007-2015 Licensed to the Comunes Association (CA) under
 * one or more contributor license agreements (see COPYRIGHT for details).
 * The CA licenses this file to you under the GNU Affero General Public
 * License version 3, (the "License"); you may not use this file except in
 * compliance with the License. This file is part of kune.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package cc.kune.gspace.client.ui.footer.license;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.EventBus;

// TODO: Auto-generated Javadoc
/**
 * The Class LicenseChangedEvent.
 *
 * @author vjrj@ourproject.org (Vicente J. Ruiz Jurado)
 */
public class LicenseChangedEvent extends GwtEvent<LicenseChangedEvent.LicenseChangedHandler> {

  /**
   * The Interface HasLicenseChangedHandlers.
   *
   * @author vjrj@ourproject.org (Vicente J. Ruiz Jurado)
   */
  public interface HasLicenseChangedHandlers extends HasHandlers {

    /**
     * Adds the license changed handler.
     *
     * @param handler
     *          the handler
     * @return the handler registration
     */
    HandlerRegistration addLicenseChangedHandler(LicenseChangedHandler handler);
  }

  /**
   * The Interface LicenseChangedHandler.
   *
   * @author vjrj@ourproject.org (Vicente J. Ruiz Jurado)
   */
  public interface LicenseChangedHandler extends EventHandler {

    /**
     * On license changed.
     *
     * @param event
     *          the event
     */
    public void onLicenseChanged(LicenseChangedEvent event);
  }

  /** The Constant TYPE. */
  private static final Type<LicenseChangedHandler> TYPE = new Type<LicenseChangedHandler>();

  /**
   * Fire.
   *
   * @param eventBus
   *          the source
   */
  public static void fire(final EventBus eventBus) {
    eventBus.fireEvent(new LicenseChangedEvent());
  }

  /**
   * Gets the type.
   *
   * @return the type
   */
  public static Type<LicenseChangedHandler> getType() {
    return TYPE;
  }

  /**
   * Instantiates a new license changed event.
   */
  public LicenseChangedEvent() {
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared
   * .EventHandler)
   */
  @Override
  protected void dispatch(final LicenseChangedHandler handler) {
    handler.onLicenseChanged(this);
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object obj) {
    return super.equals(obj);
  }

  /*
   * (non-Javadoc)
   *
   * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
   */
  @Override
  public Type<LicenseChangedHandler> getAssociatedType() {
    return TYPE;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return super.hashCode();
  }

  /*
   * (non-Javadoc)
   *
   * @see com.google.web.bindery.event.shared.Event#toString()
   */
  @Override
  public String toString() {
    return "LicenseChangedEvent[" + "]";
  }
}
