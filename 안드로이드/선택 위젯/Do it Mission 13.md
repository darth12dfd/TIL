# 리싸이클러뷰에 고객 정보 추가하기

* 사용자가 고객 정보를 간단학 ㅔ이력하고 버튼을 누르면 리싸이클러뷰에 추가되도록 하기. 리싸이클러뷰에 하나의 아이템을 추가하는 기능이다.

## 조건

* 고객 정보를 입력할 수 있고 고객 정보 리스트가 표시되는 화면을 만든다.

* 화면의 위쪽에는 각각 이름, 생년월일, 전화번호를 입력할 수 있도록 이력상자 세 개를 만든다.

* 입력상자의 아래에는 [추가] 버튼을 만들어서 버튼을 누르면 리싸이클러뷰에 아이템이 추가되도록 한다.

* 화면 아래쪽에는 리싸이클러뷰를 표시한다. 리싸이클러뷰에 들어가는 각각의 아이템은 고객 정보를 표시하며 아이콘, 이름, 생년월일, 전화번호가 표시되도록 한다.

## 참고

* 리싸이클러뷰에 새로 추가도니 아이템을 보여주기 위해서는 리싸이클러뷰의 데이터를 관리하는 어댑터에 아이템을 추가해야 한다.

* 어댑터에 아이템을 추가한 후 ```notifyDataSetChanged()``` 메서드를 호출하기

## 소스코드

### 어댑터에 저장할 데이터 클래스

```java
public class Customer {
    String name;
    String birth;
    String mobile;
    int resId;

    public Customer(String name, String birth, String mobile) {
        this.name = name;
        this.birth = birth;
        this.mobile = mobile;
    }

    public Customer(String name, String birth, String mobile, int resId) {
        this.name = name;
        this.birth = birth;
        this.mobile = mobile;
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
```

### 아이템 클릭시 이벤트 처리할 인터페이스

```java
import android.view.View;

public interface OnCustomerItemClickListener {
    public void onItemClick(CustomerAdapter.ViewHolder holder, View view,int position);
}
```

### 어댑터 클래스

```java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//어댑터 클래스
public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> implements OnCustomerItemClickListener{
    ArrayList<Customer> items=new ArrayList<Customer>();
    OnCustomerItemClickListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView1;
        TextView textView2;
        TextView textView3;
        ImageView imageView;

        public ViewHolder(View itemView, final OnCustomerItemClickListener listener){//뷰 홀더 생성자로 전달되는 뷰 객체 참조하기
            super(itemView);

            //뷰 객체에 들어있는 텍스트뷰 참조하기
            textView1=itemView.findViewById(R.id.textView);
            textView2=itemView.findViewById(R.id.textView2);
            textView3=itemView.findViewById(R.id.textView3);
            imageView=itemView.findViewById(R.id.imageView);

            //아이템뷰에 OnClickListener 설정하기
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    //아이템뷰 클릭시 미리 정의한 다른 리스너의 메서드 호출하기
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this,view,position);
                    }
                }
            });
        }

        public void setItem(Customer item){
            textView1.setText(item.getName());
            textView2.setText(item.getBirth());
            textView3.setText(item.getMobile());
            imageView.setImageResource(item.getResId());

        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.customer_item,parent,false);//인플레이션을 통해 뷰 객체 만들기

        return new ViewHolder(itemView,this);//뷰홀더 객체를 생성하면서 뷰 객체를 전달하고 그 뷰홀더 객체를 반환하기
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Customer item=items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Customer item){
        items.add(item);
    }

    public void setItems(ArrayList<Customer> items){
        this.items=items;
    }

    public Customer getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, Customer item){
        items.set(position, item);
    }

    //외부에 리스너를 설정할 수 있도록 메서드 추가하기
    public void setOnItemClickListener(OnCustomerItemClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if(listener!=null){
            listener.onItemClick(holder,view,position);
        }
    }
}
```

### 고객정보 표시 XML 레이아웃

