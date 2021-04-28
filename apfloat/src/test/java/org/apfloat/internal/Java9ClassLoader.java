/*
 * MIT License
 *
 * Copyright (c) 2002-2021 Mikko Tommila
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
package org.apfloat.internal;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class loader to load Java 9 specific multi-version classes.
 *
 * @version 1.9.0
 * @author Mikko Tommila
 */

public class Java9ClassLoader extends ClassLoader
{
    public Java9ClassLoader(ClassLoader parent)
    {
        super(parent);
    }

    /**
     * Force load a Java 8 class from the normal (non-multi-version) location.
     * Loads the class again even if it has been already loaded by this or a
     * parent class loader.
     *
     * @param name Name of the class.
     *
     * @return The class.
     *
     * @throws IOException In case of error reading the class bytes.
     * @throws ClassNotFoundException In case the class file is not found.
     */

    public Class<?> loadJava8Class(String name)
        throws IOException, ClassNotFoundException
    {
        return loadClassWithPrefix("", name);
    }

    /**
     * Force load a Java 9 class from the Java 9 multi-version location.
     * Loads the class again even if it has been already loaded by this or a
     * parent class loader.
     *
     * @param name Name of the class.
     *
     * @return The class.
     *
     * @throws IOException In case of error reading the class bytes.
     * @throws ClassNotFoundException In case the class file is not found.
     */

    public Class<?> loadJava9Class(String name)
        throws IOException, ClassNotFoundException
    {
        return loadClassWithPrefix("META-INF/versions/9/", name);
    }

    private Class<?> loadClassWithPrefix(String prefix, String name)
        throws IOException, ClassNotFoundException
    {
        String path = prefix + name.replace('.', '/') + ".class";
        byte[] bytes;
        try (InputStream in = getResourceAsStream(path))
        {
            if (in == null) {
                throw new ClassNotFoundException(name);
            }
            bytes = in.readAllBytes();
        }
        return defineClass(name, bytes, 0, bytes.length);
    }
}
