package com.qingzu.fragment;

import com.qingzu.uitest.view.RoundImageView;
import com.qingzu.waterproof_work.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class TeamMemberDetailbasciInfoFragment extends Fragment implements OnClickListener {
	
private View v = null;
private  TextView tvJobExperience=null;//工作经验
private TextView tvProfessionalSkill=null;//专业技能
private TextView tvInsurance=null;//有无保险
private TextView tvCertification=null;//有无技工证
private  RoundImageView rivTeamMemberImg=null;//头像
private TextView tvTeamMemberName=null;//队员昵称
private  TextView tvJieDanNumber=null;//接单次数
private  RatingBar ratingTeamMemberAssess=null;//小星星
private TextView tvTeamMemberPoints=null;//分数
private TextView tvTeamMemberWorkPlace=null;//工作地点
private  ImageView imgTeamMemberDetail=null;//地图
private Button btTeamMemberMakingCalls=null;//拨打电话
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_team_member_detail_basic_info, container, false);

		initView();
		return v;
	}

	private void initView() {
		// TODO Auto-generated method stub
		 tvJobExperience=(TextView) v.findViewById(R.id.team_member_job_experience_tv);
		 tvProfessionalSkill=(TextView) v.findViewById(R.id.team_member_professional_skill_tv);
		 tvInsurance=(TextView) v.findViewById(R.id.team_member_insurance_tv);
		 tvCertification=(TextView) v.findViewById(R.id.team_member_certification_tv);
		 rivTeamMemberImg=(RoundImageView) v.findViewById(R.id.riv_team_member_img);
		 tvTeamMemberName=(TextView) v.findViewById(R.id.team_member_name_tv);
		 tvJieDanNumber=(TextView) v.findViewById(R.id.team_member_jie_dan_number_tv);
		 ratingTeamMemberAssess=(RatingBar) v.findViewById(R.id.team_member_detail_assess_ratingbar);
		 tvTeamMemberPoints=(TextView) v.findViewById(R.id.team_member_points_tv);
		 tvTeamMemberWorkPlace=(TextView) v.findViewById(R.id.team_member_work_place_tv);
		 imgTeamMemberDetail=(ImageView) v.findViewById(R.id.team_member_detail_img);
		 btTeamMemberMakingCalls=(Button) v.findViewById(R.id.team_member_making_calls_bt);
		 
		 btTeamMemberMakingCalls.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
