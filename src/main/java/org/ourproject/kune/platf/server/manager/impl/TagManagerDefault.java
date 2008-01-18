/*
 * Copyright (C) 2007 The kune development team (see CREDITS for details)
 * This file is part of kune.
 *
 * Kune is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kune is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.ourproject.kune.platf.server.manager.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.ourproject.kune.platf.server.domain.Group;
import org.ourproject.kune.platf.server.domain.Tag;
import org.ourproject.kune.platf.server.domain.TagResult;
import org.ourproject.kune.platf.server.manager.TagManager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class TagManagerDefault extends DefaultManager<Tag, Long> implements TagManager {

    private final Tag tagFinder;
    private final Provider<EntityManager> provider;

    @Inject
    public TagManagerDefault(final Provider<EntityManager> provider, final Tag tagFinder) {
        super(provider, Tag.class);
        this.provider = provider;
        this.tagFinder = tagFinder;
    }

    public Tag findByTagName(final String tag) {
        return tagFinder.findByTagName(tag);
    }

    public List<TagResult> getSummaryByGroup(final Group group) {
        Query q = provider.get().createNamedQuery(Tag.TAGSGROUPED);
        q.setParameter("group", group);
        List<TagResult> results = q.getResultList();
        return results;
    }
}