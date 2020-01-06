/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2020  Mikko Tommila
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.apfloat.spi;

import java.util.concurrent.Future;

/**
 * Thread execution operations.
 *
 * @since 1.9.0
 * @version 1.9.0
 * @author Mikko Tommila
 */

public interface ExecutionStrategy
{
    /**
     * While waiting for a <code>Future</code> to be completed, steal work
     * from any running tasks and run it.<p>
     *
     * While this method may functionally appear to be equivalent to just
     * calling <code>future.get()</code> it should try its best to steal
     * work from any other tasks submitted to the <code>ExecutorService</code>
     * of the current <code>ApfloatContext</code>. It may in fact steal
     * work using multiple threads, if the current <code>ApfloatContext</code>
     * specifies <code>numberOfProcessors</code> to be more than one.<p>
     *
     * The purpose of this method is to allow keeping all threads of the
     * <code>ExecutorService</code> (and CPU cores) maximally busy at all
     * times, while also not running an excessive number of parallel
     * threads (only as many threads as there are CPU cores).
     *
     * @param future The Future to wait for.
     */

    public void wait(Future<?> future);
}
