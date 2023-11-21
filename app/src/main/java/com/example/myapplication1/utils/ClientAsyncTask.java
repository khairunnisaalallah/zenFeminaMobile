package com.example.myapplication1.utils;

import android.os.AsyncTask;

import com.example.myapplication1.TimeFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

    public class ClientAsyncTask extends AsyncTask<String, String, String> {
        private static final int CONNECTON_TIMEOUT_MILLISECONDS = 60000;
        private final TimeFragment mContext;
        private final OnPostExecuteListener mPostExecuteListener;

        public interface OnPostExecuteListener {
            void onPostExecute(String result);
        }

        public ClientAsyncTask(TimeFragment context, OnPostExecuteListener postExecuteListener) {
            mContext = context;
            mPostExecuteListener = postExecuteListener;
        }

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(CONNECTON_TIMEOUT_MILLISECONDS);
                urlConnection.setReadTimeout(CONNECTON_TIMEOUT_MILLISECONDS);

                return streamToString(urlConnection.getInputStream());
            } catch (IOException ex) {
                // Handle exceptions
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mPostExecuteListener.onPostExecute(result);
        }

        private String streamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder result = new StringBuilder();

            try {
                do {
                    line = bufferReader.readLine();
                    if (line != null) {
                        result.append(line);
                    }
                } while (line != null);
                inputStream.close();
            } catch (IOException ex) {
                // Handle exceptions
            }

            return result.toString();
        }
    }

