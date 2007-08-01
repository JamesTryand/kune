package org.ourproject.kune.sitebar.client.ui;

import org.ourproject.kune.sitebar.client.SiteBar;
import org.ourproject.kune.sitebar.client.Translate;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoginPanel extends Composite implements LoginView, ClickListener, ChangeListener, KeyboardListener {

    final Button send;
    private final Button cancel;
    private final LoginListener listener;
    private TextBox nick;
    private PasswordTextBox pass;

    public LoginPanel(final LoginListener listener) {
        final VerticalPanel generalVP = new VerticalPanel();
        final Translate t = SiteBar.getInstance().t;

        initWidget(generalVP);
        this.listener = listener;
        final HorizontalPanel buttonsHP = new HorizontalPanel();
        final Grid panelGrid = new Grid(2, 2);
        final Label nickLabel = new Label(t.NickName());
        final Label passLabel = new Label(t.Password());
        nick = new TextBox();
        pass = new PasswordTextBox();
        send = new Button(t.Login());
        cancel = new Button(t.Cancel());

        // Layout
        generalVP.add(panelGrid);
        panelGrid.setWidget(0, 0, nickLabel);
        panelGrid.setWidget(0, 1, nick);
        panelGrid.setWidget(1, 0, passLabel);
        panelGrid.setWidget(1, 1, pass);
        buttonsHP.add(send);
        buttonsHP.add(cancel);
        generalVP.add(buttonsHP);

        // Set properties
        send.addClickListener(this);
        send.setEnabled(false);
        cancel.addClickListener(this);
        nick.addChangeListener(this);
        pass.addChangeListener(this);
        nick.addKeyboardListener(this);
        pass.addKeyboardListener(this);
    }

    public void onClick(final Widget sender) {
        if (sender == send) {
            listener.doLogin(nick.getText(), pass.getText());
        } else if (sender == cancel) {
            listener.doCancel();
        }
    }

    public void setEnabledLoginButton(boolean enabled) {
        send.setEnabled(enabled);
    }

    public boolean isEnabledLoginButton() {
        return send.isEnabled();
    }

    public void onChange(Widget sender) {
        listener.onDataChanged(nick.getText(), pass.getText());
    }

    public void onKeyDown(Widget arg0, char arg1, int arg2) {
    }

    public void onKeyPress(Widget arg0, char arg1, int arg2) {
        listener.onDataChanged(nick.getText(), pass.getText());
    }

    public void onKeyUp(Widget arg0, char arg1, int arg2) {
        listener.onDataChanged(nick.getText(), pass.getText());
    }

}
