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
package cc.kune.core.shared;

/**
 * The Class SessionConstants.
 */
public final class SessionConstants {

  /** The Constant _AN_HOUR in millisecons */
  public final static int _AN_HOUR = 1000 * 60 * 60;

  /** The Constant _5_HOURS in millisecons */
  public final static int _5_HOURS = 5 * _AN_HOUR;

  /** The Constant A_DAY in millisecons */
  public final static long A_DAY = _AN_HOUR * 24;

  /** The Constant ANON_SESSION_DURATION. */
  public final static long ANON_SESSION_DURATION = A_DAY;

  /** The Constant ANON_SESSION_DURATION_AFTER_REG. */
  public final static long ANON_SESSION_DURATION_AFTER_REG = A_DAY * 365;

  /**
   * The Constant MIN_SIGN_IN_FOR_NEWBIES (number of access to kune under that,
   * the user is viewed as a newbie (so, more help is needed)
   */
  public final static long MIN_SIGN_IN_FOR_NEWBIES = 10;

  /** The Constant JSESSIONID. */
  public static final String JSESSIONID = "JSESSIONID";
  // session duration
  /** The Constant SESSION_DURATION. */
  public final static long SESSION_DURATION = A_DAY * 30; // four weeks login
  // public final static long SESSION_DURATION = 100; // For test
  /** The Constant USERHASH. */
  public final static String USERHASH = "k007userHash";

  /**
   * Instantiates a new session constants.
   */
  public SessionConstants() {

  }
}