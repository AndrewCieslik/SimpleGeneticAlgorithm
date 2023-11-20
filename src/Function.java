public class Function {
    static double fx, x;
    static int a = 1;
    static int b = -250;
    static int c = 10000;

    static double fxPositive(double x) {
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
