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
package cc.kune.events.server;

import static cc.kune.events.shared.EventsConstants.NAME;
import static cc.kune.events.shared.EventsConstants.ROOT_NAME;
import static cc.kune.events.shared.EventsConstants.TYPE_MEETING;
import static cc.kune.events.shared.EventsConstants.TYPE_ROOT;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import cc.kune.core.server.AbstractServerTool;
import cc.kune.core.server.content.ContainerManager;
import cc.kune.core.server.content.ContentManager;
import cc.kune.core.server.manager.ToolConfigurationManager;
import cc.kune.core.server.tool.ServerToolTarget;
import cc.kune.core.server.tool.ServerWaveTool;
import cc.kune.core.server.utils.UrlUtils;
import cc.kune.core.shared.domain.ContentStatus;
import cc.kune.core.shared.domain.GroupListMode;
import cc.kune.core.shared.i18n.I18nTranslationService;
import cc.kune.domain.AccessLists;
import cc.kune.domain.Container;
import cc.kune.domain.Content;
import cc.kune.domain.Group;
import cc.kune.domain.User;

import com.google.inject.Inject;

public class EventsServerTool extends AbstractServerTool implements ServerWaveTool {

  private static final String MEETING_GADGET = "http://mass-mob.appspot.com/massmob/org.ourproject.massmob.client.MassmobGadget.gadget.xml";
  private final URL gadgetUrl;

  @Inject
  public EventsServerTool(final ContentManager contentManager, final ContainerManager containerManager,
      final ToolConfigurationManager configurationManager, final I18nTranslationService i18n) {
    super(NAME, ROOT_NAME, TYPE_ROOT, Arrays.asList(TYPE_MEETING), Arrays.asList(TYPE_ROOT),
        Collections.<String> emptyList(), Arrays.asList(TYPE_ROOT), contentManager, containerManager,
        configurationManager, i18n, ServerToolTarget.forGroups);
    gadgetUrl = UrlUtils.of(MEETING_GADGET);
  }

  @Override
  public URL getGadgetUrl() {
    return gadgetUrl;
  }

  @Override
  public Group initGroup(final User user, final Group group, final Object... otherVars) {
    final Container rootFolder = createRoot(group);

    final Content content = createInitialContent(user, group, rootFolder, i18n.t("Meeting sample"),
        i18n.t("This is only a meet sample. You can invite other participants to this meeting, "
            + "but also publish to the general public allowing you to to help in the organization, "
            + "call and speed-up of events."), TYPE_MEETING, gadgetUrl);
    contentManager.save(content);
    return group;
  }

  @Override
  public void onCreateContainer(final Container container, final Container parent) {
    setContainerAcl(container);
  }

  @Override
  public void onCreateContent(final Content content, final Container parent) {
    content.setStatus(ContentStatus.publishedOnline);
    content.setPublishedOn(new Date());
  }

  @Override
  protected void setContainerAcl(final Container container) {
    final AccessLists meetsAcl = new AccessLists();
    meetsAcl.getAdmins().setMode(GroupListMode.NORMAL);
    meetsAcl.getAdmins().add(container.getOwner());
    meetsAcl.getEditors().setMode(GroupListMode.NORMAL);
    meetsAcl.getViewers().setMode(GroupListMode.EVERYONE);
    containerManager.setAccessList(container, meetsAcl);
  }
}