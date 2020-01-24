class SumAndFact {
    int sumUpTo(int n) {
        int ret = 0;
        for(int i = 0; i <= n; i++) {
            ret = ret + i;
        }
        return ret;
    }
    int fact(int n) {
        int ret = 1;
        for(int i = 1; i <= n; i++) {
            ret = ret * i;
        }
        return ret;
    }

    int common(int n, Op op) {
        int ret = op.initValue();
        for(int i = op.start(); i <= n; i++) {
            ret = op.operate(ret, i);
        }
        return ret;
    }
    int sumUpToByCommon(int n) {
        return common(n, new SumUpToOp());
    }
    int factByCommon(int n) {
        return common(n, new FactOp());
    }
    public static void main(String [] args) {
        SumAndFact saf = new SumAndFact();
        System.out.println("native   : " + saf.sumUpTo(10));
        System.out.println("by common: " + saf.sumUpToByCommon(10));
        System.out.println("native   : " + saf.fact(10));
        System.out.println("by common: " + saf.factByCommon(10));
    }
}

interface Op {
    int initValue();
    int start();
    int operate(int ret, int i);
}
class SumUpToOp implements Op {
    public int initValue() { return 0; }
    public int start() { return 0; }
    public int operate(int ret, int i) { return ret + i; }
}
class FactOp implements Op {
    public int initValue() { return 1; }
    public int start() { return 1; }
    public int operate(int ret, int i) { return ret * i; }
}