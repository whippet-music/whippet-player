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
    private double lambda;
    private String[] attributes = {
            "Bias",
            "year",
            "artistFamiliarity",
            "artistHotness",
            "artistLatitude",
            "artistLongitude",
            "duration",
            "endOfFadeIn",
            "key",
            "keyConfidence",
            "loudness",
            "mode",
            "modeConfidence",
            "songHotness",
            "startOfFadeOut",
            "tempo",
            "timeSignature",
            "timeSignatureConfidence"
    };

    public LogisticRegression(double[] weights, Context context) {
        this.weights = weights;
        this.context = context;
        this.rate = 0.005;
        this.lambda = 0.7;

    }

    public void trainMany(float[][] x, int[] labels) {
        for(int i = 0 ; i < labels.length ; i++) {
            trainOnExample(x[i], labels[i], false);
        }

        persist();
    }

    public void clearWeights() {
        this.weights = new double[Constants.WEIGHTS_LENGTH];
        persist();
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
        for (int i = 0 ; i < weights.length ; i++)  {
            logit += weights[i] * x[i];
        }
        return sigmoid(logit);
    }

    public void train(float[] featureVector, int label) {
        trainOnExample(featureVector, label, true);
    }

    public void trainOnExample(float[] featureVector, int label, boolean save) {
        double predicted = classify(featureVector);
        for (int j = 0; j < weights.length; j++) {
            double change = (predicted - label) * featureVector[j];
            if (j != 0) {
                change +=  lambda * weights[j];
            }
            weights[j] = weights[j] - rate * change;
        }

        if (save) {
            persist();
        }

    }

    public void logWeights() {
        for(int i = 0 ; i < weights.length ; i++) {
            System.out.println(attributes[i] + ": " + weights[i]);
        }
    }

    private static double sigmoid(double z) {
        return 1.0 / (1.0 + Math.exp(-z));
    }
}
