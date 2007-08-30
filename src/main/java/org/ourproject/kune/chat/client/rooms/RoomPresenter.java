/*
 * Copyright (C) 2007 The kune development team (see CREDITS for details)
 * This file is part of kune.
 *
 * Kune is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kune is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.ourproject.kune.chat.client.rooms;

import java.util.HashMap;
import java.util.Map;

import org.ourproject.kune.chat.client.rooms.RoomUser.UserType;
import org.ourproject.kune.platf.client.View;

import com.calclab.gwtjsjac.client.mandioca.XmppRoom;

public class RoomPresenter implements Room {

    private final static String[] USERCOLORS = { "green", "navy", "black", "grey", "olive", "teal", "blue", "lime",
	    "purple", "fuchsia", "maroon", "red" };

    private int currentColor;

    private RoomView view;
    private String input;
    private String subject;
    private final String sessionUserAlias;
    private final Map users;
    private final String roomName;

    private final RoomUserList userList;

    private XmppRoom handler;

    private final RoomListener listener;

    public RoomPresenter(final RoomListener listener, final String roomName, final String userAlias,
	    final UserType userType, final RoomUserList userList) {
	this.listener = listener;
	this.roomName = roomName;
	this.sessionUserAlias = userAlias;
	this.userList = userList;
	this.input = "";
	this.currentColor = 0;
	users = new HashMap();
    }

    public void init(final RoomView view) {
	this.view = view;
	view.showRoomName(roomName);
    }

    public View getView() {
	return view;
    }

    public void addMessage(final String userAlias, final String message) {
	RoomUser user = (RoomUser) users.get(userAlias);
	if (user == null) {
	    throw new RuntimeException("Trying to send a chat message with a user not in this room");
	}
	view.showMessage(user.getAlias(), user.getColor(), message);
	listener.onMessageReceived(this);
    }

    public void addInfoMessage(final String message) {
	view.showInfoMessage(message);
    }

    public void addUser(final String alias, final UserType type) {
	RoomUser user = new RoomUser(alias, getNextColor(), type);
	getUsersList().add(user);
	users.put(alias, user);
    }

    public void addDelimiter(final String datetime) {
	view.showDelimiter(datetime);
    }

    // TODO: ¿no bastaría con saveInput(null)?
    public void clearSavedInput() {
	input = null;
    }

    public String getSessionUserAlias() {
	return sessionUserAlias;
    }

    public void saveInput(final String inputText) {
	input = inputText;
    }

    public String getSavedInput() {
	return input;
    }

    protected void doClose() {
	// TODO: xmpp: send bye in room
    }

    public String getSubject() {
	return subject;
    }

    public void setSubject(final String subject) {
	this.subject = subject;
    }

    public String getRoomName() {
	return roomName;
    }

    private String getNextColor() {
	String color = USERCOLORS[currentColor++];
	if (currentColor >= USERCOLORS.length) {
	    currentColor = 0;
	}
	return color;
    }

    public RoomUserList getUsersList() {
	return userList;
    }

    public RoomUserListView getUsersListView() {
	return userList.getView();
    }

    public void setHandler(final XmppRoom handler) {
	this.handler = handler;
	listener.onRoomReady(this);
    }

    public boolean isReady() {
	return handler != null;
    }

    public XmppRoom getHandler() {
	return handler;
    }

}
