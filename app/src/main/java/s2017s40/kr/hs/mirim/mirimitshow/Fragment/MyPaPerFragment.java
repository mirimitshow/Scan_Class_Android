package s2017s40.kr.hs.mirim.mirimitshow.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import s2017s40.kr.hs.mirim.mirimitshow.Group;
import s2017s40.kr.hs.mirim.mirimitshow.MyPaPerDTO;
import s2017s40.kr.hs.mirim.mirimitshow.PaPerAdapter;
import s2017s40.kr.hs.mirim.mirimitshow.R;
import s2017s40.kr.hs.mirim.mirimitshow.Register;
import s2017s40.kr.hs.mirim.mirimitshow.ScanClassActivity;
import s2017s40.kr.hs.mirim.mirimitshow.Services;
import s2017s40.kr.hs.mirim.mirimitshow.Utils;
import s2017s40.kr.hs.mirim.mirimitshow.ViewBoardActivity;


public class MyPaPerFragment extends Fragment {
    public MyPaPerFragment() {
        // Required empty public constructor
    }
    private Services service;
    SharedPreferences sharedPreference;
    public  String email;
    Utils utils = new Utils();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MyPaPerDTO> myDataset;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_paper, container, false);

        SharedPreferences sharedPreference = getContext().getSharedPreferences("email", Activity.MODE_PRIVATE);
        email = sharedPreference.getString("email","defValue");


        mRecyclerView = (RecyclerView) view.findViewById(R.id.paper_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        myDataset = new ArrayList<>();
        //어탭터
        mAdapter = new PaPerAdapter(myDataset, new PaPerAdapter.ClickCallback() {
            @Override
            public void onItemClick(int position) {
                //클릭 이벤트
                Intent i = new Intent(getActivity(), ViewBoardActivity.class);
                i.putExtra("Category",myDataset.get(position).getTitle());
                i.putExtra("position", position);
                startActivity(i);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        service = utils.mRetrofit.create(Services.class);
        Call<Register> call = service.getuser(email);
        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if (response.code() == 200) {
                    Register user = response.body();
                  /*  if(!user.getCategory().isEmpty()){
                        for(int i = 0; i < user.getCategory().size(); i++){
                            myDataset.add(new MyPaPerDTO(user.getCategory().get(i).getName(),"0"));
                        }
                    }*/
                    Toast.makeText(getContext(), "returns user", Toast.LENGTH_LONG).show();
                }else if(response.code() == 400){
                    Toast.makeText(getContext(), "nvalid input, object invalid", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(getContext(), "정보받아오기 실패", Toast.LENGTH_LONG).show();
                Log.e("getuserError", t.toString());
            }
        });

       /* myDataset.add(new MyPaPerDTO("수학","100장"));
        myDataset.add(new MyPaPerDTO("국어","10장"));
        myDataset.add(new MyPaPerDTO("NMT","1장"));
        myDataset.add(new MyPaPerDTO("가통","20장"));*/

        return view;
    }
}
