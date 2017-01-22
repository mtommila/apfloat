package org.apfloat.samples;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatContext;
import org.apfloat.ApfloatMath;
import org.apfloat.ApfloatRuntimeException;

/**
 * Calculates pi using a cluster of servers.
 * The servers should be running {@link OperationServer}.
 *
 * The names and ports of the cluster nodes are read from the file
 * <code>cluster.properties</code>, or a <code>ResourceBundle</code>
 * by the name "cluster". The format of the property file is as
 * follows:
 *
 * <pre>
 * server1=hostname.company.com:1234
 * server2=hostname2.company.com:2345
 * server3=hostname3.company.com:3456
 * weight1=100
 * weight2=110
 * weight3=50
 * </pre>
 *
 * The server addresses are specified as hostname:port. Weights can
 * (but don't have to) be assigned to nodes to indicate the relative
 * performance of each node, to allow distributing a suitable amount
 * of work for each node. For example, <code>weight2</code> is the
 * relative performance of <code>server2</code> etc. The weights must
 * be integers in the range 1...1000.<p>
 *
 * Guidelines for configuring the servers:
 *
 * <ul>
 *   <li>If the machines are not identical, give proper weights to every
 *       machine. This can improve performance greatly.</li>
 *   <li>If the machines are somewhat similar (e.g. same processor but
 *       different clock frequency), you can calculate the weight roughly
 *       as <code>clockFrequency * numberOfProcessors</code>. For example,
 *       a machine with two 1600MHz processors is four times as fast as
 *       a machine with one 800MHz processor.
 *       </li>
 *   <li>If the machines are very heterogenous, you can benchmark their
 *       performance by running e.g. {@link PiParallel} with one
 *       million digits. Remember to specify the correct number of
 *       CPUs on each machine.</li>
 *   <li>Different JVMs can have different performance. For example,
 *       Sun's Java client VM achieves roughly two thirds of the
 *       performance of the server VM when running this application.</li>
 *   <li>When running {@link OperationServer} on the cluster nodes,
 *       specify the number of worker threads for each server to be
 *       the same as the number of CPUs of the machine.</li>
 *   <li>Additionally, you should specify the number of processors
 *       correctly in the <code>apfloat.properties</code> file
 *       for each cluster server.</li>
 * </ul>
 * <p>
 *
 * Similarly as with {@link PiParallel}, if some nodes have multiple
 * CPUs, to get any performance gain from running many
 * threads in parallel, the JVM must be executing native threads.
 * If the JVM is running in green threads mode, there is no
 * advantage of having multiple threads, as the JVM will in fact
 * execute just one thread and divide its time to multiple
 * simulated threads.
 *
 * @version 1.8.2
 * @author Mikko Tommila
 */

