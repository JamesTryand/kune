/*
 *
 * Copyright (C) 2007-2015 Licensed to the Comunes Association (CA) under
 * one or more contributor license agreements (see COPYRIGHT for details).
 * The CA licenses this file to you under the GNU Affero General Public
 * License version 3, (the "License"); you may not use this file except in
 * compliance with the License. This file is part of kune.
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
package cc.kune.events.client.actions;

import static cc.kune.events.shared.EventsToolConstants.*;
import static cc.kune.gspace.client.actions.ActionGroups.*;
import cc.kune.core.client.actions.ActionRegistryByType;
import cc.kune.core.client.resources.CoreResources;
import cc.kune.core.client.state.Session;
import cc.kune.gspace.client.actions.AbstractFoldableToolActions;
import cc.kune.gspace.client.actions.ContentViewerOptionsMenu;
import cc.kune.gspace.client.actions.CopyContentMenuItem;
import cc.kune.gspace.client.actions.MoveContentMenuItem;
import cc.kune.gspace.client.actions.ParticipateInContentBtn;
import cc.kune.gspace.client.actions.PurgeContainerBtn;
import cc.kune.gspace.client.actions.PurgeContainerMenuItem;
import cc.kune.gspace.client.actions.PurgeContentBtn;
import cc.kune.gspace.client.actions.PurgeContentMenuItem;
import cc.kune.gspace.client.actions.RefreshContentMenuItem;
import cc.kune.gspace.client.actions.TutorialBtn;
import cc.kune.gspace.client.actions.WriteToParticipantsMenuItem;
import cc.kune.gspace.client.actions.share.AddAdminMembersToContentMenuItem;
import cc.kune.gspace.client.actions.share.AddAllMembersToContentMenuItem;
import cc.kune.gspace.client.actions.share.AddCollabMembersToContentMenuItem;
import cc.kune.gspace.client.actions.share.ShareDialogMenuItem;
import cc.kune.gspace.client.actions.share.ShareInHelper;
import cc.kune.gspace.client.actions.share.ShareMenu;
import cc.kune.trash.shared.TrashToolConstants;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class EventsClientActions extends AbstractFoldableToolActions {
  final String[] all = { TYPE_ROOT, TYPE_MEETING };
  final String[] containers = { TYPE_ROOT };
  final String[] containersNoRoot = {};
  final String[] contents = { TYPE_MEETING };

  @SuppressWarnings("unchecked")
  @Inject
  public EventsClientActions(final Session session,

  final ActionRegistryByType registry, final CoreResources res, final Provider<NewEventBtn> newEventBtn,
      final Provider<GoParentFolderBtn> folderGoUp,
      final Provider<OpenMeetingMenuItem> openContentMenuItem,
      final Provider<DelMeetingMenuItem> delContentMenuItem,
      final Provider<ContentViewerOptionsMenu> optionsMenuContent,
      final Provider<ShareMenu> shareMenuContent,
      final Provider<AddAllMembersToContentMenuItem> addAllMenuItem,
      final Provider<AddAdminMembersToContentMenuItem> addAdminMembersMenuItem,
      final Provider<AddCollabMembersToContentMenuItem> addCollabMembersMenuItem,
      final Provider<ParticipateInContentBtn> participateBtn,
      final Provider<Calendar1DayViewSelectBtn> cal1DayBtn,
      final Provider<Calendar3DaysViewSelectBtn> cal3DaysBtn,
      final Provider<Calendar7DaysViewSelectBtn> cal7DaysBtn,
      final Provider<CalendarMonthViewSelectBtn> calMonthBtn,
      final Provider<EventAddMenuItem> eventAddMenuItem,
      final Provider<EventOpenMenuItem> eventOpenMenuItem, final Provider<CalendarGoPrevBtn> calPrevBtn,
      final Provider<EventRemoveMenuItem> eventRemoveMenuItem,
      final Provider<CopyContentMenuItem> copyContent, final Provider<TutorialBtn> tutorialBtn,
      final Provider<WriteToParticipantsMenuItem> writeToParticipants,
      final Provider<PurgeContentMenuItem> purgeMenuItem, final Provider<PurgeContentBtn> purgeBtn,
      final Provider<PurgeContainerMenuItem> purgeFolderMenuItem,
      final Provider<MoveContentMenuItem> moveContentMenuItem,
      final Provider<PurgeContainerBtn> purgeFolderBtn, final Provider<ExportCalendarMenuItem> export,
      final Provider<CalendarGoNextBtn> calNextBtn, final CalendarOnOverMenu onOverMenu,
      final Provider<CalendarGoTodayBtn> goToday, final Provider<RefreshContentMenuItem> refresh,
      final Provider<ShareDialogMenuItem> shareSettings, final ShareInHelper shareIHelper) {
    super(TOOL_NAME, session, registry);
    add(DOC_TOP_TOOLBAR, containers, newEventBtn);
    add(DOC_TOP_TOOLBAR, containers, goToday, calPrevBtn, cal1DayBtn, cal3DaysBtn, cal7DaysBtn,
        calMonthBtn, calNextBtn);
    add(DOC_TOP_TOOLBAR, all, tutorialBtn);
    add(DOC_HEADER_BAR, contents, participateBtn);
    add(DOC_HEADER_BAR, all, shareMenuContent);
    add(DOC_HEADER_BAR, contents, addAllMenuItem, addAdminMembersMenuItem, addCollabMembersMenuItem);
    add(DOC_HEADER_BAR, contents, shareIHelper.getShareInWaves());
    add(DOC_HEADER_BAR, all, shareIHelper.getShareInAll());
    add(DOC_HEADER_BAR, contents, shareSettings);
    add(DOC_HEADER_BAR, all, optionsMenuContent, refresh, export);
    add(DOC_HEADER_BAR, contents, copyContent, writeToParticipants);
    // On over calendar menu
    add(GROUP_HEADER_BOTTOM_BAR, contents, folderGoUp);
    add(DOC_TOP_TOOLBAR, containers, onOverMenu);
    add(TrashToolConstants.TOOL_NAME, DOC_TOP_TOOLBAR, contents, purgeBtn);
    add(TrashToolConstants.TOOL_NAME, DOC_TOP_TOOLBAR, containersNoRoot, purgeFolderBtn);
    add(TrashToolConstants.TOOL_NAME, ITEM_MENU, contents, purgeMenuItem, moveContentMenuItem);
    add(TrashToolConstants.TOOL_NAME, ITEM_MENU, containersNoRoot, purgeFolderMenuItem,
        moveContentMenuItem);
    eventOpenMenuItem.get();
    eventAddMenuItem.get();
    eventRemoveMenuItem.get();

    // For now, commented:
    // add(NAME, ITEM_MENU,
    // openContentMenuItem,
    // contents);
    // add(NAME, ITEM_MENU,
    // openContentMenuItem,
    // containersNoRoot);
    // add(NAME, ITEM_MENU,
    // delContentMenuItem,
    // contents);

  }

  @Override
  protected void createPostSessionInitActions() {
  }
}
