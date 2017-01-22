/*
 * Apfloat arbitrary precision arithmetic library
 * Copyright (C) 2002-2017  Mikko Tommila
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
package org.apfloat;

import java.util.Properties;
import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version 1.8.0
 * @author Mikko Tommila
 */

public class ApfloatContextTest
    extends TestCase
{
    public ApfloatContextTest(String methodName)
    {
        super(methodName);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite()
    {
        TestSuite suite = new TestSuite();

        suite.addTest(new ApfloatContextTest("testSetGetProperties"));
        suite.addTest(new ApfloatContextTest("testSetGetExtraProperty"));
        suite.addTest(new ApfloatContextTest("testSetGetAttributes"));
        suite.addTest(new ApfloatContextTest("testFields"));
        suite.addTest(new ApfloatContextTest("testClone"));
        suite.addTest(new ApfloatContextTest("testException"));
        suite.addTest(new ApfloatContextTest("testThreadContexts"));

        return suite;
    }

    @SuppressWarnings("deprecation")
    public static void testSetGetProperties()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        Properties properties = new Properties();

        properties.setProperty(ApfloatContext.BUILDER_FACTORY, "org.apfloat.internal.LongBuilderFactory");
        properties.setProperty(ApfloatContext.DEFAULT_RADIX, "11");
        properties.setProperty(ApfloatContext.MAX_MEMORY_BLOCK_SIZE, "1048576");
        properties.setProperty(ApfloatContext.CACHE_L1_SIZE, "16384");
        properties.setProperty(ApfloatContext.CACHE_L2_SIZE, "524288");
        properties.setProperty(ApfloatContext.CACHE_BURST, "128");
        properties.setProperty(ApfloatContext.MEMORY_THRESHOLD, "131072");
        properties.setProperty(ApfloatContext.SHARED_MEMORY_TRESHOLD, "262144");
        properties.setProperty(ApfloatContext.BLOCK_SIZE, "131072");
        properties.setProperty(ApfloatContext.NUMBER_OF_PROCESSORS, "8");
        properties.setProperty(ApfloatContext.FILE_PATH, "./");
        properties.setProperty(ApfloatContext.FILE_INITIAL_VALUE, "42");
        properties.setProperty(ApfloatContext.FILE_SUFFIX, ".dat");
        properties.setProperty(ApfloatContext.CLEANUP_AT_EXIT, "false");

        ctx.setProperties(properties);
        properties = ctx.getProperties();

        assertEquals("size", 15, properties.size());
        assertEquals("ApfloatContext.BUILDER_FACTORY", "org.apfloat.internal.LongBuilderFactory", ctx.getProperty(ApfloatContext.BUILDER_FACTORY));
        assertEquals("ApfloatContext.DEFAULT_RADIX", "11", ctx.getProperty(ApfloatContext.DEFAULT_RADIX));
        assertEquals("ApfloatContext.MAX_MEMORY_BLOCK_SIZE", "1048576", ctx.getProperty(ApfloatContext.MAX_MEMORY_BLOCK_SIZE));
        assertEquals("ApfloatContext.CACHE_L1_SIZE", "16384", ctx.getProperty(ApfloatContext.CACHE_L1_SIZE));
        assertEquals("ApfloatContext.CACHE_L2_SIZE", "524288", ctx.getProperty(ApfloatContext.CACHE_L2_SIZE));
        assertEquals("ApfloatContext.CACHE_BURST", "128", ctx.getProperty(ApfloatContext.CACHE_BURST));
        assertEquals("ApfloatContext.MEMORY_TRESHOLD", "131072", ctx.getProperty(ApfloatContext.MEMORY_TRESHOLD));
        assertEquals("ApfloatContext.MEMORY_THRESHOLD", "131072", ctx.getProperty(ApfloatContext.MEMORY_THRESHOLD));
        assertEquals("ApfloatContext.SHARED_MEMORY_TRESHOLD", "262144", ctx.getProperty(ApfloatContext.SHARED_MEMORY_TRESHOLD));
        assertEquals("ApfloatContext.BLOCK_SIZE", "131072", ctx.getProperty(ApfloatContext.BLOCK_SIZE));
        assertEquals("ApfloatContext.NUMBER_OF_PROCESSORS", "8", ctx.getProperty(ApfloatContext.NUMBER_OF_PROCESSORS));
        assertEquals("ApfloatContext.FILE_PATH", "./", ctx.getProperty(ApfloatContext.FILE_PATH));
        assertEquals("ApfloatContext.FILE_INITIAL_VALUE", "42", ctx.getProperty(ApfloatContext.FILE_INITIAL_VALUE));
        assertEquals("ApfloatContext.FILE_SUFFIX", ".dat", ctx.getProperty(ApfloatContext.FILE_SUFFIX));
        assertEquals("ApfloatContext.CLEANUP_AT_EXIT", "false", ctx.getProperty(ApfloatContext.CLEANUP_AT_EXIT));

        assertEquals("ApfloatContext.BUILDER_FACTORY", "org.apfloat.internal.LongBuilderFactory", ctx.getBuilderFactory().getClass().getName());
        assertEquals("ApfloatContext.DEFAULT_RADIX", 11, ctx.getDefaultRadix());
        assertEquals("ApfloatContext.MAX_MEMORY_BLOCK_SIZE", 1048576, ctx.getMaxMemoryBlockSize());
        assertEquals("ApfloatContext.CACHE_L1_SIZE", 16384, ctx.getCacheL1Size());
        assertEquals("ApfloatContext.CACHE_L2_SIZE", 524288, ctx.getCacheL2Size());
        assertEquals("ApfloatContext.CACHE_BURST", 128, ctx.getCacheBurst());
        assertEquals("ApfloatContext.MEMORY_TRESHOLD", 131072, ctx.getMemoryTreshold());
        assertEquals("ApfloatContext.MEMORY_THRESHOLD", 131072, ctx.getMemoryThreshold());
        assertEquals("ApfloatContext.SHARED_MEMORY_TRESHOLD", 262144, ctx.getSharedMemoryTreshold());
        assertEquals("ApfloatContext.BLOCK_SIZE", 131072, ctx.getBlockSize());
        assertEquals("ApfloatContext.NUMBER_OF_PROCESSORS", 8, ctx.getNumberOfProcessors());
        assertEquals("ApfloatContext.CLEANUP_AT_EXIT", false, ctx.getCleanupAtExit());
        assertEquals("Filename", "./42.dat", ctx.getFilenameGenerator().generateFilename());

        // Memory treshold vs. threshold
        properties.setProperty(ApfloatContext.MEMORY_TRESHOLD, "4294967296");
        properties.remove(ApfloatContext.MEMORY_THRESHOLD);
        ctx.setProperties(properties);
        assertEquals("ApfloatContext.MEMORY_TRESHOLD 4G", Integer.MAX_VALUE, ctx.getMemoryTreshold());
        assertEquals("ApfloatContext.MEMORY_THRESHOLD 4G", 4294967296L, ctx.getMemoryThreshold());
        ctx.setProperty(ApfloatContext.MEMORY_TRESHOLD, "" + (6L << 30));
        assertEquals("ApfloatContext.MEMORY_TRESHOLD 6G", Integer.MAX_VALUE, ctx.getMemoryTreshold());
        assertEquals("ApfloatContext.MEMORY_THRESHOLD 6G", 6L << 30, ctx.getMemoryThreshold());
        ctx.setProperty(ApfloatContext.MEMORY_THRESHOLD, "" + (8L << 30));
        assertEquals("ApfloatContext.MEMORY_TRESHOLD 8G", Integer.MAX_VALUE, ctx.getMemoryTreshold());
        assertEquals("ApfloatContext.MEMORY_THRESHOLD 8G", 8L << 30, ctx.getMemoryThreshold());
        ctx.setMemoryThreshold(9L << 30);
        assertEquals("ApfloatContext.MEMORY_TRESHOLD 9G", Integer.MAX_VALUE, ctx.getMemoryTreshold());
        assertEquals("ApfloatContext.MEMORY_THRESHOLD 9G", 9L << 30, ctx.getMemoryThreshold());
    }

    public static void testSetGetExtraProperty()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        assertEquals("Extra property default value", "default", ctx.getProperty("extra", "default"));
        ctx.setProperty("extra", "value");
        assertEquals("Extra property value", "value", ctx.getProperty("extra"));
        assertEquals("Extra property value, not default", "value", ctx.getProperty("extra", "default"));
        assertNull("Nonexistent property value", ctx.getProperty("bogus"));
    }

    public static void testSetGetAttributes()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        ctx.setAttribute("string", "value");
        assertEquals("string", "value", ctx.getAttribute("string"));
        Enumeration<String> attributeNames = ctx.getAttributeNames();
        assertTrue("has 1 attribute", attributeNames.hasMoreElements());
        assertEquals("attribute name", "string", attributeNames.nextElement());
        assertFalse("has no more than 1 attribute", attributeNames.hasMoreElements());
        ctx.setAttribute("integer", 6);
        assertEquals("integer", 6, ctx.getAttribute("integer"));
        assertEquals("update integer", 6, ctx.setAttribute("integer", 5));
        assertEquals("updated integer", 5, ctx.getAttribute("integer"));
        assertEquals("remove integer", 5, ctx.removeAttribute("integer"));
        assertNull("removed integer", ctx.getAttribute("integer"));
        assertNull("remove removed integer", ctx.removeAttribute("integer"));
    }

    public static void testFields()
    {
        ApfloatContext ctx = ApfloatContext.getContext();

        ExecutorService executorService = Executors.newCachedThreadPool();
        assertNotSame("Old ExecutorService", executorService, ctx.getExecutorService());
        ctx.setExecutorService(executorService);
        assertSame("New ExecutorService", executorService, ctx.getExecutorService());

        Object lock = new Object();
        assertNotSame("Old sharedMemoryLock", lock, ctx.getSharedMemoryLock());
        ctx.setSharedMemoryLock(lock);
        assertSame("New sharedMemoryLock", lock, ctx.getSharedMemoryLock());
    }

    public static void testClone()
    {
        ApfloatContext ctx = ApfloatContext.getContext(),
                       cloneCtx = (ApfloatContext) ctx.clone();

        assertNotSame("ctx", ctx, cloneCtx);
        assertSame("BuilderFactory", ctx.getBuilderFactory(), cloneCtx.getBuilderFactory());
        assertSame("FilenameGenerator", ctx.getFilenameGenerator(), cloneCtx.getFilenameGenerator());
        assertSame("SharedMemoryLock", ctx.getSharedMemoryLock(), cloneCtx.getSharedMemoryLock());
        assertNotSame("Properties", ctx.getProperties(), cloneCtx.getProperties());
        assertEquals("Properties size", ctx.getProperties().size(), cloneCtx.getProperties().size());
        ctx.setAttribute("bogus", "bogus");
        assertNull("Attribute", cloneCtx.getAttribute("bogus"));
    }

    public static void testException()
    {
        ApfloatContext ctx = ApfloatContext.getContext();
        try
        {
            ctx.setProperty(ApfloatContext.BUILDER_FACTORY, "bogus");
            fail("Bogus class accepted");
        }
        catch (ApfloatConfigurationException ace)
        {
            // OK
        }
    }

    public static void testThreadContexts()
        throws InterruptedException
    {
        ApfloatContext ctx = ApfloatContext.getContext(),
                       cloneCtx = (ApfloatContext) ctx.clone();

        assertSame("Context is global", ApfloatContext.getGlobalContext(), ApfloatContext.getContext());
        assertNull("Current thread context is null", ApfloatContext.getThreadContext());

        ApfloatContext.setThreadContext(cloneCtx);
        assertNotSame("Context is not global", ApfloatContext.getGlobalContext(), ApfloatContext.getContext());

        ApfloatContext.removeThreadContext();
        assertSame("Context is global again", ApfloatContext.getGlobalContext(), ApfloatContext.getContext());

        Thread thread = new Thread()
        {
            public void run()
            {
                assertNotSame("In-thread context is not global", ApfloatContext.getGlobalContext(), ApfloatContext.getContext());
                assertNotSame("In-thread thread context is not global", ApfloatContext.getGlobalContext(), ApfloatContext.getThreadContext());
            }
        };

        assertNull("Thread context is null", ApfloatContext.getThreadContext(thread));
        ApfloatContext.setThreadContext(cloneCtx, thread);
        assertNotNull("Thread context is set", ApfloatContext.getThreadContext(thread));
        assertNotSame("Thread context is not global", ApfloatContext.getGlobalContext(), ApfloatContext.getThreadContext(thread));
        assertSame("Current context is still global", ApfloatContext.getGlobalContext(), ApfloatContext.getContext());

        thread.start();
        thread.join();

        ApfloatContext.clearThreadContexts();
        assertNull("Thread context is removed", ApfloatContext.getThreadContext(thread));
    }
}
