package vp.mom.activitys.FAQ1;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

/**
 * Created by pallavi.b on 22-Jan-16.
 */
public class QuestionThread extends AsyncTask<Object,Object,ArrayList<vp.mom.activitys.FAQ1.Question>> {


    Context context;
    Handler handler;

    ProgressDialog dialog;

    public QuestionThread(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }

    @Override
    protected ArrayList<vp.mom.activitys.FAQ1.Question> doInBackground(Object... params) {
        return WebUtil1.getQuestionData();
    }


    @Override
    protected void onPostExecute(ArrayList<vp.mom.activitys.FAQ1.Question> questions) {
        super.onPostExecute(questions);

        dialog.dismiss();

        Message message=new Message();
        message.obj = questions;
        handler.handleMessage(message);

    }
}
