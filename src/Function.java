public class Function {
    int c;
    private int fx;
    private int x;
    private int a;
    privte
    private int b;

    static double fxPositive(int x) {
        fx = a * Math.pow(x, 2) + b * x + c;
        if (fx < 0) {
            fx = fx - fx + 1;   //+1 don't kill the weakest one
        }
        return fx;
    }

    static double fx(double x) {
        fx = a * Math.pow(x, 2) + b * x + c;
        return fx;
    }

}
