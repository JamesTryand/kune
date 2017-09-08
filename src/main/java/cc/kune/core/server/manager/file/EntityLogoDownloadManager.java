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
package cc.kune.core.server.manager.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.typesafe.config.Config;

import cc.kune.core.server.access.FinderService;
import cc.kune.core.server.manager.GroupManager;
import cc.kune.core.shared.FileConstants;
import cc.kune.core.shared.domain.utils.StateToken;
import cc.kune.domain.Content;
import cc.kune.domain.Group;
import cc.kune.initials.InitialsAvatarsServerUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class EntityLogoDownloadManager.
 *
 * @author vjrj@ourproject.org (Vicente J. Ruiz Jurado)
 */
public class EntityLogoDownloadManager extends HttpServlet {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -1958945058088446881L;

  private final byte[] clearLogo;

  private final String clearMime;

  private final FinderService contentFinder;

  /** The current last modified. */
  private long currentLastModified;

  /** The default last modified. */
  final private long defaultLastModified;

  private final String domain;

  /** The group logo. */
  private final byte[] groupLogo;

  /** The group manager. */
  GroupManager groupManager;

  /** The group mime. */
  private final String groupMime;

  /** The unknown logo. */
  private final byte[] unknownLogo;

  /** The unknown mime. */
  private final String unknownMime;

  /**
   * Instantiates a new entity logo download manager.
   *
   * @param resourceBases
   *          the resource bases
   * @param groupManager
   *          the group manager
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  @Inject
  public EntityLogoDownloadManager(Config config, final GroupManager groupManager, final FinderService contentFinder) throws IOException {
    List<String> resourceBases = config.getStringList("core.resource_bases");
    String domain = config.getString("core.wave_server_domain");
    this.groupManager = groupManager;
    this.domain = domain;
    this.contentFinder = contentFinder;

    final File groupFile = getFile(resourceBases, FileConstants.GROUP_NO_AVATAR_IMAGE);
    groupMime = getMime(groupFile);
    groupLogo = getBy(groupFile);

    final File unknownFile = getFile(resourceBases, FileConstants.NO_RESULT_AVATAR_IMAGE);
    unknownMime = getMime(unknownFile);
    unknownLogo = getBy(unknownFile);

    final File clearFile = getFile(resourceBases, FileConstants.TRANSPARENT_PIXEL);
    clearMime = getMime(clearFile);
    clearLogo = getBy(clearFile);

    defaultLastModified = 0l;
  }

  private void clearResult(final HttpServletResponse resp) throws IOException {
    reply(resp, clearLogo, clearMime);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
   * javax.servlet.http.HttpServletResponse)
   */
  @Override
  protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    currentLastModified = defaultLastModified;

    String parameter = req.getParameter(FileConstants.WAVE_URI);

    if (parameter != null) {
      // Is a waveref request for a group logo of content
      try {
        final String waveRef = parameter;
        // FIXME get this from a wave constant
        final Content content = contentFinder.getContainerByWaveRef(waveRef);
        final Group group = content.getOwner();
        replyWithGroupLogo(resp, group);
        return;
      } catch (final javax.persistence.NoResultException e) {
        // Return transparent pixel
        clearResult(resp);
        return;
      }
    }

    parameter = req.getParameter(FileConstants.TOKEN);
    final StateToken stateToken = new StateToken(parameter);
    final String onlyUserS = req.getParameter(FileConstants.ONLY_USERS);
    final boolean onlyUsers = Boolean.parseBoolean(onlyUserS);
    Group group = Group.NO_GROUP;
    try {
      group = groupManager.findByShortName(stateToken.getGroup());
      if (group == Group.NO_GROUP) {
        unknownResult(resp);
        return;
      }
      if (onlyUsers && !group.isPersonal()) {
        unknownResult(resp);
        return;
      }
      replyWithGroupLogo(resp, group);
    } catch (final NoResultException e) {
      unknownResult(resp);
    }
  }

  /**
   * Gets the by.
   *
   * @param file
   *          the file
   * @return the by
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  private byte[] getBy(final File file) throws IOException {
    final BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    org.apache.commons.io.IOUtils.copy(is, baos);
    return baos.toByteArray();
  }

  /**
   * Gets the file.
   *
   * @param resourceBases
   *          the resource bases
   * @param location
   *          the location
   * @return the file
   */
  private File getFile(final List<String> resourceBases, final String location) {
    final File file = FileDownloadManagerUtils.searchFileInResourceBases(resourceBases, location);
    return file;
  }

  /*
   * (non-Javadoc)
   *
   * @see javax.servlet.http.HttpServlet#getLastModified(javax.servlet.http.
   * HttpServletRequest)
   */
  @Override
  protected long getLastModified(final HttpServletRequest req) {
    // http://oreilly.com/catalog/jservlet/chapter/ch03.html#14260
    // (...)to play it safe, getLastModified() should always round down to the
    // nearest thousand milliseconds.
    return currentLastModified / 1000 * 1000;
  }

  /**
   * Gets the mime.
   *
   * @param file
   *          the file
   * @return the mime
   */
  private String getMime(final File file) {
    return new MimetypesFileTypeMap().getContentType(file);
  }

  /**
   * Reply.
   *
   * @param resp
   *          the resp
   * @param logo
   *          the logo
   * @param mime
   *          the mime
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  private void reply(final HttpServletResponse resp, final byte[] logo, final String mime)
      throws IOException {
    resp.setContentLength(logo.length);
    resp.setContentType(mime);
    CacheUtils.setCache1Day(resp);
    resp.getOutputStream().write(logo);
  }

  private void replyWithGroupLogo(final HttpServletResponse resp, final Group group) throws IOException {
    if (!group.hasLogo()) {
      if (group.isPersonal()) {
        InitialsAvatarsServerUtils.doInitialsResponse(resp, 100, 100, group.getShortName() + "@"
            + domain);
      } else {
        reply(resp, groupLogo, groupMime);
      }
    } else {
      // Has logo
      currentLastModified = group.getLogoLastModifiedTime();
      reply(resp, group.getLogo(), group.getLogoMime().toString());
    }
  }

  /**
   * Unknown result.
   *
   * @param resp
   *          the resp
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  private void unknownResult(final HttpServletResponse resp) throws IOException {
    reply(resp, unknownLogo, unknownMime);
  }
}
