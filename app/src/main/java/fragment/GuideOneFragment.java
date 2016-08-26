package fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gxl.photofinishing.R;

import customview.MCVideoView;

public class GuideOneFragment extends Fragment{
	private MCVideoView videoView;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_guideone, null, false);
	//	initView(view);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(videoView!=null){
			videoView.playVideo(getActivity(), Uri.parse("android.resource://"+getActivity().getPackageName()+"/"+R.raw.guide_1));
		}
	}

	private void initView(View view) {
		videoView=(MCVideoView) view.findViewById(R.id.videoView);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(videoView!=null){
			videoView.stopPlayback();
		}
	}
}
