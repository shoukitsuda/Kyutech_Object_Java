import java.util.*;
public class DFSMazeSolver {
    public static <Pos> void solve(Maze<Pos> m) {

        HashMap<Pos, Pos> prev = new HashMap<Pos, Pos>();
        Stack<Pos> stack = new Stack<Pos>();
        HashSet<Pos> seen = new HashSet<Pos>();

        Pos st = m.getStart();
        stack.push(m.getStart());
        seen.add(m.getStart());

        while(stack.size() > 0) {
            Pos p = stack.pop();
            if(m.isGoal(p)) {
                ArrayList<Pos> ps = new ArrayList<Pos>();
                for(Pos c = p; c != null; c = prev.get(c)) {
                    ps.add(c);
                }
                Collections.reverse(ps);
                m.printAnswer(ps);
                return;
            }
            List<Pos> ns = m.getNeighbors(p);
            for( Pos n : ns ) {
                if(!seen.contains(n)) {
                    stack.push(n);
                    seen.add(n);
                    prev.put(n, p);
                }
            }
        }
        System.out.println("impossible");
    }
}