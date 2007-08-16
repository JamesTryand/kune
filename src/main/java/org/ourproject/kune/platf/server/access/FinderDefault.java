package org.ourproject.kune.platf.server.access;

import javax.persistence.PersistenceException;

import org.ourproject.kune.platf.client.dto.StateToken;
import org.ourproject.kune.platf.client.errors.ContentNotFoundException;
import org.ourproject.kune.platf.server.domain.AccessLists;
import org.ourproject.kune.platf.server.domain.Content;
import org.ourproject.kune.platf.server.domain.Folder;
import org.ourproject.kune.platf.server.domain.Group;
import org.ourproject.kune.platf.server.manager.ContentManager;
import org.ourproject.kune.platf.server.manager.FolderManager;
import org.ourproject.kune.platf.server.manager.GroupManager;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class FinderDefault implements Finder {
    private final GroupManager groupManager;
    private final ContentManager contentManager;
    private final FolderManager folderManager;

    @Inject
    public FinderDefault(final GroupManager groupManager, final FolderManager folderManager,
	    final ContentManager contentManager) {
	this.groupManager = groupManager;
	this.folderManager = folderManager;
	this.contentManager = contentManager;
    }

    public Content getContent(final Long contentId) throws ContentNotFoundException {
	try {
	    return contentManager.find(contentId);
	} catch (PersistenceException e) {
	    throw new ContentNotFoundException();
	}
    }

    public Folder getFolder(final Long folderId) throws ContentNotFoundException {
	try {
	    return folderManager.find(folderId);
	} catch (PersistenceException e) {
	    throw new ContentNotFoundException();
	}

    }

    public Content getContent(final Group group, final StateToken token) throws ContentNotFoundException {
	Long contentId = checkAndParse(token.getDocument());
	Long folderId = checkAndParse(token.getFolder());

	if (token.hasAll()) {
	    return findByContentReference(token.getGroup(), token.getTool(), folderId, contentId);
	} else if (token.hasGroupToolAndFolder()) {
	    return findByFolderReference(token.getGroup(), token.getTool(), folderId);
	} else if (token.hasGroupAndTool()) {
	    return findByRootOnGroup(token.getGroup(), token.getTool());
	} else if (token.hasGroup()) {
	    return findDefaultOfGroup(token.getGroup());
	} else if (token.hasNothing()) {
	    return findDefaultOfGroup(group);
	} else {
	    throw new ContentNotFoundException();
	}
    }

    public Long checkAndParse(final String s) throws ContentNotFoundException {
	if (s != null) {
	    try {
		return Long.parseLong(s);
	    } catch (NumberFormatException e) {
		throw new ContentNotFoundException();
	    }
	}
	return null;
    }

    private Content findByContentReference(final String groupName, final String toolName,
	    final Long folderId, final Long contentId) throws ContentNotFoundException {
	Content descriptor = contentManager.find(contentId);
	Folder folder = descriptor.getFolder();

	if (!folder.getId().equals(folderId)) {
	    throw new ContentNotFoundException();
	}
	if (!folder.getToolName().equals(toolName)) {
	    throw new ContentNotFoundException();
	}
	if (!folder.getOwner().getShortName().equals(groupName)) {
	    throw new ContentNotFoundException();
	}
	return descriptor;
    }

    private Content findByFolderReference(final String groupName, final String toolName, final Long folderId) {
	Folder folder = folderManager.find(folderId);
	return generateFolderFakeContent(folder);
    }

    private Content findByRootOnGroup(final String groupName, final String toolName) {
	Group group = groupManager.findByShortName(groupName);
	Folder folder = group.getRoot(toolName);
	return generateFolderFakeContent(folder);
    }

    private Content generateFolderFakeContent(final Folder folder) {
	Content descriptor = new Content();
	descriptor.setFolder(folder);
	AccessLists emptyAccessList = new AccessLists();
	descriptor.setAccessLists(emptyAccessList);
	return descriptor;
    }

    private Content findDefaultOfGroup(final String groupName) {
	Group group = groupManager.findByShortName(groupName);
	return findDefaultOfGroup(group);
    }

    private Content findDefaultOfGroup(final Group group) {
	Content defaultContent = group.getDefaultContent();
	return defaultContent;
    }

}
