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
public class QuestionSubMenu extends LinearLayout {

    Context context;
    TextView textView;

    public QuestionSubMenu(Context context) {
        super(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.company_submenu,null);
        this.addView(view);
        textView  = (TextView) view.findViewById(R.id.txtsubmenu);
    }

    public void setQuestionSubMenuData(String emp){
        textView.setText(emp);
    }

}
