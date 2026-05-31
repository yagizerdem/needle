package Needle;

import java.util.*;

public class NfaFragmentPrinter {

    private final Map<NfaBuilder.NfaNode, Integer> nodeIds = new LinkedHashMap<>();
    private int idCounter = 0;

    public void print(NfaBuilder.NfaFragment fragment) {
        discover(fragment.start);

        int startId   = nodeIds.get(fragment.start);
        int endId     = nodeIds.get(fragment.end);
        int totalNodes = nodeIds.size();

        System.out.println();
        System.out.println("+--------------------------------------------------------+");
        System.out.printf( "|  NFA  start=N%-3d  accept=N%-3d  nodes=%-4d            |%n",
                startId, endId, totalNodes);
        System.out.println("+--------------------------------------------------------+");
        System.out.println("|  NODES                                                 |");
        System.out.println("+--------------------------------------------------------+");

        List<NfaBuilder.NfaNode> nodes = new ArrayList<>(nodeIds.keySet());
        for (NfaBuilder.NfaNode node : nodes) {
            int id = nodeIds.get(node);
            String role;
            if      (node == fragment.start && node == fragment.end) role = "START + ACCEPT";
            else if (node == fragment.start)                         role = "START         ";
            else if (node == fragment.end)                           role = "ACCEPT        ";
            else                                                     role = "              ";

            String line = String.format("|  N%-3d  %s  out-degree: %-3d",
                    id, role, node.transitions.size());
            System.out.printf("%-57s|%n", line);
        }

        System.out.println("+--------------------------------------------------------+");
        System.out.println("|  TRANSITIONS                                           |");
        System.out.println("+--------------------------------------------------------+");

        for (NfaBuilder.NfaNode node : nodes) {
            for (NfaBuilder.Transition t : node.transitions) {
                int fromId = nodeIds.get(t.from);
                int toId   = nodeIds.getOrDefault(t.to, -1);
                String label = transitionLabel(t);
                String line  = String.format("|  N%-3d  --%-13s-->  N%-3d", fromId, label, toId);
                System.out.printf("%-57s|%n", line);
            }
        }

        System.out.println("+--------------------------------------------------------+");
        System.out.println();
    }

    private void discover(NfaBuilder.NfaNode start) {
        Queue<NfaBuilder.NfaNode> queue = new LinkedList<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            NfaBuilder.NfaNode node = queue.poll();
            if (nodeIds.containsKey(node)) continue;
            nodeIds.put(node, idCounter++);
            for (NfaBuilder.Transition t : node.transitions) {
                if (!nodeIds.containsKey(t.to)) {
                    queue.add(t.to);
                }
            }
        }
    }

    private String transitionLabel(NfaBuilder.Transition t) {
        return switch (t.type) {
            case EPS  -> "[eps]        ";
            case ANY  -> "[any]        ";
            case CHAR -> {
                if (t.accepts.isEmpty()) yield "[empty]      ";
                if (t.accepts.size() == 1) yield String.format("['%c']        ", t.accepts.get(0));
                if (t.accepts.size() <= 6) {
                    StringBuilder sb = new StringBuilder("[");
                    for (char c : t.accepts) sb.append(c);
                    sb.append("]");
                    yield String.format("%-13s", sb);
                }
                yield String.format("[...(%d chars)]", t.accepts.size());
            }
        };
    }
}