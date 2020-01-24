import java.util.*;
class Divisors {

    Integer numDivisors(int n) {
        int cnt = 0;
        for(int i = 2; i <= n; i++) {
            if(n % i == 0) cnt++;
        }
        return Integer.valueOf(cnt);
    }
    List<Integer> allDivisors(int n) {
        ArrayList<Integer> ds = new ArrayList<Integer>();
        for(int i = 2; i <= n; i++) {
            if(n % i == 0) ds.add(i);
        }
        return ds;
    }

    void common(int n, OpD op) {
        op.init();
        for(int i = 2; i <= n; i++) {
            if(n % i == 0) op.add(i);
        }
    }
    int numDivisorsByCommon(int n) {
        NumDivisorsOp op = new NumDivisorsOp();
        common(n, op);
        return op.getValue();
    }
    List<Integer> allDivisorsByCommon(int n) {
        AllDivisorsOp op = new AllDivisorsOp();
        common(n, op);
        return op.getValue();
    }

    static <E> String l2s(List<E> es) {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        boolean fst = true;
        for(E e : es) {
            sb.append((fst ? "" : ", ") + e);
            fst = false;
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String [] args) {
        Divisors d = new Divisors();

        System.out.println("native   : " + d.numDivisors(100));
        System.out.println("by common: " + d.numDivisorsByCommon(100));
        System.out.println("native   : " + l2s(d.allDivisors(100)));
        System.out.println("by common: " + l2s(d.allDivisorsByCommon(100)));
    }
}

interface OpD<E> {
    void init();
    void add(int i);
    E getValue();
}

class NumDivisorsOp implements OpD<Integer> {
    int cnt;
    public void init() { cnt = 0; }
    public void add(int i) { cnt++; }
    public Integer getValue() { return Integer.valueOf(cnt); }
}

class AllDivisorsOp implements OpD<List<Integer>> {
    ArrayList<Integer> ds;
    public void init() { ds = new ArrayList<Integer>(); }
    public void add(int i) { ds.add(i); }
    public List<Integer> getValue() { return ds; }
}