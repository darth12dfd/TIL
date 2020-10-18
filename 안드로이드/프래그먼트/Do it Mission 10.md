# 기본 앱 화면 구성

* 바로가기 메뉴와 하단 탭 그리고 뷰페이저가 들어간 기본 앱 화면을 만들기

## 조건

* 앱의 화면에 바로가기 메뉴와 하단 탭 그리고 뷰페이저가 들어가도록 만들기

* 하단 탭에는 세 개의 탭 메뉴가 보이도록 한다.

* 하단 탭에서 첫 번째 탭 메뉴를 눌렀을 때 보이는 첫 번재 프래그먼트 화면 안에 뷰페이저가 표시되도록 한다. 그리고 뷰페이저 안에는 이미지나 기타 화면이 2~3개 들어가 있도록 만든다.

* 바로가기 메뉴를 넣어준다.

## 소스코드

### 메인 액티비티

#### 소스 파일

```kotlin
package org.techtown.mission

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.techtown.mission.R.id.container
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, FragmentCallback {
    var toolbar:Toolbar?=null
    var drawer:DrawerLayout?=null

    var fragment1=Fragment1()
    var fragment2=Fragment2()
    var fragment3=Fragment3()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer=findViewById(R.id.drawer_layout)
        val toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView:NavigationView=findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        fragment1= Fragment1()
        supportActionBar?.setTitle("첫 번째 화면")
        supportFragmentManager.beginTransaction().add(container,fragment1).commit()

        val bottomNavigationView:BottomNavigationView=findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu1->{
                    supportFragmentManager.beginTransaction().replace(container,fragment1).commit()
                    supportActionBar?.setTitle("첫 번째 화면")
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu2->{
                    supportFragmentManager.beginTransaction().replace(container, fragment2).commit()
                    supportActionBar?.setTitle("두 번째 화면")
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu3->{
                    supportFragmentManager.beginTransaction().replace(container,fragment3).commit()
                    supportActionBar?.setTitle("세 번째 화면")
                    return@OnNavigationItemSelectedListener true
                }
                else-> exitProcess(0)
            }
        })
    }

    override fun onBackPressed() {
        if(drawer!!.isDrawerOpen(GravityCompat.START)){
            drawer!!.closeDrawer(GravityCompat.START)
        }
        else{
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id=item.itemId

        if(id==R.id.menu1){
            onFragmentSelected(0,null)
        }
        else if(id==R.id.menu2){
            onFragmentSelected(1,null)
        }
        else if(id==R.id.menu3){
            onFragmentSelected(2,null)
        }

        drawer!!.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onFragmentSelected(position: Int, bundle: Bundle?) {
        var curFragment: Fragment? =null

        if(position==0){
            curFragment=fragment1
            toolbar!!.setTitle("첫 번째 화면")
        }
        else if(position==1){
            curFragment=fragment2
            toolbar!!.setTitle("두 번째 화면")
        }
        else if(position==2){
            curFragment=fragment3
            toolbar!!.setTitle("세 번째 화면")
        }

        supportFragmentManager.beginTransaction().replace(container, curFragment!!).commit()
    }
}
```

#### 레이아웃 리소스 파일

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.drawerlayout.widget.DrawerLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/drawer_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true"
    tools:openDrawer="start">

    <androidx.coordinatorlayout.widget.CoordinatorLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">


        //바로가기 메뉴 버튼
        <com.google.android.material.appbar.AppBarLayout
            android:id="@+id/appBarLayout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:theme="@style/AppTheme.AppBarOverlay">

            <androidx.appcompat.widget.Toolbar
                android:id="@+id/toolbar"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                android:background="?attr/colorPrimary"
                app:popupTheme="@style/AppTheme.PopupOverlay" />
        </com.google.android.material.appbar.AppBarLayout>

        //뷰페이저와 하단 메뉴를 보여줄 화면
        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layout_behavior="@string/appbar_scrolling_view_behavior">

            //뷰 페이저를 보여줄 화면
            <FrameLayout
                android:id="@+id/container"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:layout_above="@id/bottom_navigation">
            </FrameLayout>

            //하단 메뉴
            <com.google.android.material.bottomnavigation.BottomNavigationView
                android:id="@+id/bottom_navigation"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:itemBackground="@color/colorPrimary"
                app:itemTextColor="@drawable/item_color"
                app:itemIconTint="@drawable/item_color"
                android:layout_alignParentBottom="true"
                app:menu="@menu/menu_bottom" />
        </RelativeLayout>
    </androidx.coordinatorlayout.widget.CoordinatorLayout>

    //바로가기 메뉴 버튼 클릭시 나오는 메뉴 화면
    <com.google.android.material.navigation.NavigationView
        android:id="@+id/nav_view"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        android:fitsSystemWindows="true"
        app:headerLayout="@layout/nav_header_main"
        app:menu="@menu/activity_main_drawer" />

</androidx.drawerlayout.widget.DrawerLayout>
```

#### FragementCallback 인터페이스

```kotlin
package org.techtown.mission

import android.os.Bundle

public interface FragmentCallback {
    public fun onFragmentSelected(position:Int,bundle: Bundle?)
}
```

### 첫 번째 프래그먼트(뷰페이저가 담길 프래그먼트)

#### 소스파일

```kotlin
package org.techtown.mission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
class Fragment1 : Fragment() {
    var pager: ViewPager?=null
    var sub_frag1:SubFragment1?=null
    var sub_frag2:SubFragment2?=null
    var sub_frag3:SubFragment3?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView:ViewGroup= inflater.inflate(R.layout.fragment1,container,false) as ViewGroup

        pager=rootView.findViewById(R.id.pager)
        pager!!.offscreenPageLimit=3

        var pageAdapter=PageAdapter()


        sub_frag1=SubFragment1()
        pageAdapter.addItem(sub_frag1!!)

        sub_frag2= SubFragment2()
        pageAdapter.addItem(sub_frag2!!)

        sub_frag3= SubFragment3()
        pageAdapter.addItem(sub_frag3!!)

        pager!!.adapter=pageAdapter

        return rootView
    }

    inner class PageAdapter: FragmentPagerAdapter(childFragmentManager) {
        var items=ArrayList<Fragment>()

        public fun addItem(item:Fragment){
            items.add(item)
        }

        override fun getCount(): Int {
            return items.size
        }

        override fun getItem(position: Int): Fragment {
            return items.get(position)
        }
    }

}
```
