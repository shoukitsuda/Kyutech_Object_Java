import java.util.*;
import java.math.*;
import java.io.*;

class Calculator<Result> {

    private List<Command<Result>> commands;

    Calculator(List<Command<Result>> commands) {
        this.commands = new ArrayList<Command<Result>> ();
        // "return" �R�}���h��擪�ɒǉ�
        this.commands.add(new Return<Result>());
        this.commands.addAll(commands);
    }

    Result run(Result res, BufferedReader br) throws IOException {
        for(;;) {
            System.out.println(res); // ���݂̌��ʂ�\��
            System.out.print("> ");  // �R�}���h���󂯕t����v�����v�g��\��
            String line = br.readLine(); // �R�}���h��ǂݍ���
            if(line.equals("")) continue; // ��Ȃ牽�����Ȃ��ő�����
            Result newres = dispatch(res, line); // �R�}���h��U�蕪���Ď��s
            if(newres == null) return res; // �V�������ʂ��o�Ȃ������肵����I��
            res = newres;     // �V�������ʂɍX�V
        }
    }

    Result dispatch(Result res, String line) {//命令の検索
        // �s���g�[�N���ɕ�������
        String [] tokens = tokenize(line);//入力をトークンに分割
        // �e�R�}���h�ɑ΂��ăg�[�N�����^���A
        // accept ���ꂽ�Ȃ炻�̃R�}���h�����s���A���ʂ�Ԃ�
        for(Command<Result> cmd : commands) {//各コマンドについて
            if(cmd.accept(tokens)) {//対応する演算か？
                return cmd.exec(res);
            }
        }
        // �R�}���h�����s�ł��Ȃ������ꍇ�͂�����B
        // �ʓ|�Ȃ̂Ŏ��s����O�𓊂��Ă��܂��B
        throw new RuntimeException("unknown command: " + line);
    }

    String [] tokenize(String line) {
        // �蔲���H�@�L����1�����ɕ����B�A���t�@�x�b�g�Ɛ����ƃA���_�[�X�R�A�͂��̂܂ܘA���B
        // �L���Ȃǂ̑O��ɋ󔒂���ꂽ��ŋ󔒋�؂�ɂ���Ƃ��������Ŏ���
        return line.replaceAll("(\\W)"," $1 ").replaceAll("^\\s+","").split("\\s+");
    }
}


interface Command<Result> {

    //与えられたユーザ入力がこの演算に対応するか判定
    boolean accept(String [] tokens);

    //現在の結果resに対して演算を実行しその結果を返す
    Result exec(Result res);
}



class Return<Result> implements Command<Result> {

    public boolean accept(String [] tokens) {
        return tokens.length == 1 && tokens[0].equals("return");
    }

    public Result exec(Result res) {
        return null;
    }
}


class AddInt implements Command<BigInteger> {

    BigInteger x;

    public boolean accept(String [] tokens) {
        if(tokens.length < 2) return false;
        if(!tokens[0].equals("+")) return false;
        x = new BigInteger(tokens[1]);
        return true;
    }

    public BigInteger exec(BigInteger res) {

        return res.add(x);
    }
}



abstract class BinOpInt implements Command<BigInteger> {

    abstract String opeStr();

    BigInteger x;

    public boolean accept(String [] tokens) {
        if(tokens.length < 2) return false;       // ���������邩�H
        if(!tokens[0].equals(opeStr())) return false; // �ŏ������Z�q���H
        x = new BigInteger(tokens[1]);            // ������ǂݍ���
        return true;   // �����܂ŗ����� OK
    }
}

class SubInt extends BinOpInt {

    String opeStr() { return "-"; }

    public BigInteger exec(BigInteger res) {
        return res.subtract(x);
    }
}

class MulInt extends BinOpInt {

    String opeStr() { return "*"; }

    public BigInteger exec(BigInteger res) {
        return res.multiply(x);
    }
}


class DivInt extends BinOpInt {

    String opeStr() { return "/"; }

    public BigInteger exec(BigInteger res) {
        return res.divide(x);
    }
}

class NegInt implements Command<BigInteger> {
//    /**
//     * "neg" �Ƃ����s���󂯕t����B
//     * @param tokens ���͍s���g�[�N���ɕ����������́B
//     * @return ���͍s����̌`�Ȃ� {@code true}�B
//     */
    public boolean accept(String [] tokens) {
        return tokens.length == 1 && tokens[0].equals("neg");
    }
//    /**
//     * �������]�B
//     * @param res ���݂̒l�B
//     * @return �������]�����l�B
//     */
    public BigInteger exec(BigInteger res) {
        return res.negate();
    }
}


class LoadInt implements Command<BigInteger> {

    BigInteger x;

    public boolean accept(String [] tokens) {
        if(tokens.length != 1) return false;
        try {
            x = new BigInteger(tokens[0]);            // ������ǂݍ���
        } catch(Exception e) {
            return false; // �����ւ̕ϊ��Ɏ��s������Aaccept ���Ȃ��B
        }
        return true;   // �����܂ŗ����� OK
    }

    public BigInteger exec(BigInteger res) {
        return x;
    }
}

class FoultInt implements Command<BigInteger> {

    BigInteger x;

    public boolean accept(String [] tokens) {
        if(tokens.length < 2) return false;
        if(!tokens[0].equals("%")) return false;
        x = new BigInteger(tokens[1]);
        return true;
    }

    public BigInteger exec(BigInteger res) {
        return res.remainder(x);
    }
}

class PowInt extends BinOpInt {

    String opeStr() { return "^"; }

    public BigInteger exec(BigInteger res) {
        int i = x.intValue();
        return res.pow(i);
    }
}



class FactInt implements Command<BigInteger> {//文字だからcあめ


    public boolean accept(String [] tokens) {
        return tokens.length == 1 && tokens[0].equals("fact");
    }

    public BigInteger exec(BigInteger res) {
            BigInteger f = BigInteger.ONE;
            int n = res.intValue();
            for (int i = n ; i > 1 ;i--) {
                BigInteger j = BigInteger.valueOf(i);
                f = f.multiply(j);
            }
            res = f;
        return res;
    }
}

class BigIntegerCalculator {
    public static void main(String [] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<Command<BigInteger>> cmds = new ArrayList<Command<BigInteger>>();//オペレータのリスト
        cmds.add(new AddInt());
        cmds.add(new SubInt());
        cmds.add(new MulInt());
        cmds.add(new DivInt());
        cmds.add(new NegInt());
        cmds.add(new LoadInt());
        cmds.add(new FoultInt());
        cmds.add(new PowInt());
        cmds.add(new FactInt());
        Calculator<BigInteger> calc = new Calculator<BigInteger>(cmds);
        calc.run(BigInteger.ZERO, br);
    }
}