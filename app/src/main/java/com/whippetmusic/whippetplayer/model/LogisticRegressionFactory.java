package com.whippetmusic.whippetplayer.model;


import android.content.Context;
import com.whippetmusic.whippetplayer.Constants;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class LogisticRegressionFactory {
    public static LogisticRegression getLogisticRegression(Context context) {
        File file = new File(context.getFilesDir(), Constants.LOGISTIC_WEIGHTS_FILENAME);

        if (file.exists()) {
            double[] weights = new double[Constants.WEIGHTS_LENGTH];
            try {
                DataInputStream dis = new DataInputStream(new BufferedInputStream(context.openFileInput(Constants.LOGISTIC_WEIGHTS_FILENAME)));
                for (int i = 0 ; i < Constants.WEIGHTS_LENGTH ; i++) {
                    weights[i] = dis.readDouble();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new LogisticRegression(weights, context);

        } else {
            LogisticRegression regression = new LogisticRegression(new double[Constants.WEIGHTS_LENGTH], context);
            regression.persist();
            return regression;
        }
    }
}
