package vp.mom.activitys.FAQ1;

import java.util.ArrayList;

/**
 * Created by pallavi.b on 22-Jan-16.
 */
public class Question {

    private String questionName;

    private ArrayList<vp.mom.activitys.FAQ1.Answer> answers;

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public ArrayList<vp.mom.activitys.FAQ1.Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<vp.mom.activitys.FAQ1.Answer> answers) {
        this.answers = answers;
    }
}

