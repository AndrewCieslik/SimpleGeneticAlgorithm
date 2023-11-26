public class Function {
    int c;
    private int fx;
    private int x;
    private int a;
    private int b;
    private int c;

    Function(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    static int fxPositive(int x) {
        fx = a * Math.pow(x, 2) + b * x + c;
        if (fx < 0) {
            fx = fx - fx + 1;   //+1 don't kill the weakest one
        }
        return fx;
    }

    int fx(int x) {
        fx = a * Math.pow(x, 2) + b * x + c;
        return fx;
    }
}
