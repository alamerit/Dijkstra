package java_core;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

public class Main {
    private static final Graph.Edge[] GRAPH = {
            new Graph.Edge("Москва", "Санкт Питербург", 4),
            new Graph.Edge("Москва", "Казань", 4),
            new Graph.Edge("Москва", "Абакан", 3),
            new Graph.Edge("Абакан", "Кемерово", 3),
            new Graph.Edge("Санкт Питербург", "Красноярск", 5),
            new Graph.Edge("Красноярск", "Якутск", 4),
            new Graph.Edge("Якутск", "Новосибирск", 3),
            new Graph.Edge("Казань", "Краснодар", 5),
            new Graph.Edge("Краснодар", "Междуреченск", 4),
            new Graph.Edge("Междуреченск", "Новосибирск", 3),
            new Graph.Edge("Кемерово", "Новосибирск", 3),

    };
    private static final String START = "Москва";
    private static final String END = "Новосибирск";

    public static void main(String[] args) {
        Graph g = new Graph(GRAPH);
        g.dijkstra(START);
        g.printPath(END);
    }
}

class Graph {
    private final Map<String, Vertex> graph;

    static class Edge {
        final String v1, v2;
        final int dist;
        Edge(String v1, String v2, int dist) {
            this.v1 = v1;
            this.v2 = v2;
            this.dist = dist;
        }
    }


    public static class Vertex implements Comparable<Vertex> {
        final String name;
        int dist = Integer.MAX_VALUE;
        Vertex previous = null;
        final Map<Vertex, Integer> neighbours = new HashMap<>();

        Vertex(String name) {
            this.name = name;
        }

        private void printPath() {
            if (this == this.previous) {
                System.out.printf("%s", this.name);
            } else if (this.previous == null) {
                System.out.printf("%s(unreached)", this.name);
            } else {
                this.previous.printPath();
                System.out.printf(" -> %s(%d)", this.name, this.dist);
            }
        }

        public int compareTo(Vertex other) {
            return Integer.compare(dist, other.dist);
        }
    }


    Graph(Edge[] edges) {
        graph = new HashMap<>(edges.length);

        for (Edge e : edges) {
            if (!graph.containsKey(e.v1)) graph.put(e.v1, new Vertex(e.v1));
            if (!graph.containsKey(e.v2)) graph.put(e.v2, new Vertex(e.v2));
        }


        for (Edge e : edges) {
            graph.get(e.v1).neighbours.put(graph.get(e.v2), e.dist);

        }
    }

    void dijkstra(String startName) {
        if (!graph.containsKey(startName)) {
            System.err.printf("Graph doesn't contain start vertex \"%s\"\n", startName);
            return;
        }
        final Vertex source = graph.get(startName);
        NavigableSet<Vertex> q = new TreeSet<>();

        for (Vertex v : graph.values()) {
            v.previous = v == source ? source : null;
            v.dist = v == source ? 0 : Integer.MAX_VALUE;
            q.add(v);
        }

        dijkstra(q);
    }


    private void dijkstra(final NavigableSet<Vertex> q) {
        Vertex u, v;
        while (!q.isEmpty()) {

            u = q.pollFirst();
            if (u.dist == Integer.MAX_VALUE) break;
            for (Map.Entry<Vertex, Integer> a : u.neighbours.entrySet()) {
                v = a.getKey();

                final int alternateDist = u.dist + a.getValue();
                if (alternateDist < v.dist) {
                    q.remove(v);
                    v.dist = alternateDist;
                    v.previous = u;
                    q.add(v);
                }
            }
        }
    }


    void printPath(String endName) {
        if (!graph.containsKey(endName)) {
            System.err.printf("Graph doesn't contain end vertex \"%s\"\n", endName);
            return;
        }

        graph.get(endName).printPath();
        System.out.println();
    }


    }
