package vp.mom.activitys.FAQ1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import vp.mom.R;

/**
 * Created by pallavi.b on 22-Jan-16.
 */
public class QuestionMainMenu extends LinearLayout {
    Context context;
    TextView textView;

    vp.mom.activitys.FAQ1.Question company;

    public QuestionMainMenu(Context context) {
        super(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.company_main_menu,null);
        this.addView(view);
        textView  = (TextView) view.findViewById(R.id.txtMainMenu);
    }

    public void setQuestionMainData(String question){
        textView.setText(question);
    }
}

