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

package org.ourproject.kune.workspace.client.i18n.ui;

import org.ourproject.kune.platf.client.services.Kune;
import org.ourproject.kune.workspace.client.i18n.LanguageSelectorPresenter;
import org.ourproject.kune.workspace.client.i18n.LanguageSelectorView;

import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.event.ComboBoxListener;

public class LanguageSelectorPanel extends FormPanel implements LanguageSelectorView {

    public static final String LANG_ID = "abbr";

    private static final String LANG_FIELD = "lang";

    private ComboBox langCombo;

    private final LanguageSelectorPresenter presenter;

    public LanguageSelectorPanel(final LanguageSelectorPresenter presenter, final String langFieldTitle) {
        super();
        super.setBorder(false);
        if (langFieldTitle == null) {
            setHideLabels(true);
        }
        this.presenter = presenter;
        createLangCombo(langFieldTitle);
        super.add(langCombo);
    }

    public LanguageSelectorPanel(final LanguageSelectorPresenter presenter) {
        this(presenter, null);
    }

    public void setLanguage(final String languageCode) {
        langCombo.setValue(languageCode);
    }

    public String getLanguage() {
        return langCombo.getValueAsString();
    }

    public void reset() {
        langCombo.reset();
    }

    public void addChangeListener(final ComboBoxListener listener) {
        langCombo.addListener(listener);
    }

    private void createLangCombo(final String langFieldTitle) {
        final Store langStore = new SimpleStore(new String[] { LANG_ID, "language" }, getLanguages());
        langStore.load();

        Field.setMsgTarget("side");
        langCombo = new ComboBox();
        langCombo.setName(LANG_FIELD);
        langCombo.setMinChars(1);
        if (langFieldTitle != null) {
            langCombo.setFieldLabel(langFieldTitle);
        }
        langCombo.setStore(langStore);
        langCombo.setDisplayField("language");
        langCombo.setMode(ComboBox.LOCAL);
        langCombo.setTriggerAction(ComboBox.ALL);
        langCombo.setEmptyText(Kune.I18N.t("Enter language"));
        langCombo.setLoadingText(Kune.I18N.t("Searching..."));
        langCombo.setTypeAhead(true);
        langCombo.setTypeAheadDelay(1000);
        langCombo.setSelectOnFocus(false);
        langCombo.setWidth(150);
        langCombo.setValueField(LANG_ID);
        langCombo.setPageSize(7);
        langCombo.setForceSelection(true);
        langCombo.setAllowBlank(false);
    }

    private Object[][] getLanguages() {
        return presenter.getLanguages();
    }
}