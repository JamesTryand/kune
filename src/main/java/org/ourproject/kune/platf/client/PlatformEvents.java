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

package org.ourproject.kune.platf.client;

public interface PlatformEvents {
    public static final String ATTACH_TO_EXT_POINT = "ws.AttachToExtensionPoint";
    public static final String DETACH_FROM_EXT_POINT = "ws.DetachToExtensionPoint";
    public static final String CLEAR_EXT_POINT = "ws.ClearExtensionPoint";
    public static final String GOTO = "ws.Goto";
    public static final String GOTO_CONTAINER = "ws.GotoContainer";
}