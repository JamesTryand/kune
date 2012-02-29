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
package cc.kune.docs.client.actions;

import static cc.kune.docs.shared.DocsConstants.TYPE_DOCUMENT;
import static cc.kune.docs.shared.DocsConstants.TYPE_FOLDER;
import static cc.kune.docs.shared.DocsConstants.TYPE_ROOT;
import static cc.kune.docs.shared.DocsConstants.TYPE_UPLOADEDFILE;
import cc.kune.chat.client.actions.ChatAboutContentBtn;
import cc.kune.common.client.actions.ui.descrip.MenuDescriptor;
import cc.kune.core.client.actions.ActionRegistryByType;
import cc.kune.core.client.i18n.I18nUITranslationService;
import cc.kune.core.client.registry.NewMenusForTypeIdsRegistry;
import cc.kune.core.client.resources.CoreResources;
import cc.kune.core.client.state.Session;
import cc.kune.core.client.state.StateManager;
import cc.kune.gspace.client.actions.AbstractFoldableToolActions;
import cc.kune.gspace.client.actions.ActionGroups;
import cc.kune.gspace.client.actions.AddAdminMembersToContentMenuItem;
import cc.kune.gspace.client.actions.AddAllMembersToContentMenuItem;
import cc.kune.gspace.client.actions.AddCollabMembersToContentMenuItem;
import cc.kune.gspace.client.actions.ContentViewerOptionsMenu;
import cc.kune.gspace.client.actions.ParticipateInContentBtn;
import cc.kune.gspace.client.actions.RefreshContentMenuItem;
import cc.kune.gspace.client.actions.SetAsHomePageMenuItem;
import cc.kune.gspace.client.actions.TutorialContainerBtn;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class DocsClientActions extends AbstractFoldableToolActions {

  final String[] all = { TYPE_ROOT, TYPE_FOLDER, TYPE_DOCUMENT, TYPE_UPLOADEDFILE };
  final String[] containers = { TYPE_ROOT, TYPE_FOLDER };
  final String[] containersNoRoot = { TYPE_FOLDER };
  final String[] contents = { TYPE_DOCUMENT, TYPE_UPLOADEDFILE };
  final String[] contentsModerated = { TYPE_DOCUMENT, TYPE_UPLOADEDFILE };
  final String[] root = { TYPE_ROOT };

  @Inject
  public DocsClientActions(final I18nUITranslationService i18n, final Session session,
      final StateManager stateManager, final ActionRegistryByType registry, final CoreResources res,
      final Provider<GoParentFolderBtn> folderGoUp, final Provider<NewDocMenuItem> newDocMenuItem,
      final Provider<NewDocIconBtn> newDocIconBtn, final Provider<NewFolderMenuItem> newFolderMenuItem,
      final Provider<OpenDocMenuItem> openContentMenuItem, final Provider<NewFolderBtn> newFolderBtn,
      final Provider<DelDocMenuItem> delContentMenuItem,
      final Provider<ContentViewerOptionsMenu> optionsMenuContent,
      final Provider<AddAllMembersToContentMenuItem> addAllMenuItem,
      final Provider<AddAdminMembersToContentMenuItem> addAdminMembersMenuItem,
      final Provider<AddCollabMembersToContentMenuItem> addCollabMembersMenuItem,
      final Provider<ParticipateInContentBtn> participateBtn,
      final Provider<DelFolderMenuItem> delFolderMenuItem,
      final Provider<TutorialContainerBtn> tutorialBtn, final Provider<ChatAboutContentBtn> chatAbout,
      final Provider<RefreshContentMenuItem> refresh,
      final Provider<SetAsHomePageMenuItem> setAsHomePage,
      final NewMenusForTypeIdsRegistry newMenusRegistry, final DocsFolderNewMenu foldersNewMenu,
      final DocsNewMenu docsNewMenu) {
    super(session, stateManager, i18n, registry);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, optionsMenuContent, all);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, refresh, all);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, addAllMenuItem, contents);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, addAdminMembersMenuItem, contents);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, addCollabMembersMenuItem, contents);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, newDocIconBtn, all);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, newFolderBtn, containers);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, foldersNewMenu, containers);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, docsNewMenu, contents);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, newDocMenuItem, containers);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, folderGoUp, contents);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, folderGoUp, containers);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, tutorialBtn, containers);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, participateBtn, contents);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, newFolderMenuItem, containers);
    actionsRegistry.addAction(ActionGroups.TOOLBAR, chatAbout, contents);
    actionsRegistry.addAction(ActionGroups.ITEM_MENU, openContentMenuItem, contents);
    actionsRegistry.addAction(ActionGroups.ITEM_MENU, openContentMenuItem, containersNoRoot);
    actionsRegistry.addAction(ActionGroups.ITEM_MENU, delContentMenuItem, contents);
    actionsRegistry.addAction(ActionGroups.ITEM_MENU, delFolderMenuItem, containersNoRoot);
    actionsRegistry.addAction(ActionGroups.ITEM_MENU, setAsHomePage, contents);
    actionsRegistry.addAction(ActionGroups.ITEM_MENU, addAllMenuItem, contents);
    actionsRegistry.addAction(ActionGroups.ITEM_MENU, addAdminMembersMenuItem, contents);
    actionsRegistry.addAction(ActionGroups.ITEM_MENU, addCollabMembersMenuItem, contents);
    newMenusRegistry.register(TYPE_FOLDER, foldersNewMenu.get());
    newMenusRegistry.register(TYPE_ROOT, foldersNewMenu.get());
    newMenusRegistry.register(TYPE_DOCUMENT,
        (MenuDescriptor) docsNewMenu.get().withText(i18n.t("Add Gadget")));
    newMenusRegistry.register(TYPE_UPLOADEDFILE, docsNewMenu.get());
  }

  @Override
  protected void createPostSessionInitActions() {
  }
}
