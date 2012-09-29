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
package cc.kune.gspace.client;

import cc.kune.core.client.events.AppStartEvent;
import cc.kune.core.client.events.AppStartEvent.AppStartHandler;
import cc.kune.core.client.i18n.I18n;
import cc.kune.core.client.sitebar.search.SitebarSearchPresenter;
import cc.kune.core.client.sn.actions.WriteToBuddyHeaderButton;
import cc.kune.core.client.state.HistoryTokenMustBeAuthCallback;
import cc.kune.core.client.state.Session;
import cc.kune.core.client.state.SiteTokenListeners;
import cc.kune.core.client.state.SiteTokens;
import cc.kune.gspace.client.actions.GiveUsFeedbackBtn;
import cc.kune.gspace.client.armor.GSpaceArmor;
import cc.kune.gspace.client.i18n.I18nToTranslateGridPanel;
import cc.kune.gspace.client.i18n.I18nTranslator;
import cc.kune.gspace.client.i18n.I18nTranslatorTabsCollection;
import cc.kune.gspace.client.i18n.SiteOptionsI18nTranslatorAction;
import cc.kune.gspace.client.maxmin.MaxMinWorkspace;
import cc.kune.gspace.client.options.GroupOptions;
import cc.kune.gspace.client.options.UserOptions;
import cc.kune.gspace.client.tags.TagsSummaryPresenter;
import cc.kune.gspace.client.themes.GSpaceThemeManager;
import cc.kune.gspace.client.tool.selector.ToolSelector;
import cc.kune.gspace.client.ui.footer.license.EntityLicensePresenter;
import cc.kune.gspace.client.viewers.ContentViewerPresenter;
import cc.kune.gspace.client.viewers.FolderViewerPresenter;
import cc.kune.gspace.client.viewers.NoHomePageViewer;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class GSpaceParts {

  @Inject
  public GSpaceParts(final Session session, final GSpaceThemeManager themeManager, final GSpaceArmor armor,
      final Provider<EntityLicensePresenter> licenseFooter,
      final Provider<TagsSummaryPresenter> tagsPresenter, final Provider<ToolSelector> toolSelector,
      final Provider<NoHomePageViewer> noHome, final Provider<ContentViewerPresenter> docsViewer,
      final Provider<FolderViewerPresenter> folderViewer, final Provider<GroupOptions> go,
      final Provider<UserOptions> uo, final Provider<SitebarSearchPresenter> siteSearch,
      final Provider<SiteOptionsI18nTranslatorAction> transAction,
      final Provider<MaxMinWorkspace> maxMinWorkspace, final Provider<GiveUsFeedbackBtn> giveUsFeedback,
      final Provider<I18nToTranslateGridPanel> toTrans,
      final Provider<I18nTranslatorTabsCollection> gtranslator,
      final Provider<I18nTranslator> translator, final Provider<WriteToBuddyHeaderButton> writeToBuddie,
      final SiteTokenListeners tokenListener) {
    session.onAppStart(true, new AppStartHandler() {
      @Override
      public void onAppStart(final AppStartEvent event) {
        armor.setRTL(I18n.getDirection());
        licenseFooter.get();
        tagsPresenter.get();
        toolSelector.get();
        docsViewer.get();
        folderViewer.get();
        siteSearch.get();
        noHome.get();
        maxMinWorkspace.get();
        // // Add User & Groups Options
        // final GroupOptionsCollection goc = gocProv.get();
        // final UserOptionsCollection uoc = uocProv.get();

        // Init
        go.get();
        uo.get();

        // i18n
        if (event.getInitData().isTranslatorEnabled()) {
          transAction.get();
        }
        gtranslator.get().add(toTrans);

        // Feedback
        if (event.getInitData().isFeedbackEnabled()) {
          giveUsFeedback.get();
        }

        // Others
        writeToBuddie.get();
      }
    });
    tokenListener.put(SiteTokens.TRANSLATE, new HistoryTokenMustBeAuthCallback("") {
      @Override
      public void onHistoryToken(final String token) {
        if (session.isLogged() && session.getInitData().isTranslatorEnabled()) {
          translator.get().show();
        }
      }
    });
  }
}