public class PiDistributed
    extends PiParallel
{
    /**
     * Distributed version of the binary splitting algorithm.
     * Uses multiple computers to calculate pi in parallel.
     */

    protected static class DistributedBinarySplittingPiCalculator
        extends ParallelBinarySplittingPiCalculator
    {
        /**
         * Construct a distributed pi calculator with the specified precision and radix.
         *
         * @param series The binary splitting series to be used.
         */

        public DistributedBinarySplittingPiCalculator(BinarySplittingSeries series)
        {
            super(series);
        }

        /**
         * Entry point for the distributed binary splitting algorithm.
         *
         * @param n1 Start term.
         * @param n2 End term.
         * @param T Algorithm parameter.
         * @param Q Algorithm parameter.
         * @param P Algorithm parameter.
         * @param F Pointer to inverse square root parameter.
         * @param nodes The operation executors to be used for the calculation.
         */

        public void r(final long n1, final long n2, final ApfloatHolder T, final ApfloatHolder Q, final ApfloatHolder P, final ApfloatHolder F, Node[] nodes)
            throws ApfloatRuntimeException
        {
            if (nodes.length == 1)
            {
                // End of splitting work between nodes
                // Calculate remaining terms on the node
                // Splitting of work continues on the server node using multiple threads

                if (DEBUG) Pi.err.println("PiDistributed.r(" + n1 + ", " + n2 + ") transferring to server side node " + nodes[0]);

                ApfloatHolder[] TQP = nodes[0].execute(new Operation<ApfloatHolder[]>()
                {
                    public ApfloatHolder[] execute()
                    {
                        // Continue splitting by threads on server side
                        r(n1, n2, T, Q, P, null);

                        return new ApfloatHolder[] { T, Q, P };
                    }
                });

                T.setApfloat(TQP[0].getApfloat());
                Q.setApfloat(TQP[1].getApfloat());
                if (P != null) P.setApfloat(TQP[2].getApfloat());
            }
            else
            {
                // Multiple nodes available; split work in ratio of node weights and execute in parallel
                // This split is done on the client side

                Object[] objs = splitNodes(nodes);

                final Node[] nodes1 = (Node[]) objs[0],
                             nodes2 = (Node[]) objs[2];
                long weight1 = (Long) objs[1],
                     weight2 = (Long) objs[3];

                final long nMiddle = n1 + (n2 - n1) * weight1 / (weight1 + weight2);
                final ApfloatHolder LT = new ApfloatHolder(),
                                    LQ = new ApfloatHolder(),
                                    LP = new ApfloatHolder();

                if (DEBUG) Pi.err.println("PiDistributed.r(" + n1 + ", " + n2 + ") splitting " + formatArray(nodes) + " to r(" + n1 + ", " + nMiddle + ") " + formatArray(nodes1) + ", r(" + nMiddle + ", " + n2 + ") " + formatArray(nodes2));

                BackgroundOperation<Object> operation;

                // Call recursively this r() method to further split the term calculation
                operation = new BackgroundOperation<Object>(new Operation<Object>()
                {
                    public Object execute()
                    {
                        r(n1, nMiddle, LT, LQ, LP, null, nodes1);
                        return null;
                    }
                });
                r(nMiddle, n2, T, Q, P, null, nodes2);
                operation.getResult();                          // Waits for operation to complete

                // Calculate the combining multiplies using available nodes in parallel

                // Up to 4 calculations will be executed in parallel
                // If more than 4 nodes (threads) are available, each calculation can use multiple nodes (threads)
                assert (P == null || F == null);
                int numberNeeded = (P != null || F != null ? 1 : 0) + 3;
                nodes = recombineNodes(nodes, numberNeeded);

                final Operation<Apfloat> sqrtOperation = new Operation<Apfloat>()
                {
                    public Apfloat execute()
                    {
                        return ApfloatMath.inverseRoot(F.getApfloat(), 2);
                    }
                }, T1operation = new Operation<Apfloat>()
                {
                    public Apfloat execute()
                    {
                        return Q.getApfloat().multiply(LT.getApfloat());
                    }
                }, T2operation = new Operation<Apfloat>()
                {
                    public Apfloat execute()
                    {
                        return LP.getApfloat().multiply(T.getApfloat());
                    }
                }, Toperation = new Operation<Apfloat>()
                {
                    public Apfloat execute()
                    {
                        return T1operation.execute().add(T2operation.execute());
                    }
                }, Qoperation = new Operation<Apfloat>()
                {
                    public Apfloat execute()
                    {
                        return LQ.getApfloat().multiply(Q.getApfloat());
                    }
                }, Poperation = new Operation<Apfloat>()
                {
                    public Apfloat execute()
                    {
                        return LP.getApfloat().multiply(P.getApfloat());
                    }
                };
                final Operation<Apfloat[]> QPoperation = new Operation<Apfloat[]>()
                {
                    public Apfloat[] execute()
                    {
                        return new Apfloat[] { Qoperation.execute(),
                                               P == null ? null : Poperation.execute() };
                    }
                };

                int availableNodes = nodes.length;

                BackgroundOperation<Apfloat> sqrtBackgroundOperation = null,
                                             operation1,
                                             operation2,
                                             operation3 = null;
                if (F != null && availableNodes > 1)
                {
                    if (DEBUG) Pi.err.println("PiDistributed.r(" + n1 + ", " + n2 + ") calculating isqrt on node " + nodes[availableNodes - 1]);

                    sqrtBackgroundOperation = nodes[availableNodes - 1].executeBackground(sqrtOperation);
                    availableNodes--;
                }

                Apfloat t = null,
                        q = null,
                        p = null;

                switch (availableNodes)
                {
                    case 1:
                    {
                        t = nodes[0].execute(Toperation);
                        q = nodes[0].execute(Qoperation);
                        if (P != null) p = nodes[0].execute(Poperation);
                        break;
                    }
                    case 2:
                    {
                        operation1 = nodes[1].executeBackground(T1operation);
                        Apfloat tmp1 = nodes[0].execute(T2operation),
                                tmp2 = operation1.getResult();
                        operation1 = nodes[1].executeBackground(Qoperation);
                        t = executeAdd(nodes[0], tmp1, tmp2);
                        if (P != null) p = nodes[0].execute(Poperation);
                        q = operation1.getResult();
                        break;
                    }
                    case 3:
                    {
                        BackgroundOperation<Apfloat[]> operation1a;
                        operation1a = nodes[2].executeBackground(QPoperation);
                        operation2 = nodes[1].executeBackground(T1operation);
                        Apfloat tmp1 = nodes[0].execute(T2operation),
                                tmp2 = operation2.getResult();
                        t = executeAdd(nodes[1], tmp1, tmp2);
                        Apfloat[] QP = operation1a.getResult();
                        q = QP[0];
                        if (P != null) p = QP[1];
                        break;
                    }
                    default:
                    {
                        operation1 = nodes[availableNodes - 1].executeBackground(T1operation);
                        operation2 = nodes[availableNodes - 3].executeBackground(Qoperation);
                        if (P != null) operation3 = nodes[availableNodes - 4].executeBackground(Poperation);
                        Apfloat tmp1 = nodes[availableNodes - 2].execute(T2operation),
                                tmp2 = operation1.getResult();
                        t = executeAdd(nodes[availableNodes - 1], tmp1, tmp2);
                        q = operation2.getResult();
                        if (P != null) p = operation3.getResult();
                        break;
                    }
                }

                T.setApfloat(t);
                Q.setApfloat(q);
                if (P != null) P.setApfloat(p);

                if (sqrtBackgroundOperation != null)
                {
                    F.setApfloat(sqrtBackgroundOperation.getResult());
                }
            }
        }

        /**
         * Get the available set of operation executor nodes.
         * This implementation returns {@link RemoteOperationExecutor}s,
         * which execute operations on the cluster's nodes.
         *
         * @return The nodes of the cluster.
         */

        public Node[] getNodes()
        {
            ResourceBundle resourceBundle = null;

            try
            {
                resourceBundle = ResourceBundle.getBundle("cluster");
            }
            catch (MissingResourceException mre)
            {
                System.err.println("ResourceBundle \"cluster\" not found");

                System.exit(1);
            }

            Node[] nodes = null;
            List<Node> list = new ArrayList<Node>();
            long totalWeight = 0;
            int weightedNodes = 0;

            // Loop through all properties in the file
            Enumeration<String> keys = resourceBundle.getKeys();
            while (keys.hasMoreElements())
            {
                String key = keys.nextElement();
                // Only process the server properties
                if (key.startsWith("server"))
                {
                    int weight = -1;                    // -1 means unspecified here

                    // Check if a weight is specified for this server
                    try
                    {
                        String weightString = resourceBundle.getString("weight" + key.substring(6));

                        try
                        {
                            weight = Integer.parseInt(weightString);

                            if (weight < MIN_WEIGHT || weight > MAX_WEIGHT)
                            {
                                throw new NumberFormatException(weightString);
                            }

                            weightedNodes++;
                        }
                        catch (NumberFormatException nfe)
                        {
                            System.err.println("Invalid weight: " + nfe.getMessage());

                            System.exit(1);
                        }

                        totalWeight += weight;
                    }
                    catch (MissingResourceException mre)
                    {
                        // Weight not specified, OK
                    }

                    // Parse hostname and port
                    String server = resourceBundle.getString(key);
                    int index = server.indexOf(':');
                    if (index < 0)
                    {
                        System.err.println("No port specified for server: " + server);

                        System.exit(1);
                    }
                    String host = server.substring(0, index),
                           portString = server.substring(index + 1);
                    int port = 0;
                    try
                    {
                        port = Integer.parseInt(portString);
                    }
                    catch (NumberFormatException nfe)
                    {
                        System.err.println("Invalid port for host " + host + ": " + portString);

                        System.exit(1);
                    }

                    list.add(new Node(host, port, weight));
                }
            }

            if (list.size() == 0)
            {
                System.err.println("No nodes for cluster specified");

                System.exit(1);
            }

            nodes = list.toArray(new Node[list.size()]);

            // If no weights were specified at all, all nodes have same weight
            int averageWeight = (weightedNodes == 0 ? 1 : (int) (totalWeight / weightedNodes));

            // Loop through all nodes and set average weight for all nodes that don't have a weight specified
            for (Node node : nodes)
            {
                if (node.getWeight() == -1)
                {
                    node.setWeight(averageWeight);
                }
            }

            // Sort nodes in weight order (smallest first)
            Arrays.sort(nodes);

            // Get the available number of threads for each node
            for (Node node : nodes)
            {
                int numberOfProcessors = node.execute(new Operation<Integer>()
                {
                    public Integer execute()
                    {
                        return ApfloatContext.getGlobalContext().getNumberOfProcessors();
                    }
                });

                node.setNumberOfProcessors(numberOfProcessors);
            }

            if (DEBUG) Pi.err.println("PiDistributed.getNodes " + formatArray(nodes));

            return nodes;
        }

        /**
         * Attempt to combine or split nodes to form the needed number
         * of nodes. The returned number of nodes is something between
         * the number of nodes input and the number of nodes requested.
         * The requested number of nodes can be less than or greater than
         * the number of input nodes.
         *
         * @param nodes The nodes to recombine.
         * @param numberNeeded The requested number of nodes.
         *
         * @return The set of recombined nodes.
         */

        public Node[] recombineNodes(Node[] nodes, int numberNeeded)
        {
            if (numberNeeded <= nodes.length)
            {
                // Method is running on client side
                // RemoteOperationExecutors can't be combined since they don't exist on the same machine like threads

                if (DEBUG) Pi.err.println("PiDistributed.recombineNodes unable to recombine nodes " + formatArray(nodes) + " (" + numberNeeded + " <= " + nodes.length + ")");

                return nodes;
            }
            else
            {
                // Split RemoteOperationExecutors to executors that don't use all threads available on the server

                SortedSet<Node> allNodes = new TreeSet<Node>(),
                                splittableNodes = new TreeSet<Node>();
                for (Node node : nodes)
                {
                    (node.getNumberOfProcessors() > 1 ? splittableNodes : allNodes).add(node);
                }

                // Continue splitting heaviest node until no more splits can be made or we have the needed number of nodes
                while (splittableNodes.size() > 0 && allNodes.size() + splittableNodes.size() < numberNeeded)
                {
                    // Get heaviest splittable node
                    Node node = splittableNodes.last();
                    int numberOfProcessors = node.getNumberOfProcessors(),
                        numberOfProcessors1 = numberOfProcessors / 2,
                        numberOfProcessors2 = (numberOfProcessors + 1) / 2;
                    Node node1 = new Node(node.getHost(),
                                          node.getPort(),
                                          node.getWeight() * numberOfProcessors1 / numberOfProcessors,
                                          numberOfProcessors1),
                         node2 = new Node(node.getHost(),
                                          node.getPort(),
                                          node.getWeight() * numberOfProcessors2 / numberOfProcessors,
                                          numberOfProcessors2);
                    splittableNodes.remove(node);
                    (node1.getNumberOfProcessors() > 1 ? splittableNodes : allNodes).add(node1);
                    (node2.getNumberOfProcessors() > 1 ? splittableNodes : allNodes).add(node2);
                }

                allNodes.addAll(splittableNodes);

                Node[] newNodes = allNodes.toArray(new Node[allNodes.size()]);

                if (DEBUG) Pi.err.println("PiDistributed.recombineNodes recombined " + formatArray(nodes) + " to " + formatArray(newNodes) + " (requested " + numberNeeded + ")");

                return newNodes;
            }
        }

        // Split nodes to two sets that have roughly the same total weights
        private Object[] splitNodes(Node[] nodes)
        {
            List<Node> list1 = new LinkedList<Node>(),
                       list2 = new LinkedList<Node>();
            long weight1 = 0,
                 weight2 = 0;

            // Start from heaviest node to make maximally equal split
            for (int i = nodes.length; --i >= 0;)
            {
                if (weight1 < weight2)
                {
                    list1.add(0, nodes[i]);
                    weight1 += nodes[i].getWeight();
                }
                else
                {
                    list2.add(0, nodes[i]);
                    weight2 += nodes[i].getWeight();
                }
            }

            return new Object[] { list1.toArray(new Node[list1.size()]), weight1,
                                  list2.toArray(new Node[list2.size()]), weight2 };
        }

        private Apfloat executeAdd(Node node, final Apfloat x, final Apfloat y)
        {
            return node.execute(new Operation<Apfloat>()
            {
                public Apfloat execute()
                {
                    return x.add(y);
                }
            });
        }
    }

    /**
     * Class for calculating pi using the distributed Chudnovskys' binary splitting algorithm.
     */

    public static class DistributedChudnovskyPiCalculator
        extends ParallelChudnovskyPiCalculator
    {
        /**
         * Construct a pi calculator with the specified precision and radix.
         *
         * @param precision The target precision.
         * @param radix The radix to be used.
         */

        public DistributedChudnovskyPiCalculator(long precision, int radix)
            throws ApfloatRuntimeException
        {
            this(new DistributedBinarySplittingPiCalculator(new ChudnovskyBinarySplittingSeries(precision, radix)), precision, radix);
        }

        private DistributedChudnovskyPiCalculator(DistributedBinarySplittingPiCalculator calculator, long precision, int radix)
            throws ApfloatRuntimeException
        {
            super(calculator, precision, radix);
            this.calculator = calculator;
            this.precision = precision;
            this.radix = radix;
        }

        public Apfloat execute()
        {
            Pi.err.println("Using the Chudnovsky brothers' binary splitting algorithm");

            Node[] nodes = this.calculator.getNodes();

            if (nodes.length > 1)
            {
                Pi.err.println("Using up to " + nodes.length + " parallel operations for calculation");
            }

            final Apfloat f = new Apfloat(1823176476672000L, this.precision, this.radix);
            final ApfloatHolder T = new ApfloatHolder(),
                                Q = new ApfloatHolder(),
                                F = new ApfloatHolder(f);

            // Perform the calculation of T, Q and P to requested precision only, to improve performance

            long terms = (long) ((double) this.precision * Math.log((double) this.radix) / 32.65445004177);

            long time = System.currentTimeMillis();
            this.calculator.r(0, terms + 1, T, Q, null, F, nodes);
            time = System.currentTimeMillis() - time;

            Pi.err.println("Series terms calculation complete, elapsed time " + time / 1000.0 + " seconds");
            Pi.err.printf("Final value ");

            nodes = this.calculator.recombineNodes(nodes, 1);

            time = System.currentTimeMillis();
            Apfloat pi = nodes[nodes.length - 1].execute(new Operation<Apfloat>()
            {
                public Apfloat execute()
                {
                    Apfloat t = T.getApfloat(),
                            q = Q.getApfloat(),
                            factor = F.getApfloat();

                    if (factor == f)
                    {
                        factor = ApfloatMath.inverseRoot(f, 2);
                    }

                    return ApfloatMath.inverseRoot(factor.multiply(t), 1).multiply(q);
                }
            });
            time = System.currentTimeMillis() - time;

            Pi.err.println("took " + time / 1000.0 + " seconds");

            return pi;
        }

        private DistributedBinarySplittingPiCalculator calculator;
        private long precision;
        private int radix;
    }

    /**
     * Class for calculating pi using the distributed Ramanujan's binary splitting algorithm.
     */

    public static class DistributedRamanujanPiCalculator
        extends ParallelRamanujanPiCalculator
    {
        /**
         * Construct a pi calculator with the specified precision and radix.
         *
         * @param precision The target precision.
         * @param radix The radix to be used.
         */

        public DistributedRamanujanPiCalculator(long precision, int radix)
            throws ApfloatRuntimeException
        {
            this(new DistributedBinarySplittingPiCalculator(new RamanujanBinarySplittingSeries(precision, radix)), precision, radix);
        }

        private DistributedRamanujanPiCalculator(DistributedBinarySplittingPiCalculator calculator, long precision, int radix)
            throws ApfloatRuntimeException
        {
            super(calculator, precision, radix);
            this.calculator = calculator;
            this.precision = precision;
            this.radix = radix;
        }

        public Apfloat execute()
        {
            Pi.err.println("Using the Ramanujan binary splitting algorithm");

            Node[] nodes = this.calculator.getNodes();

            if (nodes.length > 1)
            {
                Pi.err.println("Using up to " + nodes.length + " parallel operations for calculation");
            }

            final Apfloat f = new Apfloat(8, this.precision, this.radix);
            final ApfloatHolder T = new ApfloatHolder(),
                                Q = new ApfloatHolder(),
                                F = new ApfloatHolder(f);

            // Perform the calculation of T, Q and P to requested precision only, to improve performance

            long terms = (long) ((double) this.precision * Math.log((double) this.radix) / 18.38047940053836);

            long time = System.currentTimeMillis();
            this.calculator.r(0, terms + 1, T, Q, null, F, nodes);
            time = System.currentTimeMillis() - time;

            Pi.err.println("Series terms calculation complete, elapsed time " + time / 1000.0 + " seconds");
            Pi.err.printf("Final value ");

            nodes = this.calculator.recombineNodes(nodes, 1);

            time = System.currentTimeMillis();
            Apfloat pi = nodes[nodes.length - 1].execute(new Operation<Apfloat>()
            {
                public Apfloat execute()
                {
                    Apfloat t = T.getApfloat(),
                            q = Q.getApfloat(),
                            factor = F.getApfloat();

                    if (factor == f)
                    {
                        factor = ApfloatMath.inverseRoot(f, 2);
                    }

                    return ApfloatMath.inverseRoot(t, 1).multiply(factor).multiply(new Apfloat(9801, Apfloat.INFINITE, DistributedRamanujanPiCalculator.this.radix)).multiply(q);
                }
            });
            time = System.currentTimeMillis() - time;

            Pi.err.println("took " + time / 1000.0 + " seconds");

            return pi;
        }

        private DistributedBinarySplittingPiCalculator calculator;
        private long precision;
        private int radix;
    }

    /**
     * RemoteOperationExecutor that implements the weight property.
     */

    protected static class Node
        extends RemoteOperationExecutor
        implements Comparable<Node>
    {
        /**
         * Construct a Node with the specified parameters and one processor.
         *
         * @param host The remote host.
         * @param port The remote port.
         * @param weight The weight.
         */

        public Node(String host, int port, int weight)
        {
            this(host, port, weight, 1);
        }

        /**
         * Construct a Node with the specified parameters.
         *
         * @param host The remote host.
         * @param port The remote port.
         * @param weight The weight.
         * @param numberOfProcessors The number of processors.
         */

        public Node(String host, int port, int weight, int numberOfProcessors)
        {
            super(host, port);
            this.weight = weight;
            this.numberOfProcessors = numberOfProcessors;
        }

        public <T> T execute(Operation<T> operation)
        {
            return super.execute(new ThreadLimitedOperation<T>(operation, this.numberOfProcessors));
        }

        public <T> BackgroundOperation<T> executeBackground(Operation<T> operation)
        {
            return super.executeBackground(new ThreadLimitedOperation<T>(operation, this.numberOfProcessors));
        }

        /**
         * Set the weight.
         *
         * @param weight The weight.
         */

        public void setWeight(int weight)
        {
            this.weight = weight;
        }

        public int getWeight()
        {
            return this.weight;
        }

        /**
         * Set the number of processors.
         *
         * @param numberOfProcessors The number of processors.
         */

        public void setNumberOfProcessors(int numberOfProcessors)
        {
            this.numberOfProcessors = numberOfProcessors;
        }

        /**
         * Get the number of processors.
         *
         * @return The number of processors.
         */

        public int getNumberOfProcessors()
        {
            return this.numberOfProcessors;
        }

        /**
         * Compare this Node to another Node.
         *
         * @param that The other node to compare to.
         *
         * @return A number less than zero if this Node should be ordered before the other node, or gerater than zero for the reverse order. Should not return zero.
         */

        public int compareTo(Node that)
        {
            // Must differentiate objects with same weight but that are not the same
            int weightDifference = this.weight - that.weight;
            return (weightDifference != 0 ? weightDifference : this.hashCode() - that.hashCode());      // This is not rock solid...
        }

        /**
         * Convert to String.
         *
         * @return The string representation.
         */

        public String toString()
        {
            return this.weight + "/" + this.numberOfProcessors;
        }

        private int weight;
        private int numberOfProcessors;
    }

    PiDistributed()
    {
    }

    /**
     * Command-line entry point.
     *
     * @param args Command-line parameters.
     *
     * @exception IOException In case writing the output fails.
     */

    public static void main(String[] args)
        throws IOException, ApfloatRuntimeException
    {
        if (args.length < 1)
        {
            System.err.println("USAGE: PiDistributed digits [method] [radix]");
            System.err.println("    radix must be 2...36");

            return;
        }

        long precision = getPrecision(args[0]);
        int method = (args.length > 1 ? getInt(args[1], "method", 0, 1) : 0),
            radix = (args.length > 2 ? getRadix(args[2]) : ApfloatContext.getContext().getDefaultRadix());

        Operation<Apfloat> operation;

        switch (method)
        {
            case 0:
                operation = new DistributedChudnovskyPiCalculator(precision, radix);
                break;
            default:
                operation = new DistributedRamanujanPiCalculator(precision, radix);
        }

        setOut(new PrintWriter(System.out, true));
        setErr(new PrintWriter(System.err, true));

        run(precision, radix, operation);
    }

    private static String formatArray(Object[] array)
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{ ");
        for (int i = 0; i < array.length; i++)
        {
            buffer.append(i == 0 ? "" : ", ");
            buffer.append(array[i]);
        }
        buffer.append(" }");
        return buffer.toString();
    }

    private static final int MIN_WEIGHT = 1,
                             MAX_WEIGHT = 1000;
    private static final boolean DEBUG = false;
}
