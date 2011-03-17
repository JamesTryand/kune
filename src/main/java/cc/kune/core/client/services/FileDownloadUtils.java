/*
 *
 * Copyright (C) 2007-2011 The kune development team (see CREDITS for details)
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
 \*/
package cc.kune.core.client.services;

import cc.kune.common.client.utils.Url;
import cc.kune.common.client.utils.UrlParam;
import cc.kune.core.client.state.Session;
import cc.kune.core.shared.domain.utils.StateToken;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;

public class FileDownloadUtils {

    private static final String DOWNLOADSERVLET = "servlets/FileDownloadManager";
    private static final String LOGODOWNLOADSERVLET = "servlets/EntityLogoDownloadManager";

    private final ImageUtils imageUtils;
    private final Session session;

    @Inject
    public FileDownloadUtils(final Session session, final ImageUtils imageUtils) {
        this.session = session;
        this.imageUtils = imageUtils;
    }

    private String calculateUrl(final StateToken token, final boolean download, final boolean useHash) {
        final Url url = new Url(GWT.getModuleBaseURL() + DOWNLOADSERVLET, new UrlParam(FileConstants.TOKEN,
                token.toString()));
        if (download) {
            url.add(new UrlParam(FileConstants.DOWNLOAD, download));
        }
        if (useHash) {
            final String hash = session.getUserHash();
            if (hash != null) {
                url.add(new UrlParam(FileConstants.HASH, hash));
            }
        }
        return url.toString();
    }

    public void downloadFile(final StateToken token) {
        final String url = calculateUrl(token, true, true);
        DOM.setElementAttribute(RootPanel.get("__download").getElement(), "src", url);
    }

    public String getImageResizedUrl(final StateToken token, final ImageSize imageSize) {
        return calculateUrl(token, false, true) + "&" + new UrlParam(FileConstants.IMGSIZE, imageSize.toString());
    }

    public String getImageUrl(final StateToken token) {
        return calculateUrl(token, false, true);
    }

    public String getLogoAvatarHtml(final StateToken groupToken, final boolean groupHasLogo, final boolean isPersonal,
            final int size, final int hvspace) {
        if (groupHasLogo) {
            return "<img hspace='" + hvspace + "' vspace='" + hvspace + "' align='left' style='width: " + size
                    + "px; height: " + size + "px;' src='" + getLogoImageUrl(groupToken) + "'>";
        } else {
            return isPersonal ? imageUtils.getImageHtml(ImageDescriptor.personDef)
                    : imageUtils.getImageHtml(ImageDescriptor.groupDefIcon);
        }
    }

    public String getLogoImageUrl(final StateToken token) {
        return new Url(GWT.getModuleBaseURL() + LOGODOWNLOADSERVLET,
                new UrlParam(FileConstants.TOKEN, token.toString())).toString();
    }

    public String getUrl(final StateToken token) {
        return calculateUrl(token, false, false);
    }
}