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

package org.ourproject.kune.workspace.client.ui.ctx.admin;

import java.util.Iterator;

import org.ourproject.kune.platf.client.View;
import org.ourproject.kune.platf.client.dto.AccessListsDTO;
import org.ourproject.kune.platf.client.dto.GroupDTO;
import org.ourproject.kune.platf.client.dto.GroupListDTO;
import org.ourproject.kune.platf.client.services.Images;
import org.ourproject.kune.platf.client.services.Kune;
import org.ourproject.kune.platf.client.ui.IconLabel;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AccessListsPanel extends VerticalPanel implements View {

    private static final Images img = Images.App.getInstance();
    private final VerticalPanel adminsVP;
    private final VerticalPanel editorsVP;
    private final VerticalPanel viewersVP;

    public AccessListsPanel() {
        final Label adminsLabel = new Label(Kune.I18N.t("Who can admin this:"));
        adminsVP = new VerticalPanel();
        final Label editorsLabel = new Label(Kune.I18N.t("Who more can edit:"));
        editorsVP = new VerticalPanel();
        final Label viewersLabel = new Label(Kune.I18N.t("Who more can view:"));
        viewersVP = new VerticalPanel();

        // Layout
        this.add(adminsLabel);
        this.add(adminsVP);
        this.add(editorsLabel);
        this.add(editorsVP);
        this.add(viewersLabel);
        this.add(viewersVP);

        // Set properties
        // FIXME: titledPanel.setTitle("Who can admin, edit or view this
        // content");
        // addStyleName("kune-AccessList");
        setWidth("100%");
        this.setWidth("100%");
        this.setCellWidth(adminsLabel, "100%");
        this.setCellWidth(editorsLabel, "100%");
        this.setCellWidth(viewersLabel, "100%");
        this.setCellWidth(adminsVP, "100%");
        this.setCellWidth(editorsVP, "100%");
        this.setCellWidth(viewersVP, "100%");

        adminsLabel.addStyleName("kune-AccessListSubLabel");
        editorsLabel.addStyleName("kune-AccessListSubLabel");
        viewersLabel.addStyleName("kune-AccessListSubLabel");

    }

    public void setAccessLists(final AccessListsDTO accessLists) {
        setGroupList(accessLists.getAdmins(), adminsVP);
        setGroupList(accessLists.getEditors(), editorsVP);
        setGroupList(accessLists.getViewers(), viewersVP);
    }

    private void setGroupList(final GroupListDTO groupList, final VerticalPanel groupVP) {
        groupVP.clear();
        if (groupList.getMode() == GroupListDTO.EVERYONE) {
            groupVP.add(new IconLabel(img.everybody(), Kune.I18N.t("Everybody")));
        } else if (groupList.getMode() == GroupListDTO.NOBODY) {
            groupVP.add(new IconLabel(img.nobody(), Kune.I18N.t("Nobody")));
        } else {
            final Iterator<GroupDTO> iter = groupList.getList().iterator();
            while (iter.hasNext()) {
                final GroupDTO next = iter.next();
                groupVP.add(new IconLabel(img.groupDefIcon(), next.getLongName()));
            }
        }
    }
}