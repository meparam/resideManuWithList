package vp.mom.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vp.mom.R;
import vp.mom.activitys.ExtraTermsAndConditions;
import vp.mom.activitys.FAQ1.FAQ1MainActivity;
import vp.mom.activitys.PrivacyPolicy;
import vp.mom.activitys.ReportActivity;
import vp.mom.activitys.ReportTransactionProblem;
import vp.mom.activitys.UserGuide;

/**
 * Created by ApkDev2 on 06-11-2015.
 */
public class HelpFragment extends Fragment {
    private TextView txtAskedQuestions,txtTermsAndConditions,txtPrivacyPolicy,txtReportProblem,reportTransactionProblem,txtUserGuide;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.help_xml, container, false);
        txtAskedQuestions= (TextView) rootView.findViewById(R.id.txtAskedQuestions);
        txtTermsAndConditions= (TextView) rootView.findViewById(R.id.txtTermsAndConditions);
        txtPrivacyPolicy= (TextView) rootView.findViewById(R.id.txtPrivacyPolicy);
        txtReportProblem= (TextView) rootView.findViewById(R.id.txtReportProblem);
        reportTransactionProblem= (TextView) rootView.findViewById(R.id.reportTransactionProblem);
        txtUserGuide= (TextView) rootView.findViewById(R.id.txtUserGuide);
        txtAskedQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), FAQ1MainActivity.class);
                startActivity(intent);
            }
        });
        txtTermsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ExtraTermsAndConditions.class);
                startActivity(intent);
            }
        });

        txtPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), PrivacyPolicy.class);
                startActivity(intent);
            }
        });

        txtReportProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ReportActivity.class);
                startActivity(intent);
            }
        });

        reportTransactionProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportTransactionProblem.class);
                startActivity(intent);
            }
        });

        txtUserGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserGuide.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

}