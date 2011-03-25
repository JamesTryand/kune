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
 */
package org.ourproject.kune.workspace.client.tool;

import org.ourproject.kune.workspace.client.skel.WorkspaceSkeleton;
import org.ourproject.kune.workspace.client.themes.WsThemeManager;

import cc.kune.gspace.client.tool.ToolSelector;
import cc.kune.gspace.client.tool.ToolSelectorItemPanel;
import cc.kune.gspace.client.tool.ToolSelectorItemPresenter;

public abstract class OldAbstractClientTool implements OldClientTool {

    public OldAbstractClientTool(final String shortName, final String longName, final ToolSelector toolSelector,
            final WsThemeManager wsThemePresenter, final WorkspaceSkeleton ws) {
        final ToolSelectorItemPresenter presenter = new ToolSelectorItemPresenter(shortName, longName, toolSelector);
        // wsThemePresenter);
        final ToolSelectorItemPanel panel = new ToolSelectorItemPanel();
        presenter.init(panel);
    }

}