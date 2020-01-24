class TextMazeRunner2 {
    public static void main(String [] args) throws Exception {
        if(args.length < 1) {
            System.out.println("java TextMazeRunner2 mazefile [-b | -d]");
            System.out.println("  -b : using BFS (default)");
            System.out.println("  -d : using DFS");
            return;
        }
        boolean bfs = true;
        if(args.length >= 2) {
            if(args[1].equals("-d")) bfs = false;
        }

        TextMaze tm = new TextMaze(args[0]);
        if(bfs) {
            MazeSolver.solve(tm, new MyQueue<IntPair>());
        } else {
            MazeSolver.solve(tm, new MyStack<IntPair>());
        }
    }
}