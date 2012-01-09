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
package cc.kune.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import cc.kune.core.shared.domain.utils.StateToken;
import cc.kune.domain.utils.HasId;
import cc.kune.domain.utils.HasStateToken;

@Entity
@Indexed
@Table(name = "containers")
public class Container implements HasId, HasStateToken {

  @OneToOne(cascade = CascadeType.ALL)
  private AccessLists accessLists;

  @LazyCollection(LazyCollectionOption.FALSE)
  @Fetch(FetchMode.JOIN)
  @OrderBy("createdOn DESC")
  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<Container> childs;

  @OneToMany(cascade = CascadeType.ALL)
  private List<ContainerTranslation> containerTranslations;

  @LazyCollection(LazyCollectionOption.FALSE)
  @Fetch(FetchMode.JOIN)
  @ContainedIn
  @OrderBy("createdOn DESC")
  @OneToMany(mappedBy = "container", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<Content> contents;

  @Basic(optional = false)
  private Long createdOn;

  @Basic(optional = true)
  private Date deletedOn;

  @Id
  @GeneratedValue
  @DocumentId
  Long id;

  @ManyToOne
  private I18nLanguage language;

  @Column
  @Field(index = Index.TOKENIZED, store = Store.NO)
  private String name;

  @IndexedEmbedded(depth = 1, prefix = "owner_")
  @OneToOne(cascade = CascadeType.ALL)
  private Group owner;

  // Parent/Child pattern:
  // http://www.hibernate.org/hib_docs/reference/en/html/example-parentchild.html
  // http://www.researchkitchen.co.uk/blog/archives/57
  @LazyCollection(LazyCollectionOption.FALSE)
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn
  private Container parent;

  private String toolName;

  private String typeId;

  public Container() {
    this(null, null, null);
  }

  public Container(final String title, final Group group, final String toolName) {
    this.name = title;
    owner = group;
    this.toolName = toolName;
    this.contents = new HashSet<Content>();
    this.childs = new HashSet<Container>();
    this.createdOn = System.currentTimeMillis();
  }

  public void addChild(final Container child) {
    child.setParent(this);
    childs.add(child);
  }

  public void addContent(final Content descriptor) {
    // FIXME: something related with lazy initialization (workaround using
    // size())
    contents.size();
    contents.add(descriptor);
  }

  @Transient
  public List<Container> getAbsolutePath() {
    ArrayList<Container> path;
    if (isRoot()) {
      path = new ArrayList<Container>();
    } else {
      path = new ArrayList<Container>((getParent().getAbsolutePath()));
    }
    path.add(this);
    return path;
  }

  @Transient
  public AccessLists getAccessLists() {
    return hasAccessList() ? accessLists : isRoot() ? getOwner().getAccessLists()
        : getParent().getAccessLists();
  }

  public List<ContainerTranslation> getAliases() {
    return containerTranslations;
  }

  public Set<Container> getChilds() {
    return childs;
  }

  public List<ContainerTranslation> getContainerTranslations() {
    return containerTranslations;
  }

  public Set<Content> getContents() {
    return contents;
  }

  public Long getCreatedOn() {
    return createdOn;
  }

  public Date getDeletedOn() {
    return deletedOn;
  }

  @Override
  public Long getId() {
    return id;
  }

  public I18nLanguage getLanguage() {
    return language;
  }

  public String getName() {
    return name;
  }

  public Group getOwner() {
    return owner;
  }

  public Container getParent() {
    return parent;
  }

  public Long getParentFolderId() {
    return parent != null ? parent.getId() : null;
  }

  @Override
  @Transient
  public StateToken getStateToken() {
    return new StateToken(getOwner().getShortName(), getToolName(), getId());
  }

  @Transient
  public String getStateTokenEncoded() {
    return getStateToken().getEncoded();
  }

  public String getToolName() {
    return toolName;
  }

  public String getTypeId() {
    return typeId;
  }

  @Transient
  public boolean hasAccessList() {
    return accessLists != null;
  }

  @Transient
  public boolean isLeaf() {
    return childs.size() == 0 && contents.size() == 0;
  }

  @Transient
  public boolean isRoot() {
    return parent == null;
  }

  public void removeChild(final Container child) {
    child.setParent(null);
    childs.remove(child);
  }

  public void removeContent(final Content content) {
    contents.size();
    contents.remove(content);
  }

  public void setAccessLists(final AccessLists accessLists) {
    this.accessLists = accessLists;
  }

  public void setAliases(final List<ContainerTranslation> containerTranslations) {
    this.containerTranslations = containerTranslations;
  }

  public void setChilds(final Set<Container> childs) {
    this.childs = childs;
  }

  public void setContainerTranslations(final List<ContainerTranslation> containerTranslations) {
    this.containerTranslations = containerTranslations;
  }

  public void setContents(final HashSet<Content> contents) {
    this.contents = contents;
  }

  public void setCreatedOn(final Long createdOn) {
    this.createdOn = createdOn;
  }

  public void setDeletedOn(final Date deletedOn) {
    this.deletedOn = deletedOn;
  }

  @Override
  public void setId(final Long id) {
    this.id = id;
  }

  public void setLanguage(final I18nLanguage language) {
    this.language = language;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void setOwner(final Group owner) {
    this.owner = owner;
  }

  public void setParent(final Container parent) {
    this.parent = parent;
  }

  public void setToolName(final String toolName) {
    this.toolName = toolName;
  }

  public void setTypeId(final String typeId) {
    this.typeId = typeId;
  }

  @Override
  public String toString() {
    return "Container[(" + getStateTokenEncoded() + "): " + getName() + "]";
  }
}
