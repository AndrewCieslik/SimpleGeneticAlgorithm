public class Function {
    private int fx;
    private int a;
    private int b;
    private int c;

    Function(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    int fx(int x) {
        fx = a * x * x + b * x + c;
        return fx;
    }
}
