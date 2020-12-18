# RSS 조회 내용을 그리드뷰로 보여주기

* RSS 사이트를 조회하여 최신 기사를 받은 후 격자 모양으로 보여주는 기능을 만들기. RSS 사이트는 연예, 경제 등 어떤 내용이든 상관 X

## 조건

* RSS 기사를 조회할 수 있는 화면을 구성한다.

* 화면의 아래쪽에 [조회]버튼을 배치하고 나머지 공간은 리싸이클러뷰가 차지하도록 한다.

* [조회]버튼을 누르면 지정한 RSS 사이트를 조회하여 최신 기사를 가져온 후 리싸이클러뷰에 표시한다.

* RSS 사이트 주소는 미리 소스코드에 설정한다.

* 리싸이클러뷰의 각 아이템에 보일 데이터로는 아이콘과 제목, 내용이 표시되도록 한다.

## 참고

* RSS 사이트를 조회해서 가져온 데이터는 태그로 구성되므로 RSS 조회 결과 문서를 파싱한 후 각 기사를 리싸이클러뷰의 아이템으로 추가해야 한다.

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

    <EditText
        android:id="@+id/editText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_weight="7"
        android:ems="10"
        android:hint="RSS 주소 입력하기"
        android:inputType="textPersonName"
        android:textSize="16sp" />

    <Button
        android:id="@+id/button"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_weight="2"
        android:text="조회" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_weight="1" />

