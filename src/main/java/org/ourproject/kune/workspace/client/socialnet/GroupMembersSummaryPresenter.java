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
package org.ourproject.kune.workspace.client.socialnet;

import java.util.List;

import org.ourproject.kune.chat.client.ChatEngine;
import org.ourproject.kune.platf.client.actions.ActionToolbarMenuDescriptor;
import org.ourproject.kune.platf.client.actions.ActionToolbarMenuRadioDescriptor;
import org.ourproject.kune.platf.client.actions.ActionToolbarPosition;
import org.ourproject.kune.platf.client.actions.RadioMustBeChecked;
import org.ourproject.kune.platf.client.dto.AccessListsDTO;
import org.ourproject.kune.platf.client.dto.AccessRightsDTO;
import org.ourproject.kune.platf.client.dto.AccessRolDTO;
import org.ourproject.kune.platf.client.dto.AdmissionTypeDTO;
import org.ourproject.kune.platf.client.dto.GroupDTO;
import org.ourproject.kune.platf.client.dto.InitDataDTO;
import org.ourproject.kune.platf.client.dto.LinkDTO;
import org.ourproject.kune.platf.client.dto.SocialNetworkDTO;
import org.ourproject.kune.platf.client.dto.SocialNetworkDataDTO;
import org.ourproject.kune.platf.client.dto.SocialNetworkVisibilityDTO;
import org.ourproject.kune.platf.client.dto.StateAbstractDTO;
import org.ourproject.kune.platf.client.dto.StateToken;
import org.ourproject.kune.platf.client.i18n.I18nUITranslationService;
import org.ourproject.kune.platf.client.rpc.AsyncCallbackSimple;
import org.ourproject.kune.platf.client.rpc.GroupServiceAsync;
import org.ourproject.kune.platf.client.rpc.SocialNetworkServiceAsync;
import org.ourproject.kune.platf.client.services.ImageDescriptor;
import org.ourproject.kune.platf.client.services.ImageUtils;
import org.ourproject.kune.platf.client.state.Session;
import org.ourproject.kune.platf.client.state.StateManager;
import org.ourproject.kune.platf.client.ui.MenuItem;
import org.ourproject.kune.platf.client.ui.download.FileDownloadUtils;
import org.ourproject.kune.platf.client.ui.gridmenu.GridGroup;
import org.ourproject.kune.platf.client.ui.noti.NotifyUser;
import org.ourproject.kune.workspace.client.search.GroupLiveSearcher;
import org.ourproject.kune.workspace.client.socialnet.toolbar.ActionGroupSummaryToolbar;
import org.ourproject.kune.workspace.client.themes.WsTheme;
import org.ourproject.kune.workspace.client.themes.WsThemePresenter;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener2;
import com.calclab.suco.client.ioc.Provider;

public class GroupMembersSummaryPresenter extends SocialNetworkPresenter implements GroupMembersSummary {

    public static final String MEMBERS_VISIBILITY_GROUP = "k-gmsp-memb-visib";
    public static final String NEW_MEMBERS_POLICY_GROUP = "k-gmsp-new-memb-pol";

    private GroupMembersSummaryView view;
    private final I18nUITranslationService i18n;
    private final GridGroup adminCategory;
    private final GridGroup collabCategory;
    private final GridGroup pendigCategory;
    private final Session session;
    private final Provider<SocialNetworkServiceAsync> snServiceProvider;
    private final StateManager stateManager;
    private final GroupActionRegistry groupActionRegistry;
    private final Provider<GroupLiveSearcher> liveSearcherProvider;
    private final Provider<GroupServiceAsync> groupServiceProvider;

