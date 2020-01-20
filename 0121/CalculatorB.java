import java.util.*;
import java.math.*;
import java.io.*;


class CalculatorB<Result> {

    private List<Command<Result>> commands;

    private String nextLine;

    private boolean interactive;

    void setInteractive(boolean _int) {
        interactive = _int;
    }

    boolean getInteractive() {
        return interactive;
    }

    CalculatorB(List<Command<Result>> commands) {
        this.commands = new ArrayList<Command<Result>> ();
        this.commands.add(new Return2<Result>());
        this.commands.addAll(commands);
        this.nextLine = null;
        this.interactive = true;
    }

    Result run(Result res, BufferedReader br) throws IOException {
        return run(res, br, true);
    }

    Result run(Result res, BufferedReader br, boolean showPrompt) throws IOException {
        boolean _int = interactive;
        String _nl = nextLine;
        nextLine = null;
        interactive = showPrompt;
        for(;;) {
            if(interactive) System.out.println(res);
            if(interactive) System.out.print(">>> ");
            String line = nextLine == null ? br.readLine() : nextLine;
            nextLine = null;
            if(line == null) break;
            if(line.equals("")) continue;
            Result newres = dispatch(res, line, br);
            if(newres == null) break;
            res = newres;
        }
        interactive = _int;
        nextLine = _nl;
        return res;
    }

    List<String> getBlock(BufferedReader br) throws IOException {
        ArrayList<String> lines = new ArrayList<String>();
        for(;;) {
            if(interactive) System.out.print("... ");
            nextLine = br.readLine();
            if(nextLine == null || nextLine.equals("") || nextLine.charAt(0) != '\t') {
                if(nextLine != null && nextLine.equals("")) nextLine = null;
                break;
            }
            lines.add(nextLine.substring(1));
        }
        if(lines.size() == 0) {
            throw new RuntimeException("an indented block is expected.");
        }
        return lines;
    }

    lt dispatch(Result res, String line, BufferedReader br) throws IOException {
        String [] tokens = tokenize(line);
        for(Command<Result> cmd : commands) {
            if(cmd.accept(tokens)) {
                if(cmd instanceof BlockCommand) {
                    ((BlockCommand<Result>)cmd).setBlock(getBlock(br), this);
                }
                return cmd.exec(res);
            }
        }

        throw new RuntimeException("unknown command: " + line);
    }

    String [] tokenize(String line) {

        return line.replaceAll("(\\W)"," $1 ").replaceAll("^\\s+","").split("\\s+");
    }
}


class Return2<Result> implements Command<Result> {

    public boolean accept(String [] tokens) {
        return tokens.length == 1 && tokens[0].equals("return");
    }


    public Result exec(Result res) {
        throw new ReturnFromFunction(res);
    }
}


class Comment<Result> implements Command<Result> {

    public boolean accept(String [] tokens) {
        return tokens.length >= 1 && tokens[0].equals("#");
    }

    public Result exec(Result res) {
        return res;
    }
}


class ReturnFromFunction extends RuntimeException {

    private Object res;

    ReturnFromFunction(Object res) {
        this.res = res;
    }

    Object getResult() {
        return res;
    }
}



interface BlockCommand<Result> extends Command<Result> {

    void setBlock(List<String> block, CalculatorB<Result> calc);
}


abstract class AbstractBlockCommand implements BlockCommand<BIwithMem> {

    CalculatorB<BIwithMem> calc;

    String blockStr;

    public void setBlock(List<String> block, CalculatorB<BIwithMem> calc) {
        this.blockStr = String.join("\n", block);
        this.calc = calc;
    }

    public BIwithMem runOnce(BIwithMem res) {

        try {
            res = calc.run(res, new BufferedReader(new StringReader(blockStr)), false);
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return res;
    }
}


class IfBIWM extends AbstractBlockCommand {

    public boolean accept(String [] tokens) {
        return tokens.length == 1 && tokens[0].equals("if");
    }

    public BIwithMem exec(BIwithMem res) {
        if(!res.getValue().equals(BigInteger.ZERO)) {
            res = runOnce(res);
        }
        return res;
    }
}

class WhileBIWM extends AbstractBlockCommand {

    public boolean accept(String [] tokens) {
        return tokens.length == 1 && tokens[0].equals("while");
    }

