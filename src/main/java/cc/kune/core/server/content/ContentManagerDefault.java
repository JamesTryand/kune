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
package cc.kune.core.server.content;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.waveprotocol.wave.model.waveref.WaveRef;
import org.waveprotocol.wave.util.escapers.jvm.JavaWaverefEncoder;

import cc.kune.core.client.actions.xml.XMLKuneClientActions;
import cc.kune.core.client.actions.xml.XMLWaveExtension;
import cc.kune.core.client.errors.DefaultException;
import cc.kune.core.client.errors.I18nNotFoundException;
import cc.kune.core.client.errors.MoveOnSameContainerException;
import cc.kune.core.client.errors.NameInUseException;
import cc.kune.core.client.errors.UserNotFoundException;
import cc.kune.core.server.access.FinderService;
import cc.kune.core.server.manager.SearchResult;
import cc.kune.core.server.manager.TagUserContentManager;
import cc.kune.core.server.manager.file.FileUtils;
import cc.kune.core.server.manager.impl.DefaultManager;
import cc.kune.core.server.manager.impl.ServerManagerException;
import cc.kune.core.server.tool.ServerTool;
import cc.kune.core.server.tool.ServerToolRegistry;
import cc.kune.core.server.utils.FilenameUtils;
import cc.kune.core.shared.domain.ContentStatus;
import cc.kune.core.shared.domain.RateResult;
import cc.kune.domain.Container;
import cc.kune.domain.Content;
import cc.kune.domain.I18nLanguage;
import cc.kune.domain.Rate;
import cc.kune.domain.Revision;
import cc.kune.domain.User;
import cc.kune.domain.finders.ContainerFinder;
import cc.kune.domain.finders.ContentFinder;
import cc.kune.domain.finders.I18nLanguageFinder;
import cc.kune.domain.finders.UserFinder;
import cc.kune.wave.server.KuneWaveServerUtils;
import cc.kune.wave.server.ParticipantUtils;
import cc.kune.wave.server.kspecific.KuneWaveService;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class ContentManagerDefault extends DefaultManager<Content, Long> implements ContentManager {

  private final XMLKuneClientActions actions;
  private final ContainerFinder containerFinder;
  private final ContentFinder contentFinder;
  private final FinderService finder;
  private final KuneWaveService kuneWaveManager;
  private final I18nLanguageFinder languageFinder;
  private final Log LOG = LogFactory.getLog(ContentManagerDefault.class);
  private final ParticipantUtils participantUtils;
  private final TagUserContentManager tagManager;
  private final ServerToolRegistry tools;
  private final UserFinder userFinder;

  @Inject
  public ContentManagerDefault(final ContentFinder contentFinder, final ContainerFinder containerFinder,
      final Provider<EntityManager> provider, final FinderService finder, final UserFinder userFinder,
      final I18nLanguageFinder languageFinder, final TagUserContentManager tagManager,
      final KuneWaveService kuneWaveManager, final ParticipantUtils participantUtils,
      final ServerToolRegistry tools, final XMLActionReader xmlActionReader) {
    super(provider, Content.class);
    this.contentFinder = contentFinder;
    this.containerFinder = containerFinder;
    this.finder = finder;
    this.userFinder = userFinder;
    this.languageFinder = languageFinder;
    this.tagManager = tagManager;
    this.kuneWaveManager = kuneWaveManager;
    this.participantUtils = participantUtils;
    this.tools = tools;
    this.actions = xmlActionReader.getActions();
  }

  @Override
  public void addAuthor(final User user, final Long contentId, final String authorShortName)
      throws DefaultException {
    final Content content = finder.getContent(contentId);
    final User author = userFinder.findByShortName(authorShortName);
    if (author == null) {
      throw new UserNotFoundException();
    }
    content.addAuthor(author);
  }

  @Override
  public void addGadgetToContent(final User user, final Content content, final String gadgetName) {
    final URL gadgetUrl = getGadgetUrl(gadgetName);
    addGadgetToContent(user, content, gadgetUrl);
  }

  public void addGadgetToContent(final User user, final Content content, final URL gadgetUrl) {
    kuneWaveManager.addGadget(KuneWaveServerUtils.getWaveRef(content),
        participantUtils.of(user.getShortName()).toString(), gadgetUrl);
  }

  @Override
  public boolean addParticipant(final User user, final Long contentId, final String participant) {
    final Content content = finder.getContent(contentId);
    if (content.isWave()) {
      return kuneWaveManager.addParticipants(KuneWaveServerUtils.getWaveRef(content),
          getContentAuthor(content), user.getShortName(), participant);
    }
    return false;
  }

  protected Content createContent(final String title, final String body, final User author,
      final Container container, final String typeId) {
    return createContent(title, body, author, container, typeId, KuneWaveService.WITHOUT_GADGET,
        Collections.<String, String> emptyMap());
  }

  protected Content createContent(final String title, final String body, final User author,
      final Container container, final String typeId, final URL gadgetUrl,
      final Map<String, String> gadgetProperties) {
    FilenameUtils.checkBasicFilename(title);
    final String newtitle = findInexistentTitle(container, title);
    final Content newContent = new Content();
    newContent.addAuthor(author);
    newContent.setLanguage(author.getLanguage());
    newContent.setTypeId(typeId);
    container.addContent(newContent);
    newContent.setContainer(container);
    final Revision revision = new Revision(newContent);
    revision.setTitle(newtitle);
    // Duplicate in StateServiceDefault
    if (newContent.isWave()) {
      final String authorName = author.getShortName();
      final WaveRef waveRef = kuneWaveManager.createWave(newtitle, body,
          KuneWaveService.DO_NOTHING_CBACK, gadgetUrl, gadgetProperties, participantUtils.of(authorName));
      newContent.setWaveId(JavaWaverefEncoder.encodeToUriPathSegment(waveRef));
      newContent.setModifiedOn((new Date()).getTime());
    }
    revision.setBody(body);
    newContent.addRevision(revision);
    return save(newContent);
  }

  @Override
  public Content createGadget(final User user, final Container container, final String gadgetname,
      final String typeIdChild, final String title, final String body,
      final Map<String, String> gadgetProperties) {
    final String toolName = container.getToolName();
    final ServerTool tool = tools.get(toolName);
    tool.checkTypesBeforeContentCreation(container.getTypeId(), typeIdChild);
    final Content content = createContent(title, body, user, container, typeIdChild,
        getGadgetUrl(gadgetname), gadgetProperties);
    tool.onCreateContent(content, container);
    return content;
  }

  private MultiFieldQueryParser createMultiFieldParser() {
    final MultiFieldQueryParser parser = new MultiFieldQueryParser(DEF_GLOBAL_SEARCH_FIELDS,
        new StandardAnalyzer());
    return parser;
  }

  @Override
  public boolean findIfExistsTitle(final Container container, final String title) {
    return (contentFinder.findIfExistsTitle(container, title) > 0)
        || (containerFinder.findIfExistsTitle(container, title) > 0);
  }

  private String findInexistentTitle(final Container container, final String title) {
    String initialTitle = String.valueOf(title);
    while (findIfExistsTitle(container, initialTitle)) {
      initialTitle = FileUtils.getNextSequentialFileName(initialTitle);
    }
    return initialTitle;
  }

  private String getContentAuthor(final Content content) {
    return content.getAuthors().get(0).getShortName();
  }

  private URL getGadgetUrl(final String gadgetname) {
    URL gadgetUrl = null;
    final XMLWaveExtension extension = actions.getExtensions().get(gadgetname);
    assert extension != null;
    final String urlS = extension.getGadgetUrl();
    try {
      gadgetUrl = new URL(urlS);
    } catch (final MalformedURLException e) {
      LOG.error("Parsing gadget URL: " + urlS, e);
    }
    return gadgetUrl;
  }

  @Override
  public Double getRateAvg(final Content content) {
    return finder.getRateAvg(content);
  }

  @Override
  public Long getRateByUsers(final Content content) {
    return finder.getRateByUsers(content);
  }

  @Override
  public Double getRateContent(final User rater, final Content content) {
    final Rate rate = finder.getRate(rater, content);
    if (rate != null) {
      return rate.getValue();
    } else {
      return null;
    }
  }

  @Override
  public Content moveContent(final Content content, final Container newContainer) {
    if (newContainer.equals(content.getContainer())) {
      throw new MoveOnSameContainerException();
    }
    final String title = content.getTitle();
    if (findIfExistsTitle(newContainer, title)) {
      throw new NameInUseException();
    }
    final Container oldContainer = content.getContainer();
    oldContainer.removeContent(content);
    newContainer.addContent(content);
    content.setContainer(newContainer);
    return persist(content);
  }

  @Override
  public RateResult rateContent(final User rater, final Long contentId, final Double value)
      throws DefaultException {
    final Content content = finder.getContent(contentId);
    final Rate oldRate = finder.getRate(rater, content);
    if (oldRate == null) {
      final Rate rate = new Rate(rater, content, value);
      super.persist(rate, Rate.class);
    } else {
      oldRate.setValue(value);
      super.persist(oldRate, Rate.class);
    }
    final Double rateAvg = getRateAvg(content);
    final Long rateByUsers = getRateByUsers(content);
    return new RateResult(rateAvg != null ? rateAvg : 0D, rateByUsers != null ? rateByUsers.intValue()
        : 0, value);
  }

  @Override
  public void removeAuthor(final User user, final Long contentId, final String authorShortName)
      throws DefaultException {
    final Content content = finder.getContent(contentId);
    final User author = userFinder.findByShortName(authorShortName);
    if (author == null) {
      throw new UserNotFoundException();
    }
    content.removeAuthor(author);
  }

  @Override
  public Content renameContent(final User user, final Long contentId, final String newTitle)
      throws DefaultException {
    final String newTitleWithoutNL = FilenameUtils.chomp(newTitle);
    FilenameUtils.checkBasicFilename(newTitleWithoutNL);
    final Content content = finder.getContent(contentId);
    if (findIfExistsTitle(content.getContainer(), newTitleWithoutNL)) {
      throw new NameInUseException();
    }
    content.getLastRevision().setTitle(newTitleWithoutNL);
    if (content.isWave()) {
      kuneWaveManager.setTitle(KuneWaveServerUtils.getWaveRef(content), newTitle,
          getContentAuthor(content));
    }
    setModifiedTime(content);
    return content;
  }

  @Override
  public Content save(final Content content) {
    setModifiedTime(content);
    return persist(content);
  }

  @Override
  public Content save(final User editor, final Content content, final String body) {
    setModifiedTime(content);
    final Revision revision = new Revision(content);
    revision.setEditor(editor);
    revision.setTitle(content.getTitle());
    revision.setBody(body);
    content.addRevision(revision);
    return persist(content);
  }

  @Override
  public SearchResult<Content> search(final String search) {
    return this.search(search, null, null);
  }

  @Override
  public SearchResult<Content> search(final String search, final Integer firstResult,
      final Integer maxResults) {
    final MultiFieldQueryParser parser = createMultiFieldParser();
    Query query;
    try {
      query = parser.parse(search);
    } catch (final ParseException e) {
      throw new ServerManagerException("Error parsing search");
    }
    return super.search(query, firstResult, maxResults);
  }

  @Override
  public SearchResult<Content> searchMime(final String search, final Integer firstResult,
      final Integer maxResults, final String groupShortName, final String mimetype) {
    final List<Content> list = contentFinder.findMime(groupShortName, "%" + search + "%", mimetype,
        firstResult, maxResults);
    final Long count = contentFinder.findMimeCount(groupShortName, "%" + search + "%", mimetype);
    return new SearchResult<Content>(count.intValue(), list);
  }

  @Override
  public SearchResult<?> searchMime(final String search, final Integer firstResult,
      final Integer maxResults, final String groupShortName, final String mimetype,
      final String mimetype2) {
    final List<Content> list = contentFinder.find2Mime(groupShortName, "%" + search + "%", mimetype,
        mimetype2, firstResult, maxResults);
    final Long count = contentFinder.find2MimeCount(groupShortName, "%" + search + "%", mimetype,
        mimetype2);
    return new SearchResult<Content>(count.intValue(), list);
  }

  @Override
  public void setGadgetProperties(final User user, final Content content, final String gadgetName,
      final Map<String, String> properties) {
    final URL gadgetUrl = getGadgetUrl(gadgetName);
    kuneWaveManager.setGadgetProperty(KuneWaveServerUtils.getWaveRef(content),
        getContentAuthor(content), gadgetUrl, properties);
  }

  @Override
  public I18nLanguage setLanguage(final User user, final Long contentId, final String languageCode)
      throws DefaultException {
    final Content content = finder.getContent(contentId);
    final I18nLanguage language = languageFinder.findByCode(languageCode);
    if (language == null) {
      throw new I18nNotFoundException();
    }
    content.setLanguage(language);
    return language;
  }

  @Override
  public void setModifiedOn(final Content content, final long lastModifiedTime) {
    content.setModifiedOn(lastModifiedTime);
  }

  private void setModifiedTime(final Content content) {
    setModifiedOn(content, System.currentTimeMillis());
  }

  @Override
  public void setPublishedOn(final User user, final Long contentId, final Date publishedOn)
      throws DefaultException {
    final Content content = finder.getContent(contentId);
    content.setPublishedOn(publishedOn);
  }

  @Override
  public Content setStatus(final Long contentId, final ContentStatus status) {
    final Content content = finder.getContent(contentId);
    content.setStatus(status);
    switch (status) {
    case publishedOnline:
      content.setPublishedOn(new Date());
      content.setDeletedOn(null);
      break;
    case inTheDustbin:
      content.setDeletedOn(new Date());
      content.setPublishedOn(null);
      break;
    default:
      break;
    }
    return content;
  }

  @Override
  public void setTags(final User user, final Long contentId, final String tags) throws DefaultException {
    final Content content = finder.getContent(contentId);
    tagManager.setTags(user, content, tags);
  }

}
