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

package org.ourproject.kune.workspace.client.actions;

import org.ourproject.kune.platf.client.dispatch.Action;
import org.ourproject.kune.platf.client.rpc.AsyncCallbackSimple;
import org.ourproject.kune.platf.client.rpc.GroupService;
import org.ourproject.kune.platf.client.rpc.GroupServiceAsync;
import org.ourproject.kune.platf.client.state.Session;
import org.ourproject.kune.workspace.client.sitebar.Site;
import org.ourproject.kune.workspace.client.workspace.Workspace;

public class ChangeGroupWsThemeAction implements Action<String> {

    private final Workspace workspace;
    private final Session session;

    public ChangeGroupWsThemeAction(final Session session, final Workspace workspace) {
        this.session = session;
        this.workspace = workspace;
    }

    public void execute(final String theme) {
        onChangeGroupWsTheme(theme);
    }

    private void onChangeGroupWsTheme(final String theme) {
        Site.showProgressProcessing();
        final GroupServiceAsync server = GroupService.App.getInstance();
        server.changeGroupWsTheme(session.getUserHash(), session.getCurrentState().getGroup().getShortName(), theme,
                new AsyncCallbackSimple<Object>() {
                    public void onSuccess(final Object result) {
                        workspace.setTheme(theme);
                        Site.hideProgress();
                    }
                });

    }

}