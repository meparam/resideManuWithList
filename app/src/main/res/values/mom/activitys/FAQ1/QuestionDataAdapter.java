package vp.mom.activitys.FAQ1;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;

/**
 * Created by pallavi.b on 22-Jan-16.
 */
public class QuestionDataAdapter extends BaseExpandableListAdapter {



    private Context context;
    private ArrayList<Question> list;
    private  ArrayList<Answer> answerList;

    public QuestionDataAdapter(Context context, ArrayList<Question> list) {
        this.context = context;
        this.list = list;
        //this.empList = emp;
    }

    @Override
    public int getGroupCount() {

        //parent count of expandable list view.
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).getAnswers().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getAnswers().get(childPosition);
        //this.empList.get(this.list.get(groupPosition)).get(childPosition);

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //method used to set data to compound view of parent item..
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {


        String questionName  =((Question)getGroup(groupPosition)).getQuestionName();

        QuestionMainMenu mainMenu;

        if (convertView==null){

            mainMenu = new QuestionMainMenu(context);

        }else {

            mainMenu = (QuestionMainMenu) convertView;
        }

        mainMenu.setQuestionMainData(questionName);

        return mainMenu;
    }


    //method used to set data to compound view of CHild item..
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String answerName = ((Answer)getChild(groupPosition,childPosition)).getAnswerName();
        QuestionSubMenu subMenu;
        if (convertView==null){
            subMenu = new QuestionSubMenu(context);
        }else {
            subMenu = (QuestionSubMenu) convertView;
        }

        subMenu.setQuestionSubMenuData(answerName);
        return subMenu;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}