```xml
<?xml version="1.0" encoding="UTF-8"?>
<LinearLayout
    android:layout_height="wrap_content"
    android:layout_width="match_parent"
    android:orientation="vertical"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <androidx.cardview.widget.CardView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:cardBackgroundColor="#FFFFFFFF"
        app:cardCornerRadius="10dp"
        app:cardElevation="5dp"
        app:cardUseCompatPadding="true">


        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_margin="8dp"
            android:orientation="horizontal">

            <ImageView
                android:id="@+id/imageView"
                android:layout_width="80dp"
                android:layout_height="80dp"
                android:src="@drawable/customer" />


            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginLeft="10dp"
                android:orientation="vertical">

                <TextView
                    android:id="@+id/textView"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Name"
                    android:textColor="#ff5805"
                    android:textSize="40sp"
                    android:textStyle="bold" />

                <RelativeLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content">

                    <TextView
                        android:id="@+id/textView2"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_alignParentLeft="true"
                        android:layout_marginTop="6dp"
                        android:text="Birth"
                        android:textColor="#0223e0"
                        android:textSize="20sp" />

                    <TextView
                        android:id="@+id/textView3"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_alignParentRight="true"
                        android:text="Mobile"
                        android:textColor="#4902d6"
                        android:textSize="20sp" />
                </RelativeLayout>
            </LinearLayout>
        </LinearLayout>
    </androidx.cardview.widget.CardView>
</LinearLayout>
```

### 메인 액티비티 XML 레이아웃

```xml
<?xml version="1.0" encoding="UTF-8"?>
<LinearLayout
    android:orientation="vertical"
    android:layout_height="match_parent"
    android:layout_width="match_parent"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <LinearLayout
        android:orientation="horizontal"
        android:layout_height="wrap_content"
        android:layout_width="match_parent"
        android:id="@+id/layout1">

        <EditText
            android:layout_height="wrap_content"
            android:layout_width="0dp"
            android:id="@+id/editText"
            android:hint="이름"
            android:layout_weight="1"/>

        <EditText
            android:layout_height="wrap_content"
            android:layout_width="0dp"
            android:id="@+id/editText3"
            android:hint="전화번호"
            android:layout_weight="1"/>

    </LinearLayout>

    <LinearLayout
        android:orientation="horizontal"
        android:layout_height="wrap_content"
        android:layout_width="match_parent"
        android:id="@+id/layout2">

        <EditText
            android:layout_height="wrap_content"
            android:layout_width="match_parent"
            android:id="@+id/editText2"
            android:hint="생년월일"/>
    </LinearLayout>

    <RelativeLayout
        android:layout_height="wrap_content"
        android:layout_width="match_parent"
        android:id="@+id/layout3">

        <Button
            android:layout_height="wrap_content"
            android:layout_width="wrap_content"
            android:id="@+id/button"
            android:text="추가"
            android:layout_centerHorizontal="true"/>

        <TextView
            android:layout_height="wrap_content"
            android:layout_width="wrap_content"
            android:id="@+id/textView"
            android:text="0 명"
            android:textSize="14dp"
            android:textColor="#000000"
            android:layout_marginRight="14dp"
            android:layout_alignBaseline="@+id/button"
            android:layout_alignParentRight="true"/>

    </RelativeLayout>

    <androidx.recyclerview.widget.RecyclerView
        android:layout_height="match_parent"
        android:layout_width="match_parent"
        android:id="@+id/recyclerView"/>
</LinearLayout>
```

### 메인 액티비티 소스코드

```java
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity {
    EditText editText;
    EditText editText2;
    EditText editText3;

    TextView textView;

    RecyclerView recyclerView;
    CustomerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        textView = findViewById(R.id.textView);
        textView.setText("2 명");

        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        adapter = new CustomerAdapter();

        adapter.addItem(new Customer("김준수", "1995-10-20", "010-1000-1000", R.drawable.customer));
        adapter.addItem(new Customer("이희연", "1994-02-13", "010-2000-2000", R.drawable.customer));

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnCustomerItemClickListener() {
            @Override
            public void onItemClick(CustomerAdapter.ViewHolder holder, View view, int position) {
                Customer item = adapter.getItem(position);

                Toast.makeText(getApplicationContext(), "아이템 선택됨 : " + item.getName(), Toast.LENGTH_LONG).show();
            }
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText.getText().toString();
                String birth = editText2.getText().toString();
                String mobile = editText3.getText().toString();

                adapter.addItem(new Customer(name, birth, mobile, R.drawable.customer));
                adapter.notifyDataSetChanged();

                textView.setText(adapter.getItemCount() + " 명");
            }
        });

    }
}
```
