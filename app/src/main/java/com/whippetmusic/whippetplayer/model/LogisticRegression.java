package com.whippetmusic.whippetplayer.model;

import android.content.Context;

import com.whippetmusic.whippetplayer.Constants;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LogisticRegression {
    private double[] weights;
    private Context context;
    private double rate;

    public LogisticRegression(double[] weights, Context context) {
        this.weights = weights;
        this.context = context;
        this.rate = 0.001;
    }

    public void persist() {
        try {
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(context.openFileOutput(Constants.LOGISTIC_WEIGHTS_FILENAME, Context.MODE_PRIVATE)));
            for (int i = 0 ; i < Constants.WEIGHTS_LENGTH ; i++) {
                dos.writeDouble(this.weights[i]);
                dos.flush();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double classify(float[] x) {
        double logit = 0.0;
        for (int i = 0 ; i< weights.length ; i++)  {
            logit += weights[i] * x[i];
        }
        return sigmoid(logit);
    }

    public void train(float[] featureVector, int label) {
        double predicted = classify(featureVector);
        for (int j = 0 ; j < weights.length ; j++) {
            weights[j] = weights[j] + rate * (label - predicted) * featureVector[j];
        }
        persist();
    }

    public void logWeights() {
        for(double weight : weights) {
            System.out.println(weight);
        }
    }

    private static double sigmoid(double z) {
        return 1.0 / (1.0 + Math.exp(-z));
    }
}
