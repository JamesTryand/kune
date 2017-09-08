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
package cc.kune.core.server.xmpp;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.jivesoftware.smackx.muc.MultiUserChat;

// TODO: Auto-generated Javadoc
/**
 * The Class RoomConfigurationDumper.
 *
 * @author danigb@gmail.com
 * @author vjrj@ourproject.org (Vicente J. Ruiz Jurado)
 */
class RoomConfigurationDumper {

  /** The Constant LOG. */
  private static final Log LOG = LogFactory.getLog(RoomConfigurationDumper.class);

  /**
   * Log.
   *
   * @param options
   *          the options
   * @return the string
   */
  private static String log(final Collection<? extends Object> options) {
    String s = "";
    for (Object cs : options) {
      s += "| " + cs.toString();
    }
    return s;
  }

  /**
   * Log.
   *
   * @param string
   *          the string
   */
  private static void log(final String string) {
    LOG.debug(string);
  }

  /**
   * Show configuration.
   *
   * @param form
   *          the form
   */
  public static void showConfiguration(final Form form) {
    for (FormField formField : form.getFields()) {
      log("Field label: " + formField.getLabel());
      log("Field variable: " + formField.getVariable());
      log("Field type: " + formField.getType());
      log("Field desc: " + formField.getDescription());
      log("Field options: " + log(formField.getOptions()));
      log("Field values: " + log(formField.getValues()));
      log("Field end -------------------");
    }
  }

  /**
   * Configure2.
   *
   * @param muc
   *          the muc
   * @throws XMPPException
   *           the xMPP exception
 * @throws NotConnectedException
 * @throws NoResponseException
   * @throws InterruptedException
   */
  void configure2(final MultiUserChat muc) throws XMPPException, NoResponseException, NotConnectedException, InterruptedException {
    final Form form = muc.getConfigurationForm().createAnswerForm();
    form.setAnswer("muc#roomconfig_passwordprotectedroom", false);
    // form.setAnswer("muc#roomconfig_roomname",
    // mucRoomDialog.getRoomName());

    form.setAnswer("muc#roomconfig_persistentroom", true);

    // List owners = new ArrayList();
    // owners.add(SparkManager.getSessionManager().getBareAddress());
    // form.setAnswer("muc#roomconfig_roomowners", owners);

    // new DataFormDialog(groupChat, form);
    muc.sendConfigurationForm(form);

  }
}