    public GroupMembersSummaryPresenter(final I18nUITranslationService i18n, final StateManager stateManager,
            final ImageUtils imageUtils, final Session session,
            final Provider<SocialNetworkServiceAsync> snServiceProvider,
            final Provider<GroupServiceAsync> groupServiceProvider,
            final Provider<GroupLiveSearcher> liveSearcherProvider, final WsThemePresenter wsThemePresenter,
            final Provider<ChatEngine> chatEngineProvider, final GroupActionRegistry groupActionRegistry,
            final ActionGroupSummaryToolbar toolbar, final Provider<FileDownloadUtils> downloadProvider) {
        super(i18n, stateManager, session, snServiceProvider, groupActionRegistry, downloadProvider);
        this.i18n = i18n;
        this.stateManager = stateManager;
        this.session = session;
        this.snServiceProvider = snServiceProvider;
        this.groupServiceProvider = groupServiceProvider;
        this.liveSearcherProvider = liveSearcherProvider;
        this.groupActionRegistry = groupActionRegistry;
        final Listener<StateAbstractDTO> setStateListener = new Listener<StateAbstractDTO>() {
            public void onEvent(StateAbstractDTO state) {
                setState(state);
                toolbar.disableMenusAndClearButtons();
                toolbar.addActions(groupActionRegistry.getCurrentActions(state.getGroup().getStateToken(),
                        session.isLogged(), state.getGroupRights(), true));
                toolbar.attach();
            }
        };
        stateManager.onStateChanged(setStateListener);
        stateManager.onSocialNetworkChanged(setStateListener);
        wsThemePresenter.onThemeChanged(new Listener2<WsTheme, WsTheme>() {
            public void onEvent(final WsTheme oldTheme, final WsTheme newTheme) {
                view.setTheme(oldTheme, newTheme);
            }
        });
        session.onInitDataReceived(new Listener<InitDataDTO>() {
            public void onEvent(final InitDataDTO initData) {
                addUserOperation(new MenuItem<GroupDTO>("images/new-chat.gif", i18n.t("Start a chat with this member"),
                        new Listener<GroupDTO>() {
                            public void onEvent(final GroupDTO group) {
                                chatEngineProvider.get().show();
                                if (chatEngineProvider.get().isLoggedIn()) {
                                    chatEngineProvider.get().chat(
                                            XmppURI.jid(group.getShortName() + "@" + initData.getChatDomain()));
                                } else {
                                    NotifyUser.important(i18n.t("To start a chat you need to be 'online'"));
                                }
                            }
                        }), true);
            }
        });
        final String adminsTitle = i18n.t("Admins");
        final String collabsTitle = i18n.t("Collaborators");
        final String pendingTitle = i18n.t("Pending");
        adminCategory = new GridGroup(adminsTitle, adminsTitle, i18n.t("People that can admin this group"), true);
        collabCategory = new GridGroup(collabsTitle, collabsTitle,
                i18n.t("Other people that collaborate with this group"), true);
        pendigCategory = new GridGroup(pendingTitle, pendingTitle,
                i18n.t("People pending to be accepted in this group by the admins"),
                imageUtils.getImageHtml(ImageDescriptor.alert), true);
        super.addGroupOperation(gotoGroupMenuItem, false);
        super.addUserOperation(gotoMemberMenuItem, false);
        createActions();
    }

    public void addCollab(final String groupShortName) {
        NotifyUser.showProgressProcessing();
        snServiceProvider.get().addCollabMember(session.getUserHash(), session.getCurrentState().getStateToken(),
                groupShortName, new AsyncCallbackSimple<SocialNetworkDataDTO>() {
                    public void onSuccess(final SocialNetworkDataDTO result) {
                        NotifyUser.hideProgress();
                        NotifyUser.info(i18n.t("Member added as collaborator"));
                        stateManager.setSocialNetwork(result);
                    }

                });
    }

    public void init(final GroupMembersSummaryView view) {
        this.view = view;
    }

    private void createActions() {
        ActionToolbarMenuDescriptor<StateToken> addMember = new ActionToolbarMenuDescriptor<StateToken>(
                AccessRolDTO.Administrator, ActionToolbarPosition.bottombar, new Listener<StateToken>() {
                    public void onEvent(StateToken parameter) {
                        liveSearcherProvider.get().onSelection(new Listener<LinkDTO>() {
                            public void onEvent(final LinkDTO link) {
                                view.confirmAddCollab(link.getShortName(), link.getLongName());
                            }
                        });
                        liveSearcherProvider.get().show();
                    }
                });
        addMember.setIconUrl("images/add-green.gif");
        addMember.setTextDescription(i18n.t("Add member"));
        addMember.setToolTip(i18n.t("Add an user or a group as member of this group"));
        addMember.setParentMenuTitle(i18n.t("Options"));

        groupActionRegistry.addAction(addMember);
        groupActionRegistry.addAction(unJoin);

        groupActionRegistry.addAction(participate);
        createSetMembersVisibilityAction(i18n.t("anyone"), SocialNetworkVisibilityDTO.anyone);
        createSetMembersVisibilityAction(i18n.t("only members"), SocialNetworkVisibilityDTO.onlymembers);
        createSetMembersVisibilityAction(i18n.t("only admins"), SocialNetworkVisibilityDTO.onlyadmins);
        createNewMembersPolicyAction(i18n.t("moderate request to join"), AdmissionTypeDTO.Moderated);
        createNewMembersPolicyAction(i18n.t("auto accept request to join"), AdmissionTypeDTO.Open);
    }

