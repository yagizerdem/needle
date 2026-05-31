package Needle;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class NfaSimulation {

    private final NfaBuilder.NfaFragment graph;

    public NfaSimulation(NfaBuilder.NfaFragment graph) {
        this.graph = graph;
    }



    public boolean matches(String input) {
        Set<NfaBuilder.NfaNode> currentStates = epsClosure(graph.start);
        for(int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            Set<NfaBuilder.NfaNode> nextStates = new HashSet<>();
            for(NfaBuilder.NfaNode state : currentStates) {
                for(NfaBuilder.Transition t : state.transitions) {
                    if(t.accepts.contains(ch) || t.type == NfaBuilder.TransitionType.ANY) {
                        nextStates.add(t.to);
                    }
                }
            }

            currentStates.clear();
            // take eps closure of all next states
            for(NfaBuilder.NfaNode state : nextStates) {
                currentStates.addAll(epsClosure(state));
            }
        }

        return currentStates.contains(graph.end);
    }

    public Set<NfaBuilder.NfaNode> epsClosure(NfaBuilder.NfaNode start) {
        Set<NfaBuilder.NfaNode> closure = new HashSet<>();
        Stack<NfaBuilder.NfaNode> stack = new Stack<>();
        stack.push(start);
        while (!stack.isEmpty()) {
            NfaBuilder.NfaNode node = stack.pop();

            if (closure.contains(node)) {
                continue;
            }
            closure.add(node);
            for (NfaBuilder.Transition t : node.transitions) {
                if (t.type == NfaBuilder.TransitionType.EPS) {
                    stack.push(t.to);
                }
            }
        }
        return closure;
    }

}
