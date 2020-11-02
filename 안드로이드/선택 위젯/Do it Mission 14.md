# 쇼핑 상품 화면 구성하기

* 격자형태로 쇼핑 상품을 보여주는 화면을 구성해 보세요. 격자 형태의 뷰에 보이는 각각의 아이템에는 상품의 이미지와 정보가 표시된다.

## 조건

* 쇼핑 상품을 보여주는 화면을 리싸이클러뷰로 만든다.

* 리싸이클러뷰의 칼럼은 두 개로 하고, 아이템은 가상의 데이터를 여러 개 입력해둔다.

* 각각의 아이템에는 상품 이밎, 상품 이름, 가격, 간단한 설명이 보일 수 있도록 한다.

* 리싸이클러뷰의 한 아이템을 터치했을 때, 선택된 상품의 이름과 가격을 토스트로 간단하게 보여준다.

## 소스코드

### 메인 액티비티 XML 레이아웃

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".MainActivity" >

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</LinearLayout>
```

### 상품 정보 클래스

```java
public class Product {
    int image;
    String name;
    String price;
    String overview;

    public Product(int image,String name, String price, String overview) {
        this.image=image;
        this.name = name;
        this.price = price;
        this.overview = overview;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
```

### 상품 어댑터 클래스

```java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements OnProductClickListener{
    ArrayList<Product> items=new ArrayList<Product>();
    OnProductClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.product_item,parent,false);

        return new ViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product item=items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView1;
        TextView textView2;
        TextView textView3;
        ImageView imageview;

        public ViewHolder(@NonNull View itemView,final OnProductClickListener listener) {
            super(itemView);

            textView1=itemView.findViewById(R.id.textView1);
            textView2=itemView.findViewById(R.id.textView2);
            textView3=itemView.findViewById(R.id.textView3);
            imageview=itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(listener!=null){
                        listener.onProductClick(ViewHolder.this,view,position);
                    }
                }
            });
        }

        public void setItem(Product item){
            textView1.setText(item.getName());
            textView2.setText(item.getPrice());
            textView3.setText(item.getOverview());
            imageview.setImageResource(item.getImage());
        }
    }

    public void addItem(Product item){
        items.add(item);
    }

    public void setItems(ArrayList<Product> items){
        this.items=items;
    }

    public Product getItem(int position){
        return items.get(position);
    }

    public void setItem(int position,Product item){
        items.set(position,item);
    }

    public void setOnProductClickListener(OnProductClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onProductClick(ViewHolder viewHolder, View view, int position) {
        if(listener!=null){
            listener.onProductClick(viewHolder,view,position);
        }
    }
}
```

### 상품 정보 표시 XML 레이아웃 파일

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical">

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
            android:orientation="vertical">


            <ImageView
                android:id="@+id/imageView"
                android:layout_width="80dp"
                android:layout_height="80dp"
                android:padding="5dp"
                tools:srcCompat="@tools:sample/avatars" />

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="horizontal">

                <TextView
                    android:id="@+id/textView1"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:text="상품명"
                    android:textSize="20sp" />

                <TextView
                    android:id="@+id/textView2"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:text="상품 가격"
                    android:textColor="#0000FF"
                    android:textSize="20sp" />

            </LinearLayout>

            <TextView
                android:id="@+id/textView3"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="설명"
                android:textSize="20sp" />


        </LinearLayout>
    </androidx.cardview.widget.CardView>
</LinearLayout>
```

### 클릭 이벤트 처리 관련 인터페이스

```java
import android.view.View;

public interface OnProductClickListener {
    public void onProductClick(ProductAdapter.ViewHolder viewHolder, View view,int position);
}
```

### 메인 액티비티 클래스

```java
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recyclerView);

        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        adapter=new ProductAdapter();

        adapter.addItem(new Product(R.drawable.longcoat,"롱코트","160,000원","명절 기획상품"));
        adapter.addItem(new Product(R.drawable.y_shirts,"와이셔츠","80,000원","특가 상품"));
        adapter.addItem(new Product(R.drawable.shoes,"조깅화","220,000원","한정 상품"));
        adapter.addItem(new Product(R.drawable.sunglasses,"썬글라스","100,000원","신상품"));

        recyclerView.setAdapter(adapter);

        adapter.setOnProductClickListener(new OnProductClickListener() {
            @Override
            public void onProductClick(ProductAdapter.ViewHolder viewHolder, View view, int position) {
                Product item=adapter.getItem(position);
                Toast.makeText(getApplicationContext(),"이름 :"+item.getName()+" 가격: "+item.getPrice(),Toast.LENGTH_LONG).show();
            }
        });

    }
}
```
