import java.util.*;
/**
 * DFSMazeSolver �� BFSMazeSolver �� solve �́C���ق��z�����邽�߂̃C���^�[�t�F�[�X�D
 * �[�I�ɂ́C���ق̐��������\�b�h���K�v�ł���Ƃ������ƁD
 * @param <E> �v�f�̌^�D
 */
interface MyCollection<E> {

    void init();
    void in(E p);
    E out();
    int size_count();
}

class MazeSolver {

    public static <Pos> void solve(Maze<Pos> m, MyCollection<Pos> collection) {
        HashMap<Pos, Pos> prev = new HashMap<Pos, Pos>();
        collection.init();
        HashSet<Pos> seen = new HashSet<Pos>();
        Pos st = m.getStart(); // �X�^�[�g�ʒu
        collection.in(m.getStart()); // �X�^�[�g�ʒu���L���[�ɒǉ�
        seen.add(m.getStart()); // �X�^�[�g�ʒu���L���[�ɓ��ꂽ�ƋL��
        while(collection.size_count() > 0) {
            Pos p = collection.out();
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
                    collection.in(n);
                    seen.add(n);
                    prev.put(n, p);
                }
            }
        }
    }
}

class MyStack<E> implements MyCollection<E> {
    Stack<E> st;
    MyStack() {
        st = null;
    }
    public void init() {
        st = new Stack<E>();
    }

    public void in(E s){
      st.push(s);
    }

    public E out(){
      return st.pop();
    }

    public int size_count(){
    return  st.size();
    }
}

class MyQueue<E> implements MyCollection<E> {
    LinkedList<E> que;
    MyQueue() {
        que = null;
    }
    public void init() {
        que = new LinkedList<E>();
    }

    public void in(E q){
      que.add(q);
    }

    public E out(){
      return que.remove();
    }

    public int size_count(){
      return  que.size();
    }
  }
