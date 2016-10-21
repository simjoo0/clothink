package clothing.plus.think.clothink;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {


    ExpandableListView expandableListView;
    int threadStopFlag=1;
    int n=0;
    String str1="흰옷은 흰옷끼리";
    String str2="검은옷은 검은옷끼리";
    String str3="빨간옷은 빨간옷끼리";
    String str4="노란옷은 노란옷끼리";

    private ArrayList<String> arrayGroup=new ArrayList<String>();;
    private HashMap<String, ArrayList<String>> arrayChild=new HashMap<String, ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expandableListView=(ExpandableListView) findViewById(R.id.expandableListView1);

        threadStopFlag=0;
        new Thread(threadRun).start();

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                try {
                    threadStopFlag=1;
//                    Log.i("그룹상태",""+threadStopFlag);
                    Thread.interrupted();
                }catch(Throwable t){
                }
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                threadStopFlag=0;
//                Log.i("차일드상태",""+threadStopFlag);
                new Thread(threadRun).start();
                return false;
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) { //Application 클래스 (폰트 적용)
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            updateThread();
        }
    };

    Runnable threadRun=new Runnable(){

        public void run() {
            // TODO Auto-generated method stub
            while(threadStopFlag!=1){
                try{
                    handler.sendMessage(handler.obtainMessage());
                    Thread.sleep(5000);
                }catch(Throwable t){}

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(threadStopFlag!=1) {
                            arrayGroup.clear();
                            arrayChild.clear();
                            setArrayData();
                            expandableListView.setAdapter(new ListViewAdapter(getApplicationContext(), arrayGroup, arrayChild));
                        }
                    }
                });
            }
        }
    };

    private void setArrayData(){
//        String tipData="str"+n;   // 저렇게 입력하면 str1의 값을 갖고올 수 있을 줄 알았는데 그냥 문자열 str1 이 들어가서 출력됨 ㅜㅜ
//        arrayGroup.add(tipData);

        String tipData="";

        if(n==1){
            tipData=str1;
        }else if(n==2){
            tipData=str2;
        }else if(n==3){
            tipData=str3;
        }else if(n==4){
            tipData=str4;
        }

        arrayGroup.add(tipData.toString());
        ArrayList<String> arr1=new ArrayList<String>();

        if(tipData.equals(str1)){
            arr1.add("하얗게 하얗게 히히히히히");
        }else if(tipData.equals(str2)){
            arr1.add("까맣게 까맣게 어둡게 어둡게");
        }else if(tipData.equals(str3)){
            arr1.add("빨강은 강렬하죠");
        }else if(tipData.equals(str4)){
            arr1.add("미쳐써요 주영이는");
        }

        arrayChild.put(arrayGroup.get(0),arr1);
    }

    private void updateThread(){
        if(n==4){   // 정보 데이터 개수까지
            n=1;
        }else{
            n++;
        }
    }

}
