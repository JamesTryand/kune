/*
 *
 * Copyright (C) 2007-2008 The kune development team (see CREDITS for details)
 * This file is part of kune.
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
package org.ourproject.kune.chat.client;

import java.util.Date;

import org.ourproject.kune.platf.client.services.I18nTranslationService;
import org.ourproject.kune.workspace.client.ui.newtmp.skel.WorkspaceSkeleton;

import com.calclab.emite.client.im.roster.RosterManager.SubscriptionMode;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteuimodule.client.EmiteUIDialog;
import com.calclab.emiteuimodule.client.UserChatOptions;
import com.calclab.emiteuimodule.client.params.AvatarProvider;
import com.calclab.emiteuimodule.client.status.OwnPresence.OwnStatus;
import com.calclab.suco.client.signal.Slot;
import com.google.gwt.user.client.Window;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;

class ChatEngineXmpp implements ChatEngine {
    private final ChatOptions chatOptions;
    private final EmiteUIDialog emiteDialog;
    private final I18nTranslationService i18n;
    private final WorkspaceSkeleton ws;
    private ToolbarButton traybarButton;

    public ChatEngineXmpp(final EmiteUIDialog emiteUIDialog, final ChatOptions chatOptions,
	    final I18nTranslationService i18n, final WorkspaceSkeleton ws) {
	this.emiteDialog = emiteUIDialog;
	this.chatOptions = chatOptions;
	this.i18n = i18n;
	this.ws = ws;
    }

    public ChatOptions getChatOptions() {
	return chatOptions;
    }

    public void joinRoom(final String roomName, final String userAlias) {
	if (emiteDialog.isLoggedIn()) {
	    final XmppURI roomURI = XmppURI.uri(roomName + "@" + chatOptions.roomHost + "/"
		    + chatOptions.userOptions.getUserJid().getNode());
	    emiteDialog.joinRoom(roomURI);
	} else {
	    ws.showAlertMessage(i18n.t("Error"), i18n.t("To join a chatroom you need to be 'online'."));
	}
    }

    public void login(final String jid, final String passwd) {
	final UserChatOptions userChatOptions = getUserChatOptions(jid, passwd);
	// FIXME: Avatar provider
	final AvatarProvider avatarProvider = new AvatarProvider() {
	    public String getAvatarURL(XmppURI userURI) {
		return "images/person-def.gif";
	    }
	};
	final String initialWindowTitle = Window.getTitle();
	chatOptions.userOptions = userChatOptions;
	emiteDialog.start(userChatOptions, chatOptions.httpBase, chatOptions.roomHost, initialWindowTitle,
		avatarProvider, i18n.t("Chat"));
	emiteDialog.show(OwnStatus.online);
	if (traybarButton == null) {
	    traybarButton = new ToolbarButton();
	    traybarButton.setTooltip(i18n.t("Show/hide the chat window"));
	    traybarButton.setIcon("images/emite-chat.gif");
	    traybarButton.addListener(new ButtonListenerAdapter() {
		@Override
		public void onClick(final Button button, final EventObject e) {
		    if (emiteDialog.isVisible()) {
			emiteDialog.hide();
		    } else {
			emiteDialog.show();
		    }
		}

	    });
	    ws.getSiteTraybar().addButton(traybarButton);
	}
	emiteDialog.setEnableStatusUI(true);
	emiteDialog.hide();
	emiteDialog.onChatAttended(new Slot<String>() {
	    public void onEvent(final String parameter) {
		Window.setTitle(initialWindowTitle);
	    }
	});
	emiteDialog.onChatUnattendedWithActivity(new Slot<String>() {
	    public void onEvent(final String chatTitle) {
		Window.setTitle("(* " + chatTitle + ") " + initialWindowTitle);
	    }
	});
    }

    public void logout() {
	if (!emiteDialog.isDialogNotStarted()) {
	    emiteDialog.setOwnPresence(OwnStatus.offline);
	    final UserChatOptions userChatOptions = getUserChatOptions("reset@example.com", "");
	    emiteDialog.refreshUserInfo(userChatOptions);
	    chatOptions.userOptions = userChatOptions;
	    emiteDialog.setEnableStatusUI(false);
	}
    }

    private UserChatOptions getUserChatOptions(final String jid, final String passwd) {
	final String resource = "emiteui-" + new Date().getTime() + "-kune"; // +
	// getGwtMetaProperty(GWT_PROPERTY_RELEASE);
	// FIXME, get this from user profile
	return new UserChatOptions(jid + "@" + chatOptions.domain, passwd, resource, "blue",
		SubscriptionMode.autoAcceptAll, true);
    }
}
