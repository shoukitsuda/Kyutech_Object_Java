import java.util.*;
public class BFSMazeSolver {
    public static <Pos> void solve(Maze<Pos> m) {
        HashMap<Pos, Pos> prev = new HashMap<Pos, Pos>();
        LinkedList<Pos> que = new LinkedList<Pos>();
        HashSet<Pos> seen = new HashSet<Pos>();
        Pos st = m.getStart();
        que.add(m.getStart());
        seen.add(m.getStart());
        while(que.size() > 0) {
            Pos p = que.remove();
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
                    que.add(n);
                    seen.add(n);
                    prev.put(n, p);
                }
            }
        }
        System.out.println("impossible");
    }
}