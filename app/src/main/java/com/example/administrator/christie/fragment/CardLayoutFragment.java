package com.example.administrator.christie.fragment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import com.example.administrator.christie.R;
import com.example.administrator.christie.TApplication;
import com.example.administrator.christie.activity.PlateDetailActivity;
import com.example.administrator.christie.adapter.CardsAdapter;
import com.example.administrator.christie.entity.Plate;
import com.example.administrator.christie.util.Consts;
import com.example.administrator.christie.view.CustomProgress;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class CardLayoutFragment extends Fragment {
    private ListView cardsList;
    public static final int SHOW_RESPONSE = 0;
    ArrayList<Plate> items = new ArrayList<>();
    private CardsAdapter adapter;
    private CustomProgress dialog;

    // 2.1 定义用来与外部activity交互，获取到宿主activity
    private FragmentInteraction listterner;

    // 1 定义了所有activity必须实现的接口方法
    public interface FragmentInteraction {
        void process(String str);
    }

    // 当FRagmen被加载到activity的时候会被回调
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof FragmentInteraction) {
            listterner = (FragmentInteraction)context; // 2.2 获取到宿主activity并赋值
        } else{
            throw new IllegalArgumentException("activity must implements FragmentInteraction");
        }
    }


    public CardLayoutFragment() {
        // nop
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card_layout, container, false);
        cardsList = (ListView) rootView.findViewById(R.id.cards_list);
        setupList();
        return rootView;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listterner = null;
    }

    private void setupList() {
        GetThread thread = new GetThread(TApplication.user.getId());
        thread.start();
    }

    private final class ListItemButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = cardsList.getFirstVisiblePosition(); i <= cardsList.getLastVisiblePosition(); i++) {
                if (v == cardsList.getChildAt(i - cardsList.getFirstVisiblePosition()).findViewById(R.id.list_item_card_button_1)) {
                    // PERFORM AN ACTION WITH THE ITEM AT POSITION i
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setIcon(R.drawable.ic_launcher);
                    builder.setTitle("设置选项");
                    //    指定下拉列表的显示数据
                    final String[] s;
                    //    设置一个下拉的列表选择项
                    final Plate plate = items.get(i);
                    if (plate.getFisdefault().equals("Y")){
                        s = new String[]{"删除"};
                        builder.setItems(s, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        //数据库删除
                                        DelThread thread = new DelThread(plate.getId());
                                        thread.start();
                                        break;
                                }
                            }
                        });
                    }else{
                        s = new String[]{"设为默认","删除"};
                        builder.setItems(s, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        //设为默认
                                        Get get = new Get(plate.getId(),TApplication.user.getId());
                                        get.start();
                                        break;
                                    case 1:
                                        items.remove(plate);
                                        adapter.notifyDataSetChanged();
                                        //数据库删除
                                        DelThread thread = new DelThread(plate.getId());
                                        thread.start();
                                        break;
                                }
                            }
                        });
                    }
                    builder.show();
                } else if (v == cardsList.getChildAt(i - cardsList.getFirstVisiblePosition()).findViewById(R.id.list_item_card_button_2)) {
                    // PERFORM ANOTHER ACTION WITH THE ITEM AT POSITION i
                    Intent intent = new Intent(getActivity(),PlateDetailActivity.class);
                    intent.putExtra("platedetail",(Serializable) items.get(i));
                    startActivity(intent);
                }
            }
        }
    }

    private final class ListItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }


    //子线程：使用Get方法向服务器发送数据
    class GetThread extends Thread {
        String id;

        public GetThread(String id) {
            dialog = CustomProgress.show(getActivity(),"加载中...", true, null);
            this.id = id;
        }

        @Override
        public void run() {
            HttpClient httpClient = new DefaultHttpClient();
            String url = Consts.URL+"getplate?id="+id;
            Log.i("当前请求的URL",url+"  <<<<<<<<<<<<<<<<<<<<<<");
            //第二步：生成使用POST方法的请求对象
            HttpGet httpGet = new HttpGet(url);
            try {
                try {
                    //第三步：执行请求对象，获取服务器发还的相应对象
                    HttpResponse response = httpClient.execute(httpGet);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    if (response.getStatusLine().getStatusCode() == 200) {
                        //第五步：从相应对象当中取出数据，放到entity当中
                        HttpEntity entity = response.getEntity();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(entity.getContent()));
                        String result = reader.readLine();
                        Log.d("HTTP", "POST:" + result);
                        //在子线程中将Message对象发出去
                        Message message = new Message();
                        message.what = SHOW_RESPONSE;
                        message.obj = result;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    try {
                        JSONArray array = new JSONArray(response);
                        for(int i=0;i<array.length();i++) {
                            JSONObject json = array.getJSONObject(i);
                            Plate plate = new Plate();
                            plate.setId(json.getString("id"));
                            plate.setFplateno(json.getString("fplateno"));
                            plate.setFisdefault(json.getString("fisdefault"));
                            plate.setFmodel(json.getString("fmodel"));
                            plate.setFcolor(json.getString("fcolor"));
                            plate.setFmobile(json.getString("fmobile"));
                            items.add(plate);
                        }
                        adapter = new CardsAdapter(getActivity(), items, new ListItemButtonClickListener());
                        cardsList.setAdapter(adapter);
                        cardsList.setOnItemClickListener(new ListItemClickListener());
                        listterner.process(String.valueOf(items.size()));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };





    //更改默认车牌
    class Get extends Thread {
        String id;
        String oldid;

        public Get(String id,String oldid) {
            this.id = id;
            this.oldid = oldid;
        }

        @Override
        public void run() {
            HttpClient httpClient = new DefaultHttpClient();
            String url = Consts.URL+"changeplate?id="+id+"&oldid="+oldid;
            Log.i("当前请求的URL",url+"  <<<<<<<<<<<<<<<<<<<<<<");
            //第二步：生成使用POST方法的请求对象
            HttpGet httpGet = new HttpGet(url);
            try {
                try {
                    //第三步：执行请求对象，获取服务器发还的相应对象
                    HttpResponse response = httpClient.execute(httpGet);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    if (response.getStatusLine().getStatusCode() == 200) {
                        //第五步：从相应对象当中取出数据，放到entity当中
                        HttpEntity entity = response.getEntity();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(entity.getContent()));
                        String result = reader.readLine();
                        Log.d("HTTP", "POST:" + result);
                        //在子线程中将Message对象发出去
                        Message message = new Message();
                        message.what = SHOW_RESPONSE;
                        message.obj = result;
                        handler1.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    if(response.equals("1")){
                        Toast.makeText(getActivity(),"修改成功,请刷新",Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getActivity(),"操作失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    //删除车牌
    class DelThread extends Thread {
        String id;

        public DelThread(String id) {
            this.id = id;
        }

        @Override
        public void run() {
            HttpClient httpClient = new DefaultHttpClient();
            String url = Consts.URL+"delplate?id="+id;
            Log.i("当前请求的URL",url+"  <<<<<<<<<<<<<<<<<<<<<<");
            //第二步：生成使用POST方法的请求对象
            HttpGet httpGet = new HttpGet(url);
            try {
                try {
                    //第三步：执行请求对象，获取服务器发还的相应对象
                    HttpResponse response = httpClient.execute(httpGet);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    if (response.getStatusLine().getStatusCode() == 200) {
                        //第五步：从相应对象当中取出数据，放到entity当中
                        HttpEntity entity = response.getEntity();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(entity.getContent()));
                        String result = reader.readLine();
                        Log.d("HTTP", "POST:" + result);
                        //在子线程中将Message对象发出去
                        Message message = new Message();
                        message.what = SHOW_RESPONSE;
                        message.obj = result;
                        handler2.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    if(response.equals("1")){
                        Toast.makeText(getActivity(),"删除成功",Toast.LENGTH_SHORT).show();
                        listterner.process(String.valueOf(items.size()-1));
                    }else if(response.equals("2")){
                        Toast.makeText(getActivity(),"请设置新的默认车牌后再删除！",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(),"操作失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };
}
