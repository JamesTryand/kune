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

package org.ourproject.kune.workspace.client.actions.i18n;

import org.ourproject.kune.platf.client.dispatch.Action;
import org.ourproject.kune.platf.client.dto.GetTranslationActionParams;
import org.ourproject.kune.platf.client.rpc.I18nService;
import org.ourproject.kune.platf.client.rpc.I18nServiceAsync;
import org.ourproject.kune.platf.client.state.Session;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetTranslationAction implements Action<GetTranslationActionParams> {
    private final Session session;

    public GetTranslationAction(final Session session) {
        this.session = session;
    }

    public void execute(final GetTranslationActionParams params) {
        onGetTranslation(params.getLanguage(), params.getText());
    }

    private void onGetTranslation(final String language, final String text) {
        final I18nServiceAsync server = I18nService.App.getInstance();
        server.getTranslation(session.getUserHash(), language, text, new AsyncCallback<String>() {
            public void onFailure(final Throwable caught) {
            }

            public void onSuccess(final String result) {
            }
        });

    }
}