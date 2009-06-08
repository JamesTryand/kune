/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.ourproject.kune.platf.client.ui.rte.insertlink;

import org.ourproject.kune.platf.client.ui.rte.RichTextArea;
import org.xwiki.gwt.dom.client.Element;
import org.xwiki.gwt.dom.client.Range;
import org.xwiki.gwt.dom.client.Selection;

import com.google.gwt.event.dom.client.HasLoadHandlers;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Base class for tests running on a rich text area.
 * 
 * @version $Id$
 */
public class AbstractRichTextAreaTest extends AbstractWysiwygClientTest implements LoadHandler {
    /**
     * The number of milliseconds we delay the test finish. This delay is needed
     * because in some browsers the rich text area is initialized after a
     * timeout.
     */
    public static final int FINISH_DELAY = 400;

    /**
     * The number of milliseconds we delay the test start. This delay is needed
     * because in some browsers the rich text area is initialized after a
     * timeout and we want to start the test after the rich text area is fully
     * initialized.
     */
    public static final int START_DELAY = 300;

    /**
     * The rich text area on which we run the tests.
     */
    protected RichTextArea rta;

    public void onLoad(final LoadEvent event) {
        // http://wiki.codetalks.org/wiki/index.php/Docs/Keyboard_navigable_JS_widgets
        // #Use_setTimeout_with_element.focus.28.29_to_set_focus
        (new Timer() {
            @Override
            public void run() {
                rta.setFocus(true);
            }
        }).schedule(1);
    }

    /**
     * Cleans the HTML input. This is needed in order to have uniform tests
     * between Firefox and Internet Explorer.
     * 
     * @param html
     *            The HTML fragment to be cleaned.
     * @return The input string in lower case, stripped of new lines.
     */
    protected String clean(final String html) {
        return html.replaceAll("\r\n", "").toLowerCase();
    }

    /**
     * @return The body element of the DOM document edited with the rich text
     *         area.
     */
    protected Element getBody() {
        return rta.getDocument().getBody().cast();
    }

    /**
     * {@inheritDoc}
     * 
     * @see AbstractWysiwygClientTest#gwtSetUp()
     */
    @Override
    protected void gwtSetUp() throws Exception {
        super.gwtSetUp();

        if (rta == null) {
            rta = new RichTextArea();
            // Workaround till GWT provides a way to detect when the rich text
            // area has finished loading.
            if (rta.getBasicFormatter() instanceof HasLoadHandlers) {
                ((HasLoadHandlers) rta.getBasicFormatter()).addLoadHandler(this);
            }
        }
        RootPanel.get().add(rta);
    }

    /**
     * {@inheritDoc}
     * 
     * @see AbstractWysiwygClientTest#gwtTearDown()
     */
    @Override
    protected void gwtTearDown() throws Exception {
        super.gwtTearDown();

        RootPanel.get().remove(rta);
    }

    /**
     * Removes the non-breaking spaces, {@code &nbsp;}, from the given HTML. The
     * is method is needed because the Selection implementation for Internet
     * Explorer adds {@code &nbsp;} when it cannot place the caret at the
     * specified place.
     * 
     * @param html
     *            the HTML fragment to be cleaned of non-breaking spaces
     * @return the input HTML fragment without any non-breaking spaces
     */
    protected String removeNonBreakingSpaces(final String html) {
        return html.replace("&nbsp;", "");
    }

    /**
     * Selects the given range.
     * 
     * @param range
     *            The range to be selected.
     */
    protected void select(final Range range) {
        final Selection selection = rta.getDocument().getSelection();
        selection.removeAllRanges();
        selection.addRange(range);
    }
}
