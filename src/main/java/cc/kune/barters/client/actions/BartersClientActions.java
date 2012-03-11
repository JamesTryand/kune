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
 \*/
package cc.kune.barters.client.actions;

import static cc.kune.barters.shared.BartersConstants.TYPE_BARTER;
import static cc.kune.barters.shared.BartersConstants.TYPE_FOLDER;
import static cc.kune.barters.shared.BartersConstants.TYPE_ROOT;
import cc.kune.core.client.actions.ActionRegistryByType;
import cc.kune.core.client.i18n.I18nUITranslationService;
import cc.kune.core.client.resources.CoreResources;
import cc.kune.core.client.state.Session;
import cc.kune.core.client.state.StateManager;
import cc.kune.gspace.client.actions.AbstractFoldableToolActions;
import cc.kune.gspace.client.actions.ActionGroups;
import cc.kune.gspace.client.actions.AddAdminMembersToContentMenuItem;
import cc.kune.gspace.client.actions.AddAllMembersToContentMenuItem;
import cc.kune.gspace.client.actions.AddCollabMembersToContentMenuItem;
import cc.kune.gspace.client.actions.AddPublicToContentMenuItem;
import cc.kune.gspace.client.actions.ContentViewerOptionsMenu;
import cc.kune.gspace.client.actions.ContentViewerShareMenu;
import cc.kune.gspace.client.actions.CopyContentMenuItem;
import cc.kune.gspace.client.actions.ParticipateInContentBtn;
import cc.kune.gspace.client.actions.RefreshContentMenuItem;
import cc.kune.gspace.client.actions.TutorialContainerBtn;
import cc.kune.gspace.client.actions.WriteToParticipantsMenuItem;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class BartersClientActions extends AbstractFoldableToolActions {
  final String[] all = { TYPE_ROOT, TYPE_FOLDER, TYPE_BARTER };
  final String[] containers = { TYPE_ROOT, TYPE_FOLDER };
  final String[] containersNoRoot = { TYPE_FOLDER };
  final String[] contents = { TYPE_BARTER };

  @Inject
  public BartersClientActions(final I18nUITranslationService i18n, final Session session,
      final StateManager stateManager, final ActionRegistryByType registry, final CoreResources res,
      final Provider<GoParentFolderBtn> folderGoUp, final Provider<NewBartersBtn> newBartersBtn,
      final Provider<NewFolderBtn> newFolderBtn,
      final Provider<OpenBartersMenuItem> openContentMenuItem,
      final Provider<DelBartersMenuItem> delContentMenuItem,
      final Provider<ContentViewerOptionsMenu> optionsMenuContent,
      final Provider<ContentViewerShareMenu> shareMenuContent,
      final Provider<AddAllMembersToContentMenuItem> addAllMenuItem,
      final Provider<AddAdminMembersToContentMenuItem> addAdminMembersMenuItem,
      final Provider<AddCollabMembersToContentMenuItem> addCollabMembersMenuItem,
      final Provider<AddPublicToContentMenuItem> addPublicMenuItem,
      final Provider<TutorialContainerBtn> tutorialBtn,
      final Provider<ParticipateInContentBtn> participateBtn,
      final Provider<DelFolderMenuItem> delFolderMenuItem,
      final Provider<RefreshContentMenuItem> refresh, final Provider<CopyContentMenuItem> copyContent,
      final Provider<WriteToParticipantsMenuItem> writeToParticipants) {
    super(session, stateManager, i18n, registry);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, optionsMenuContent, all);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, refresh, all);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, folderGoUp, contents);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, folderGoUp, containers);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, shareMenuContent, contents);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, addAllMenuItem, contents);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, addAdminMembersMenuItem, contents);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, addCollabMembersMenuItem, contents);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, addPublicMenuItem, contents);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, tutorialBtn, containers);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, newBartersBtn, containers);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, participateBtn, contents);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, newFolderBtn, containers);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, copyContent, contents);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, writeToParticipants, contents);
    actionsRegistry.addAction(ActionGroups.ITEM_MENU, openContentMenuItem, contents);
    actionsRegistry.addAction(ActionGroups.ITEM_MENU, openContentMenuItem, containersNoRoot);
    actionsRegistry.addAction(ActionGroups.ITEM_MENU, delContentMenuItem, contents);
    actionsRegistry.addAction(ActionGroups.ITEM_MENU, delFolderMenuItem, containersNoRoot);
    actionsRegistry.addAction(ActionGroups.ITEM_MENU, addAllMenuItem, contents);
    actionsRegistry.addAction(ActionGroups.ITEM_MENU, addAdminMembersMenuItem, contents);
    actionsRegistry.addAction(ActionGroups.ITEM_MENU, addCollabMembersMenuItem, contents);
    actionsRegistry.addAction(ActionGroups.ITEM_MENU, addPublicMenuItem, contents);
    actionsRegistry.addAction(ActionGroups.ITEM_MENU, copyContent, contents);
    actionsRegistry.addAction(ActionGroups.ITEM_MENU, writeToParticipants, contents);
  }

  @Override
  protected void createPostSessionInitActions() {
  }
}
