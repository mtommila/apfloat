/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2019  Mikko Tommila
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

/**
Transforms for using a GPU via the aparapi library.<p>

This package contains Number-Theoretic Transform implementations that can use
the GPU (Graphics Processing Unit) for executing the transforms. There is significant
overhead in invoking the GPU, e.g. in transferring the data between the main memory
and the GPU memory, so for small data sets there is usually no performance improvement,
and in many cases performance can be even significantly slower. However for very large
calculations, e.g. one billion digits, using the GPU can improve the performance noticeably,
depending on the hardware used.
*/

package org.apfloat.aparapi;
