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

package org.ourproject.kune.workspace.client.title;

import org.ourproject.kune.platf.client.ui.EditableClickListener;
import org.ourproject.kune.platf.client.ui.EditableIconLabel;
import org.ourproject.kune.workspace.client.skel.SimpleToolbar;
import org.ourproject.kune.workspace.client.skel.WorkspaceSkeleton;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class EntityTitlePanel implements EntityTitleView {
    private final EditableIconLabel titleLabel;
    private final Label dateLabel;
    private final Image icon;

    public EntityTitlePanel(final WorkspaceSkeleton ws, final EntityTitlePresenter presenter) {
	icon = new Image();
	titleLabel = new EditableIconLabel(new EditableClickListener() {
	    public void onEdited(final String text) {
		presenter.onTitleRename(text);
	    }
	});
	dateLabel = new Label();

	final SimpleToolbar wsTitle = ws.getEntityWorkspace().getTitle();
	wsTitle.add(icon);
	wsTitle.add(titleLabel);
	wsTitle.addFill();
	wsTitle.add(dateLabel);
	icon.addStyleName("kune-Margin-Large-l");
	titleLabel.addStyleName("kune-Margin-Medium-l");
	titleLabel.addStyleName("kune-ft17px");
	titleLabel.ensureDebugId("k-entity-title-title");
	dateLabel.addStyleName("kune-Margin-Large-r");
	dateLabel.addStyleName("kune-ft12px");
	dateLabel.ensureDebugId("k-entity-title-date");
    }

    public void restoreOldTitle() {
	titleLabel.restoreOldText();
    }

    public void setContentDate(final String date) {
	dateLabel.setText(date);
    }

    public void setContentIcon(final String iconUrl) {
	icon.setUrl(iconUrl);
    }

    public void setContentIconVisible(final boolean visible) {
	icon.setVisible(visible);
	if (visible) {
	    titleLabel.removeStyleName("kune-Margin-Large-l");
	    titleLabel.addStyleName("kune-Margin-Medium-l");
	} else {
	    titleLabel.removeStyleName("kune-Margin-Medium-l");
	    titleLabel.addStyleName("kune-Margin-Large-l");
	}
    }

    public void setContentTitle(final String title) {
	titleLabel.setText(title);
    }

    public void setContentTitleEditable(final boolean editable) {
	titleLabel.setEditable(editable);
    }

    public void setDateVisible(final boolean visible) {
	dateLabel.setVisible(visible);
    }

}