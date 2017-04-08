import edu.rit.io.InStream;
import edu.rit.io.OutStream;
import edu.rit.pj2.Job;
import edu.rit.pj2.Rule;
import edu.rit.pj2.Task;
import static edu.rit.pj2.Task.terminate;
import edu.rit.pj2.Tuple;
import edu.rit.pj2.TupleSpace;
import edu.rit.pj2.TupleListener;
import edu.rit.pj2.tracker.JobProperties;
import edu.rit.pj2.tuple.EmptyTuple;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;


public class WaterJugParallel
        extends Job {

    public void main(String[] args)
            throws Exception {

        long st_Time = System.currentTimeMillis();
        System.out.println("Start time=" + st_Time);
        
        int bottleNum = 10;

        
        List<Integer> start = new ArrayList<>();
        List<List<Integer>> nodes = new ArrayList<>();
        List<List<Integer>> visitedNodes = new ArrayList<>();
        for (int i = 0; i < bottleNum; i++) {
            start.add(0);
        }
        nodes.add(start);
        visitedNodes.add(start);
        putTuple(new NodeTuple(nodes, visitedNodes));

        // Perform a search task each time there's a new work item.
        rule().whenMatch(new NodeTuple()).task(BFS.class)
                .args("" + bottleNum);

    }

    private static class NodeTuple
            extends Tuple {

        private List<List<Integer>> visitedNodes = new ArrayList<>();
        private List<List<Integer>> nodes = new ArrayList<>();

        
        public NodeTuple() {
        }

        public NodeTuple(List<List<Integer>> x, List<List<Integer>> w) {
            this.nodes = x;
            this.visitedNodes = w;

        }

        public void writeOut(OutStream stream) throws IOException {
            stream.writeObject(nodes);
            stream.writeObject(visitedNodes);
        }

        public void readIn(InStream stream) throws IOException {
            nodes = (List<List<Integer>>) stream.readObject();
            visitedNodes = (List<List<Integer>>) stream.readObject();
        }

    }

    private static class BFS
            extends Task {

        private int N;
        private int target_amount;
        volatile boolean found;

        private class State {

            private List<List<Integer>> state = new ArrayList<>();
            private final int[] size;
            private List<List<Integer>> visitedNodes = new ArrayList<>();
            private List<List<Integer>> nextState = new ArrayList<>();
            
            public State(List<List<Integer>> a, int[] b, List<List<Integer>> c) {
                state = a;
                size = b;
                visitedNodes = c;
            }

	    void check(List<Integer> step){
		if (step.contains(target_amount)) {
                	System.out.println(step);
                        System.out.println("Finish Time=" + System.currentTimeMillis());
                        terminate(0);
                }else if (!visitedNodes.contains(step)){
			visitedNodes.add(step);
			nextState.add(step);
		}	
	    }
 
	    //Transfer the bottle	
            void transfer(List<Integer> node, int from, int to) {
                int transfer_amount;
                List<Integer> step = new ArrayList(node);
                transfer_amount = Math.min(step.get(from), size[to] - step.get(to));
                step.set(to, step.get(to) + transfer_amount);
                step.set(from, step.get(from) - transfer_amount);
		check(step);	
            }

            //Fill the bottle
            void fill(List<Integer> node, int jug) {
                List<Integer> step = new ArrayList(node);
                step.set(jug, size[jug]);
		check(step);
            }

            //Pour the bottle
            void pour(List<Integer> node, int jug) {
                List<Integer> step = new ArrayList(node);
                step.set(jug, 0);
		check(step);		
            }

            State calcGoal() throws IOException {
                List<Integer> step = new ArrayList<>();
                found = targetfound(state);

                if (!found) {
                    for (int k = 0; k < state.size(); k++) {
                        List<Integer> temp = new ArrayList<>(state.get(k));
                        //If first bottle is empty, fill it
                        for (int i = 0; i < N; i++) {
                            fill(temp, i);
                        }
                        //if first bottle is not empty and last bottle is not full, transfer water
                        for (int i = 0; i < N; i++) {
                            for (int j = 0; j < N; j++) {
                                if (i != j) {
                                    transfer(temp, i, j);
                                }
                            }
                        }
                        //if first bottle is not pour but last bottle is full, pour last bottle
                        for (int i = 0; i < N; i++) {
                            pour(temp, i);
                        }
                    }

                    if (targetfound(state)) {
                        putTuple(new EmptyTuple());
                        terminate(0);
                    } else {
                        int n = 0;
                        for (int i = 0; i < (nextState.size() / 20) + 1; i += 20) {
                            if (i + 20 > nextState.size()) {
                                n = nextState.size();
                            } else {
                                n = i + 20;
                            }
                            List<List<Integer>> pass = new ArrayList<>(nextState.subList(i, n));
                            putTuple(new NodeTuple(pass, visitedNodes));
                        }
                    }

                } else {
                    putTuple(new EmptyTuple());
                    System.out.println("End time=" + System.currentTimeMillis());
                    terminate(0);
                }
                return null;

            }
        }

        public static int[] getIntegers(String numbers) {
            StringTokenizer st = new StringTokenizer(numbers, ",");
            int[] intArr = new int[st.countTokens()];
            int i = 0;
            while (st.hasMoreElements()) {
                intArr[i] = Integer.parseInt((String) st.nextElement());
                i++;
            }
            return intArr;
        }

        boolean targetfound(List<List<Integer>> graph) {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < graph.size(); i++) {
                list = graph.get(i);
                if (list.contains(target_amount)) {
                    return true;
                }
            }
            return false;
        }

        public void main(String[] args) throws Exception {

            N = 10; //bottleNum
            target_amount = 40; //goal
            int[] size = {3,5,7,13,19,29,31,37,39,41}; //bottleCapacity

            // Early loop exit
            addTupleListener(new TupleListener<EmptyTuple>(new EmptyTuple()) {
                public void run(EmptyTuple tuple) {
                    found = true;
                }
            });

            NodeTuple tuple = (NodeTuple) getMatchingTuple(0);
            State x = new State(tuple.nodes, size, tuple.visitedNodes).calcGoal();

        }

        protected static int coresRequired() {
            return 1;
        }
    }
}
