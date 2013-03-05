package cc.kune.colorpalette.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class KuneColorPaletteEntryPoint implements EntryPoint {

  /**
   * This is the entry point method.
   */
  @Override
  public void onModuleLoad() {
    ColorWebSafePalettePresenter pres = new ColorWebSafePalettePresenter();
    ColorWebSafePalettePanel panel = new ColorWebSafePalettePanel(pres);
   panel.show(0, 0);
  }
}