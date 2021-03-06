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
package cc.kune.bootstrap.client.actions.ui;

import org.gwtbootstrap3.client.ui.base.button.CustomButton;
import org.gwtbootstrap3.client.ui.constants.Toggle;

import cc.kune.common.client.actions.Action;
import cc.kune.common.client.actions.ui.AbstractGuiItem;
import cc.kune.common.client.actions.ui.descrip.ButtonDescriptor;
import cc.kune.common.client.actions.ui.descrip.GuiActionDescrip;
import cc.kune.common.client.tooltip.Tooltip;
import cc.kune.common.shared.res.KuneIcon;

import com.google.gwt.resources.client.ImageResource;

/**
 * The Class AbstractBSButtonGui.
 *
 * @author vjrj@ourproject.org (Vicente J. Ruiz Jurado)
 */
public abstract class AbstractBSButtonGui extends AbstractBSChildGuiItem {

  /** The button. */
  private CustomButton button;
  /** The enable tongle. */
  protected boolean enableTongle;

  /**
   * Instantiates a new abstract gwt button gui.
   */
  public AbstractBSButtonGui() {
    this(null, false);
  }

  /**
   * Instantiates a new abstract gwt button gui.
   *
   * @param enableTongle
   *          the enable tongle
   */
  public AbstractBSButtonGui(final boolean enableTongle) {
    this(null, enableTongle);
  }

  /**
   * Instantiates a new abstract gwt button gui.
   *
   * @param buttonDescriptor
   *          the button descriptor
   */
  public AbstractBSButtonGui(final ButtonDescriptor buttonDescriptor) {
    this(buttonDescriptor, false);
  }

  /**
   * Instantiates a new abstract gwt button gui.
   *
   * @param buttonDescriptor
   *          the button descriptor
   * @param enableTongle
   *          the enable tongle
   */
  public AbstractBSButtonGui(final ButtonDescriptor buttonDescriptor, final boolean enableTongle) {
    super(buttonDescriptor);
    this.enableTongle = enableTongle;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * cc.kune.common.client.actions.ui.AbstractChildGuiItem#addStyle(java.lang
   * .String)
   */
  @Override
  protected void addStyle(final String style) {
    button.addStyleName(style);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * cc.kune.common.client.actions.ui.AbstractChildGuiItem#create(cc.kune.common
   * .client.actions.ui.descrip.GuiActionDescrip)
   */
  @Override
  public AbstractGuiItem create(final GuiActionDescrip descriptor) {
    super.descriptor = descriptor;

    button = new CustomButton();
    if (enableTongle) {
      button.setDataToggle(Toggle.BUTTON);
    }
    final String value = (String) descriptor.getValue(Action.STYLES);
    if (value != null) {
      setStyles(value);
    }

    final String id = descriptor.getId();
    if (id != null) {
      button.ensureDebugId(id);
    }
    if (descriptor.isChild()) {
      // If button is inside a toolbar don't init...
      child = button;
    } else {
      initWidget(button);
    }
    button.addClickHandler(clickHandlerDefault);
    super.create(descriptor);
    configureItemFromProperties();
    return this;
  }

  /*
   * (non-Javadoc)
   *
   * @see cc.kune.common.client.actions.ui.AbstractGuiItem#setEnabled(boolean)
   */
  @Override
  public void setEnabled(final boolean enabled) {
    button.setEnabled(enabled);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * cc.kune.common.client.actions.ui.AbstractGuiItem#setIcon(cc.kune.common
   * .shared.res.KuneIcon)
   */
  @Override
  public void setIcon(final KuneIcon icon) {
    button.setIcon(icon);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * cc.kune.common.client.actions.ui.AbstractGuiItem#setIconBackground(java
   * .lang.String)
   */
  @Override
  public void setIconBackColor(final String backgroundColor) {
    button.setIconBackColor(backgroundColor);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * cc.kune.common.client.actions.ui.AbstractGuiItem#setIconResource(com.google
   * .gwt.resources.client.ImageResource)
   */
  @Override
  public void setIconResource(final ImageResource icon) {
    button.setIconResource(icon);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * cc.kune.common.client.actions.ui.AbstractGuiItem#setIconStyle(java.lang
   * .String)
   */
  @Override
  protected void setIconStyle(final String style) {
    button.setIconStyle(style);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * cc.kune.common.client.actions.ui.AbstractGuiItem#setIconUrl(java.lang.String
   * )
   */
  @Override
  public void setIconUrl(final String url) {
    button.setIconUrl(url);
  }

  /**
   * Sets the pressed.
   *
   * @param pressed
   *          the new pressed
   */
  public void setPressed(final boolean pressed) {
    button.setActive(pressed);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * cc.kune.common.client.actions.ui.AbstractGuiItem#setText(java.lang.String)
   */
  @Override
  public void setText(final String text) {
    // FIXME descriptor.getDirection()
    button.setText(text);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * cc.kune.common.client.actions.ui.AbstractGuiItem#setToolTipText(java.lang
   * .String)
   */
  @Override
  public void setToolTipText(final String tooltipText) {
    Tooltip.to(button, tooltipText);
  }

  /*
   * (non-Javadoc)
   *
   * @see com.google.gwt.user.client.ui.UIObject#setVisible(boolean)
   */
  @Override
  public void setVisible(final boolean visible) {
    button.setVisible(visible);
  }

  /*
   * (non-Javadoc)
   *
   * @see cc.kune.common.client.actions.ui.AbstractGuiItem#shouldBeAdded()
   */
  @Override
  public boolean shouldBeAdded() {
    return !descriptor.isChild();
  }

}