</LinearLayout>
```

### 메인 액티비티 클래스

```java
package org.techtown.mission20;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static String rssUrl = "http://api.sbs.co.kr/xml/news/rss.jsp?pmDiv=entertainment";

    ProgressDialog progressDialog;
    Handler handler = new Handler();

    RecyclerView recyclerView;
    RssAdapter adapter;
    ArrayList<RssItem> newsItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RssAdapter();
        recyclerView.setAdapter(adapter);
        
        newsItemList = new ArrayList<RssItem>();

        final EditText edit01 = findViewById(R.id.editText);
        edit01.setText(rssUrl);

        Button show_btn = findViewById(R.id.button);
        show_btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String inputStr = edit01.getText().toString();
                showRSS(inputStr);
            }

        });

    }

    //RSS 보여주기 로딩창
    private void showRSS(String urlStr) {
        try {
            progressDialog = ProgressDialog.show(this, "RSS Refresh", "RSS 정보 업데이트 중...", true, true);

            RefreshThread thread = new RefreshThread(urlStr);
            thread.start();

        } catch (Exception e) {
            Log.e(TAG, "Error", e);
        }
    }

    //새로 고침 스레드
    class RefreshThread extends Thread {
        String urlStr;

        public RefreshThread(String str) {
            urlStr = str;
        }

        public void run() {

            try {

                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = builderFactory.newDocumentBuilder();

                URL urlForHttp = new URL(urlStr);

                InputStream instream = getInputStreamUsingHTTP(urlForHttp);
                Document document = builder.parse(instream);
                int countItem = processDocument(document);
                Log.d(TAG, countItem + " news item processed.");

                // post for the display of fetched RSS info.
                //fetch된 RSS 정보의 디스플레이를 포스트한다.
                handler.post(updateRSSRunnable);

            } catch(Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    //
    public InputStream getInputStreamUsingHTTP(URL url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        int resCode = conn.getResponseCode();
        Log.d(TAG, "Response Code : " + resCode);

        InputStream instream = conn.getInputStream();

        return instream;
    }

    /**
     * process DOM document for RSS
     */
    private int processDocument(Document doc) {
        newsItemList.clear();

        Element docEle = doc.getDocumentElement();
        NodeList nodelist = docEle.getElementsByTagName("item");
        int count = 0;
        if ((nodelist != null) && (nodelist.getLength() > 0)) {
            for (int i = 0; i < nodelist.getLength(); i++) {
                RssItem newsItem = dissectNode(nodelist, i);
                if (newsItem != null) {
                    newsItemList.add(newsItem);
                    count++;
                }
            }
        }

        return count;
    }

    private RssItem dissectNode(NodeList nodelist, int index) {
        RssItem newsItem = null;

        try {
            Element entry = (Element) nodelist.item(index);

            Element title = (Element) entry.getElementsByTagName("title").item(0);
            Element link = (Element) entry.getElementsByTagName("link").item(0);
            Element description = (Element) entry.getElementsByTagName("description").item(0);

            NodeList pubDataNode = entry.getElementsByTagName("pubDate");
            if (pubDataNode == null) {
                pubDataNode = entry.getElementsByTagName("dc:date");
            }
            Element pubDate = (Element) pubDataNode.item(0);

            Element author = (Element) entry.getElementsByTagName("author").item(0);
            Element category = (Element) entry.getElementsByTagName("category").item(0);

            String titleValue = null;
            if (title != null) {
                Node firstChild = title.getFirstChild();
                if (firstChild != null) {
                    titleValue = firstChild.getNodeValue();
                }
            }
            String linkValue = null;
            if (link != null) {
                Node firstChild = link.getFirstChild();
                if (firstChild != null) {
                    linkValue = firstChild.getNodeValue();
                }
            }

            String descriptionValue = null;
            if (description != null) {
                Node firstChild = description.getFirstChild();
                if (firstChild != null) {
                    descriptionValue = firstChild.getNodeValue();
                }
            }

            String pubDateValue = null;
            if (pubDate != null) {
                Node firstChild = pubDate.getFirstChild();
                if (firstChild != null) {
                    pubDateValue = firstChild.getNodeValue();
                }
            }

            String authorValue = null;
            if (author != null) {
                Node firstChild = author.getFirstChild();
                if (firstChild != null) {
                    authorValue = firstChild.getNodeValue();
                }
            }

            String categoryValue = null;
            if (category != null) {
                Node firstChild = category.getFirstChild();
                if (firstChild != null) {
                    categoryValue = firstChild.getNodeValue();
                }
            }

            Log.d(TAG, "item node : " + titleValue + ", " + linkValue + ", " + descriptionValue +
                    ", " + pubDateValue + ", " + authorValue + ", " + categoryValue);

            newsItem = new RssItem(titleValue, linkValue, descriptionValue,
                    pubDateValue, authorValue, categoryValue);

        } catch (DOMException e) {
            e.printStackTrace();
        }

        return newsItem;
    }


    Runnable updateRSSRunnable = new Runnable() {
        public void run() {

            try {

                Resources res = getResources();
                Drawable rssIcon = res.getDrawable(R.drawable.rss_icon);
                for (int i = 0; i < newsItemList.size(); i++) {
                    RssItem newsItem = newsItemList.get(i);
                    newsItem.setIcon(rssIcon);
                    adapter.addItem(newsItem);
                }

                adapter.notifyDataSetChanged();

                progressDialog.dismiss();
            } catch(Exception ex) {
                ex.printStackTrace();
            }

        }
    };

}
```

### RSS 아이템 클래스

```java
package org.techtown.mission20;

import android.graphics.drawable.Drawable;

public class RssItem {

    private String title;
    private String link;
    private String description;
    private String pubDate;
    private String author;
    private String category;

    private Drawable mIcon;

    /**
     * Initialize with icon and data array
     */
    public RssItem() {
    }

    /**
     * Initialize with icon and strings
     */
    public RssItem(String title, String link, String description, String pubDate, String author, String category) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.author = author;
        this.category = category;
    }

    /**
     * Set icon
     *
     * @param icon
     */
    public void setIcon(Drawable icon) {
        mIcon = icon;
    }

    /**
     * Get icon
     *
     * @return
     */
    public Drawable getIcon() {
        return mIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Compare with the input object
     *
     * @param other
     * @return
     */
    public int compareTo(RssItem other) {
        if (title.equals(other.getTitle())) {
            return -1;
        } else if (link.equals(other.getLink())) {
            return -1;
        } else if (description.equals(other.getDescription())) {
            return -1;
        } else if (pubDate.equals(other.getPubDate())) {
            return -1;
        } else if (author.equals(other.getAuthor())) {
            return -1;
        } else if (category.equals(other.getCategory())) {
            return -1;
        }

        return 0;
    }

}
```

### RSS 아이템 XML 레이아웃

```xml
<LinearLayout
    android:layout_marginRight="4dp"
    android:layout_marginLeft="4dp"
    android:layout_marginTop="4dp"
    android:orientation="vertical"
    android:layout_height="wrap_content"
    android:layout_width="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <androidx.cardview.widget.CardView
        android:layout_height="wrap_content"
        android:layout_width="match_parent"
        app:cardUseCompatPadding="true"
        app:cardElevation="5dp"
        app:cardCornerRadius="10dp"
        app:cardBackgroundColor="#FFFFFFFF">

        <LinearLayout
            android:orientation="vertical"
            android:layout_height="300dp"
            android:layout_width="match_parent"
            android:layout_margin="8dp">

            <RelativeLayout
                android:layout_height="wrap_content"
                android:layout_width="match_parent">

                <ImageView
                    android:layout_height="40dp"
                    android:layout_width="40dp"
                    android:layout_alignParentTop="true"
                    android:layout_alignParentLeft="true"
                    android:padding="8dp"
                    android:id="@+id/iconItem"/>

                <LinearLayout
                    android:orientation="vertical"
                    android:layout_height="wrap_content"
                    android:layout_width="wrap_content"
                    android:layout_alignParentTop="true"
                    android:id="@+id/layout1"
                    android:layout_toRightOf="@+id/iconItem"
                    android:layout_alignParentRight="true">

                    <TextView
                        android:layout_height="wrap_content"
                        android:layout_width="wrap_content"
                        android:padding="4dp"
                        android:id="@+id/dataItem01"
                        android:textSize="16sp"
                        android:textStyle="bold"/>

                </LinearLayout>

            <LinearLayout
                android:orientation="vertical"
                android:layout_height="wrap_content"
                android:layout_width="wrap_content"
                android:layout_below="@+id/layout1">

                <TextView
                    android:layout_height="wrap_content"
                    android:layout_width="wrap_content"
                    android:id="@+id/dataItem02"/>

                <TextView
                    android:layout_height="wrap_content"
                    android:layout_width="wrap_content"
                    android:id="@+id/dataItem03"
                    android:textSize="12sp"
                    android:textStyle="bold"
                    android:paddingRight="4dp"
                    android:textColor="#ccf88107"/>

            </LinearLayout>
        </RelativeLayout>

        <WebView
            android:layout_height="match_parent"
            android:layout_width="match_parent"
            android:padding="4dp"
            android:id="@+id/dataItem04"/>

        </LinearLayout>
    </androidx.cardview.widget.CardView>
</LinearLayout>
```

### RSS 어댑터 클래스

```java
package org.techtown.mission20;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RssAdapter extends RecyclerView.Adapter<RssAdapter.ViewHolder>
        implements OnRssItemClickListener {
    ArrayList<RssItem> items = new ArrayList<RssItem>();

    OnRssItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.rss_item, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        RssItem item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(RssItem item) {
        items.add(item);
    }

    public void setItems(ArrayList<RssItem> items) {
        this.items = items;
    }

    public RssItem getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, RssItem item) {
        items.set(position, item);
    }

    public void setOnItemClickListener(OnRssItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIcon;
        private TextView mText01;
        private TextView mText02;
        private TextView mText03;
        private WebView mText04;

        public ViewHolder(View itemView, final OnRssItemClickListener listener) {
            super(itemView);

            mIcon = itemView.findViewById(R.id.iconItem);
            mText01 = itemView.findViewById(R.id.dataItem01);
            mText02 = itemView.findViewById(R.id.dataItem02);
            mText03 = itemView.findViewById(R.id.dataItem03);
            mText04 = itemView.findViewById(R.id.dataItem04);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(RssItem item) {
            mIcon.setImageDrawable(item.getIcon());

            mText01.setText(item.getTitle());
            mText02.setText(item.getPubDate());

            String category = item.getCategory();
            if (category != null) {
                mText03.setText(category);
            }

            setTextToWebView(item.getDescription());

        }

        private void setTextToWebView(String msg) {
            Log.d("RSSNewsItemView", "setTextToWebView() called.");

            String outData = msg;

            outData = outData.replace("\"//", "\"http://");

            mText04.loadDataWithBaseURL("http://localhost/", outData, "text/html", "utf-8", null);
        }

    }

}
```

### RSS 아이템 클릭리스너 인터페이스

```java
package org.techtown.mission20;

import android.view.View;

public interface OnRssItemClickListener {
    public void onItemClick(RssAdapter.ViewHolder holder, View view, int position);
}
```
