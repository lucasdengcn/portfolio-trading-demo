/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.market.utils;

/**
 * https://en.wikipedia.org/wiki/Cumulative_distribution_function
 * https://en.wikipedia.org/wiki/Normal_distribution#Cumulative_distribution_function
 */
public class Norm {

    static double a1 = 0.254829592;
    static double a2 = -0.284496736;
    static double a3 = 1.421413741;
    static double a4 = -1.453152027;
    static double a5 = 1.061405429;
    static double p = 0.3275911;

    public static double erf(double x) {
        int sign = (x >= 0) ? 1 : -1;
        x = Math.abs(x);
        double t = 1 / (1 + p * x);
        double y = 1 - (((((a5 * t + a4) * t) + a3) * t + a2) * t + a1) * t * Math.exp(-x * x);
        return (1 + sign * y) / 2;
    }

    public static double cdf(double x) {
        return (1 + erf(x / Math.sqrt(2))) / 2;
    }

    public static double standardDeviation(double[] values) {
        double mean = mean(values);
        double sum = 0;
        for (double value : values) {
            sum += Math.pow(value - mean, 2);
        }
        return Math.sqrt(sum / values.length);
    }

    public static double mean(double[] values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.length;
    }
}
