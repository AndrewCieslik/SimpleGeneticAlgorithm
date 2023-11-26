public class Function {
    private double fx;
    private int a;
    private int b;
    private int c;

    Function(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    double fx(double x) {
        fx = a * x * x + b * x + c;
        return fx;
    }
}