    public BIwithMem exec(BIwithMem res) {
        while(!res.getValue().equals(BigInteger.ZERO)) {
            res = runOnce(res);
        }
        return res;
    }
}


class BICalculatorWithBlock {
    public static void main(String [] args) throws Exception {
        boolean promptFlag = !(args.length > 0 && args[0].equals("--no-prompt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<Command<BIwithMem>> cmds = new ArrayList<Command<BIwithMem>>();
        cmds.add(new AddBIWM());
        cmds.add(new SubBIWM());
        cmds.add(new MulBIWM());
        cmds.add(new DivBIWM());
        cmds.add(new NegBIWM());
        cmds.add(new IfBIWM());  // block type
        cmds.add(new WhileBIWM());  // block type
        cmds.add(new DefunBIWM());  // block type
        cmds.add(new CallBIWM());
        cmds.add(new LoadBIWM());
        cmds.add(new StoreBIWM());
        cmds.add(new ShowVarsBIWM());
        cmds.add(new PrintVarBIWM());
        cmds.add(new Comment<BIwithMem>());
        cmds.add(new LoadImmBIWM());
        CalculatorB<BIwithMem> calc = new CalculatorB<BIwithMem>(cmds);
        BIwithMem biwm = new BIwithMemFunc();

        try {
            calc.run(biwm, br, promptFlag);
        } catch(ReturnFromFunction rff) {
        }
    }
}



class PrintVarBIWM implements Command<BIwithMem> {

    String x;

    public boolean accept(String [] tokens) {
        if(!(tokens.length == 2 && tokens[0].equals("print"))) {
            return false;
        }
        x = tokens[1];
        return true;
    }

    public BIwithMem exec(BIwithMem res) {
        System.out.println(res.load(x));
        return res;
    }
}


class Func<Result> {

    String bodyStr;

    List<String> args;

    CalculatorB<Result> calc;

    Func(List<String> args, String bodyStr, CalculatorB<Result> calc) {
        this.bodyStr = bodyStr;
        this.args = args;
        this.calc = calc;
    }

    String showArgs() {
        return String.join(" ", args);
    }
}

class BIwithMemFunc extends BIwithMem {

    private HashMap<String, Func<BIwithMem>> funcs;

    BIwithMemFunc() {
        super();
        funcs = new HashMap<String, Func<BIwithMem>>();
    }

    BIwithMemFunc(BIwithMemFunc biwmf) {
        super();
        funcs = new HashMap<String, Func<BIwithMem>>();
        funcs.putAll(biwmf.funcs);
    }
    void storeFunc(String x, Func<BIwithMem> f) {
        funcs.put(x, f);
    }

    Func<BIwithMem> loadFunc(String x) {
        Func<BIwithMem> f = funcs.get(x);
        if(f == null) {
            throw new RuntimeException("Unknown function: " + x);
        }
        return f;
    }

    public String dumpFuncs() {
        StringBuffer sb = new StringBuffer();
        for(Map.Entry<String, Func<BIwithMem>> e : funcs.entrySet()) {
            String var = e.getKey();
            Func f = e.getValue();
            sb.append(" " + var + " " + f.showArgs() + "\n");
        }
        return sb.toString();
    }
}


class DefunBIWM extends AbstractBlockCommand {

    String x;

    List<String> args;

    public boolean accept(String [] tokens) {
        if(!(tokens.length >= 2 && tokens[0].equals("def"))) {
            return false;
        }
        x = tokens[1];
        args = new ArrayList<String>();
        for(int i = 2; i < tokens.length; i++) {
            args.add(tokens[i]);
        }
        return true;
    }

    public BIwithMem exec(BIwithMem res) {
        ((BIwithMemFunc)res).storeFunc(x, new Func<BIwithMem>(args, blockStr, calc));
        return res;
    }
}

class CallBIWM extends BIorVarWM {

    String x;

    List<String> args;

    public boolean accept(String [] tokens) {
        if(!(tokens.length >= 2 && tokens[0].equals("call"))) {
            return false;
        }
        x = tokens[1];
        args = new ArrayList<String>();
        for(int i = 2; i < tokens.length; i++) {
            args.add(tokens[i]);
        }
        return true;
    }

    public BIwithMem exec(BIwithMem res) {
        Func<BIwithMem> f = ((BIwithMemFunc)res).loadFunc(x);

        if(f.args.size() != args.size()) {
            throw new RuntimeException("the number of arguments mismatches: expected " + f.args.size() + " but got " + args.size());
        }
        BIwithMemFunc biwmf = new BIwithMemFunc((BIwithMemFunc)res);
        BIwithMem biwm = biwmf;

        for(int i = 0; i < f.args.size(); i++) {
            biwm.store(f.args.get(i), parseOrLoad(res, args.get(i)));
        }
        boolean _int = false;
        try {
            _int = f.calc.getInteractive();
            biwm = f.calc.run(biwm, new BufferedReader(new StringReader(f.bodyStr)), false);
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(0);
        } catch(ReturnFromFunction rff) {
            biwm = (BIwithMem)rff.getResult();
        } finally {
            f.calc.setInteractive(_int);
        }
        res.setValue(biwm.getValue());
        return res;
    }
}