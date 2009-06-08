/*
 *
 * Copyright (C) 2007-2009 The kune development team (see CREDITS for details)
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
package org.ourproject.kune.workspace.client.options;

import org.ourproject.kune.platf.client.PlatfMessages;
import org.ourproject.kune.platf.client.app.EntityOptionsGroup;
import org.ourproject.kune.platf.client.i18n.I18nTranslationService;
import org.ourproject.kune.platf.client.services.Images;
import org.ourproject.kune.platf.client.ui.dialogs.tabbed.AbstractTabbedDialogPanel;
import org.ourproject.kune.workspace.client.entityheader.EntityHeader;
import org.ourproject.kune.workspace.client.entityheader.EntityHeaderButton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class EntityOptionsPanel extends AbstractTabbedDialogPanel implements EntityOptionsView {

    public static final String ENTITY_OP_PANEL_ID = "k-eop-diagpan";
    public static final String GROUP_OPTIONS_ERROR_ID = "k-eop-err-mess";
    public static final String GROUP_OPTIONS_ICON = "k-eop-icon";
    private final I18nTranslationService i18n;
    private final EntityHeader entityHeader;
    private EntityHeaderButton optionsButton;
    private final Images images;

    private final EntityOptionsGroup entityPreferencesGroup;

    public EntityOptionsPanel(final EntityOptions presenter, final EntityHeader entityHeader,
            final I18nTranslationService i18n, final Images images, final EntityOptionsGroup entityOptionsGroup) {
        super(ENTITY_OP_PANEL_ID, "", 400, HEIGHT + 80, 400, HEIGHT + 80, false, images, GROUP_OPTIONS_ERROR_ID);
        this.entityHeader = entityHeader;
        this.i18n = i18n;
        this.images = images;
        this.entityPreferencesGroup = entityOptionsGroup;
        super.setIconCls("k-options-icon");
        createOptionsButton();
    }

    @Override
    public void createAndShow() {
        // FIXME: use onAfterCreate in Factory/Module
        createAndShowItImpl();
    }

    public void setButtonVisible(final boolean visible) {
        optionsButton.setVisible(visible);
    }

    public void setGroupTitle() {
        super.setTitle(i18n.t(PlatfMessages.ENT_OPTIONS_GROUP_TITLE));
        optionsButton.setText(i18n.t(PlatfMessages.ENT_OPTIONS_GROUP_TITLE));
    }

    public void setPersonalTitle() {
        super.setTitle(i18n.t(PlatfMessages.ENT_OPTIONS_USER_TITLE));
        optionsButton.setText(i18n.t(PlatfMessages.ENT_OPTIONS_USER_TITLE));
    }

    private void createAndShowItImpl() {
        entityPreferencesGroup.createAll();
        super.createAndShow();
    }

    private void createOptionsButton() {
        optionsButton = new EntityHeaderButton("", images.emblemSystem());
        optionsButton.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                createAndShowItImpl();
            }
        });
        optionsButton.ensureDebugId(GROUP_OPTIONS_ICON);
        entityHeader.addWidget(optionsButton);
    }
}
