// Tags: JDK1.1

// Copyright (C) 2005 David Gilbert <david.gilbert@object-refinery.com>

// Mauve is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2, or (at your option)
// any later version.

// Mauve is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with Mauve; see the file COPYING.  If not, write to
// the Free Software Foundation, 59 Temple Place - Suite 330,
// Boston, MA 02111-1307, USA.  */

package gnu.testlet.java2.util.Date;

import gnu.testlet.TestHarness;
import gnu.testlet.Testlet;

import java.util.Date;

/**
 * Some checks for the before() method in the {@link Date} class.
 */
public class before implements Testlet 
{

  /**
   * Runs the test using the specified harness.
   * 
   * @param harness  the test harness (<code>null</code> not permitted).
   */
  public void test(TestHarness harness)   
  {
    Date d1 = new Date(-1L);
    Date d2 = new Date(0L);
    Date d3 = new Date(1L);

    harness.check(!d1.before(d1));
    harness.check(d1.before(d2));
    harness.check(d1.before(d3));
    harness.check(!d2.before(d1));
    harness.check(!d2.before(d2));
    harness.check(d2.before(d3));
    harness.check(!d3.before(d1));
    harness.check(!d3.before(d2));
    harness.check(!d3.before(d3));
 
    boolean pass = false;
    try
    {
      d1.before(null);   
    }
    catch (NullPointerException e)
    {
      pass = true;
    }
    harness.check(pass);
  }

}
