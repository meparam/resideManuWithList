package vp.mom.activitys.FAQ1;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by pallavi.b on 22-Jan-16.
 */
public class WebUtil1 {


    public static ArrayList<vp.mom.activitys.FAQ1.Question> getQuestionData() {

        final String API_URL = vp.mom.api.AppConfig.FAQ_QUESTION;


        try {

            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            //InputStream stream = conn.getInputStream();


            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));

            //read responce
            StringBuffer response = new StringBuffer();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + "\n");
            }
            in.close();

            //JSON Parsing
            JSONObject jObj = new JSONObject(response.toString());
            JSONArray jArray = jObj.getJSONArray("items");
            ArrayList<vp.mom.activitys.FAQ1.Question> questions = new ArrayList<vp.mom.activitys.FAQ1.Question>();

            for (int i = 0; i < jArray.length(); i++) {

                //company Data
                vp.mom.activitys.FAQ1.Question question = new vp.mom.activitys.FAQ1.Question();
                JSONObject jQuestion = jArray.getJSONObject(i);
                question.setQuestionName(jQuestion.getString("question"));


                Log.d("Test", "Company Name is : " + jQuestion.getString("question"));

                ArrayList<vp.mom.activitys.FAQ1.Answer> answers = new ArrayList<>();
                vp.mom.activitys.FAQ1.Answer answer = new vp.mom.activitys.FAQ1.Answer();
                JSONObject jAnswerData = jArray.getJSONObject(i);
                answer.setAnswerName(jAnswerData.getString("answer"));
                Log.d("Test", "Emp Name is : " + jAnswerData.getString("answer"));

                answers.add(answer);
                question.setAnswers(answers);


                questions.add(question);
            }

            return questions;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



}
