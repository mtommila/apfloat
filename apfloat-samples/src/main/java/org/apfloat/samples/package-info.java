/*
 * MIT License
 *
 * Copyright (c) 2002-2023 Mikko Tommila
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/**
Sample applications demonstrating apfloat use.<p>

Three different versions of an application for calculating &pi; are
included. The simplest, {@link org.apfloat.samples.Pi} runs on one
computer using one processor (and one thread) only. {@link org.apfloat.samples.PiParallel}
executes multiple threads in parallel and has vastly better performance
on multi-core computers. Finally, {@link org.apfloat.samples.PiDistributed}
can use multiple separate computers for calculating pi with even
greater processing power.<p>

As a curiosity, two applets are provided for running {@link org.apfloat.samples.Pi}
and {@link org.apfloat.samples.PiParallel} through a graphical user
interface: {@link org.apfloat.samples.PiApplet} and {@link org.apfloat.samples.PiParallelApplet},
correspondingly. These programs can also be run as stand-alone
Java applications: {@link org.apfloat.samples.PiGUI} and {@link org.apfloat.samples.PiParallelGUI}.<p>

Compared to the C++ version of apfloat, the Java version pi calculation
program is usually just as fast. Even in worst cases the Java version achieves
roughly 50% of the performance of the assembler-optimized C++ versions of apfloat.
Modern JVMs are nearly as efficient as optimizing C++ compilers in code generation.
The advantage that JVMs have over native C++ compilers is obviously that the JVM
generates optimal code for every target architecture and runtime profile
automatically, from an intermediate portable binary executable format. With C++,
the source code must be compiled and profiled manually for every target
architecture, which can be difficult and tedious.<p>

On multi-core computers the Java parallel pi calculator is often significantly faster
than the C++ parallel version. The same applies to the distributed pi calculator.
Multi-threaded and distributed applications are more efficient to implement in Java
due to C++'s historical lack of standard libraries for threading and networking.
*/

package org.apfloat.samples;
