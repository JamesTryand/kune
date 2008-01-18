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

package org.ourproject.kune.platf.client.services;

import org.ourproject.kune.platf.client.dispatch.DefaultDispatcher;
import org.ourproject.kune.platf.client.errors.AccessViolationException;
import org.ourproject.kune.platf.client.errors.AlreadyGroupMemberException;
import org.ourproject.kune.platf.client.errors.AlreadyUserMemberException;
import org.ourproject.kune.platf.client.errors.ContentNotFoundException;
import org.ourproject.kune.platf.client.errors.GroupNotFoundException;
import org.ourproject.kune.platf.client.errors.LastAdminInGroupException;
import org.ourproject.kune.platf.client.errors.SessionExpiredException;
import org.ourproject.kune.platf.client.errors.UserMustBeLoggedException;
import org.ourproject.kune.platf.client.state.Session;
import org.ourproject.kune.sitebar.client.Site;
import org.ourproject.kune.workspace.client.WorkspaceEvents;

import com.google.gwt.core.client.GWT;

public class KuneErrorHandler {
    private static KuneErrorHandler instance;
    private final Session session;

    public KuneErrorHandler(final Session session) {
        this.session = session;
        instance = this;
    }

    public void process(final Throwable caught) {
        Site.hideProgress();
        try {
            throw caught;
        } catch (final AccessViolationException e) {
            Site.error(Kune.I18N.t("You don't have rights to do that"));
        } catch (final SessionExpiredException e) {
            doSessionExpired();
        } catch (final UserMustBeLoggedException e) {
            if (session.isLogged()) {
                doSessionExpired();
            } else {
                Site.important(Kune.I18N.t("Please sign in or register to collaborate"));
            }
        } catch (final GroupNotFoundException e) {
            Site.error(Kune.I18N.t("Group not found"));
            DefaultDispatcher.getInstance().fire(WorkspaceEvents.GOTO, "", null);
        } catch (final ContentNotFoundException e) {
            Site.error(Kune.I18N.t("Content not found"));
            DefaultDispatcher.getInstance().fire(WorkspaceEvents.GOTO, "", null);
        } catch (final LastAdminInGroupException e) {
            Site.showAlertMessage(Kune.I18N.t("Sorry, you are the last admin of this group."
                    + " Look for someone to substitute you appropriately as admin before unjoin this group."));
        } catch (final AlreadyGroupMemberException e) {
            Site.error(Kune.I18N.t("This group is already a group member"));
        } catch (final AlreadyUserMemberException e) {
            Site.error(Kune.I18N.t("This user is already a member of this group"));
        } catch (final Throwable e) {
            Site.error(Kune.I18N.t("Error performing operation"));
            GWT.log("Other kind of exception in StateManagerDefault/processErrorException", null);
            throw new RuntimeException();
        }
    }

    private void doSessionExpired() {
        Site.doLogout();
        Site.showAlertMessage(Kune.I18N.t("Your session has expired. Please login again."));
    }

    public static KuneErrorHandler getInstance() {
        return instance;
    }

}