    private void createNewMembersPolicyAction(String textDescription, final AdmissionTypeDTO admissionPolicy) {
        ActionToolbarMenuRadioDescriptor<StateToken> newMembersPolicy = new ActionToolbarMenuRadioDescriptor<StateToken>(
                AccessRolDTO.Administrator, ActionToolbarPosition.bottombar, new Listener<StateToken>() {
                    public void onEvent(StateToken parameter) {
                        groupServiceProvider.get().setGroupNewMembersJoiningPolicy(session.getUserHash(),
                                session.getCurrentState().getGroup().getStateToken(), admissionPolicy,
                                new AsyncCallbackSimple<Object>() {
                                    public void onSuccess(Object result) {
                                        NotifyUser.info(i18n.t("Members joining policy changed"));
                                    }
                                });
                    }
                }, NEW_MEMBERS_POLICY_GROUP, new RadioMustBeChecked() {
                    public boolean mustBeChecked() {
                        StateAbstractDTO currentState = session.getCurrentState();
                        return currentState.getGroup().getAdmissionType().equals(admissionPolicy);
                    }
                });
        newMembersPolicy.setTextDescription(textDescription);
        newMembersPolicy.setParentMenuTitle(i18n.t("Options"));
        newMembersPolicy.setParentSubMenuTitle(i18n.t("New members policy"));
        groupActionRegistry.addAction(newMembersPolicy);
    }

    private void createSetMembersVisibilityAction(String textDescription, final SocialNetworkVisibilityDTO visibility) {
        ActionToolbarMenuRadioDescriptor<StateToken> showMembers = new ActionToolbarMenuRadioDescriptor<StateToken>(
                AccessRolDTO.Administrator, ActionToolbarPosition.bottombar, new Listener<StateToken>() {
                    public void onEvent(StateToken parameter) {
                        groupServiceProvider.get().setSocialNetworkVisibility(session.getUserHash(),
                                session.getCurrentState().getGroup().getStateToken(), visibility,
                                new AsyncCallbackSimple<Object>() {
                                    public void onSuccess(Object result) {
                                        NotifyUser.info(i18n.t("Members visibility changed"));
                                    }
                                });
                    }
                }, MEMBERS_VISIBILITY_GROUP, new RadioMustBeChecked() {
                    public boolean mustBeChecked() {
                        StateAbstractDTO currentState = session.getCurrentState();
                        if (!currentState.getGroup().isPersonal()) {
                            SocialNetworkDataDTO socialNetworkData = currentState.getSocialNetworkData();
                            return socialNetworkData.getSocialNetworkVisibility().equals(visibility);
                        }
                        return false;
                    }
                });
        showMembers.setTextDescription(textDescription);
        showMembers.setParentMenuTitle(i18n.t("Options"));
        showMembers.setParentSubMenuTitle(i18n.t("Those who can view this member list"));
        groupActionRegistry.addAction(showMembers);
    }

    @SuppressWarnings("unchecked")
    private void setGroupMembers(final SocialNetworkDTO socialNetwork, final AccessRightsDTO rights) {
        final AccessListsDTO accessLists = socialNetwork.getAccessLists();

        final List<GroupDTO> adminsList = accessLists.getAdmins().getList();
        final List<GroupDTO> collabList = accessLists.getEditors().getList();
        final List<GroupDTO> pendingCollabsList = socialNetwork.getPendingCollaborators().getList();

        // final int numAdmins = adminsList.size();

        boolean userIsAdmin = rights.isAdministrable();
        final boolean userCanView = rights.isVisible();

        view.clear();

        view.setDraggable(session.isLogged());

        if (userCanView) {
            for (final GroupDTO admin : adminsList) {
                view.addItem(createGridItem(adminCategory, admin, rights, changeToCollabMenuItem, removeMemberMenuItem));
            }
            for (final GroupDTO collab : collabList) {
                view.addItem(createGridItem(collabCategory, collab, rights, changeToAdminMenuItem, removeMemberMenuItem));
            }
            if (userIsAdmin) {
                for (final GroupDTO pendingCollab : pendingCollabsList) {
                    view.addItem(createGridItem(pendigCategory, pendingCollab, rights, acceptJoinGroupMenuItem,
                            denyJoinGroupMenuItem));
                }
            }
        }
        view.setVisible(true);
    }

    private void setState(final StateAbstractDTO state) {
        if (state.getGroup().isPersonal()) {
            view.setVisible(false);
        } else {
            if (state.getSocialNetworkData().isMembersVisible()) {
                setGroupMembers(state.getGroupMembers(), state.getGroupRights());
            } else {
                view.clear();
                view.showMembersNotVisible();
                view.setVisible(true);
            }
        }
    }
}
