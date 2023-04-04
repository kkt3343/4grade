package com.example.capstone

import android.app.ActionBar
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.detail.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.annotation.Nullable

class Favorite : Fragment() {
    //그림을 저장할 공간
    private lateinit var bitmap: Bitmap
    private var Detail_bitmap : MutableList<Bitmap> = mutableListOf<Bitmap>()

    // ArrayList -> Json으로 변환
    var box = ArrayList<Any>()

    private lateinit var netip : String

    //동적할당부분 코드
    //스크롤레이아웃
    private var scrolllayout : LinearLayout? = null
    //동적 배열 (텍스트 및 이미지 및 레이아웃)
    private var dynamic_text : MutableList<TextView> = mutableListOf<TextView>()
    private var dynamic_image : MutableList<ImageView> = mutableListOf<ImageView>()
    private var dynamic_layout : MutableList<LinearLayout> = mutableListOf<LinearLayout>()
    private var dynamic_button : MutableList<Button> = mutableListOf<Button>()

    lateinit var layout : View
    lateinit var mycontext : Context

    @Nullable
    @Override
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, saveInstanceState: Bundle?): View? {

        //뷰를 가져온다. (findviewmyid 사용)
        layout = inflater.inflate(R.layout.favorite, container, false)
        //getApplicationContext대신해서 사용
        mycontext = container!!.context

        return layout
    }

    fun favorite_layout_make(context: Context, layout: View)
    {
        var getcount = box.size

        //쓰레드를 하고 마칠 때 까지 기다린다.
        for (i in 0 until getcount)
        {
            var thread = NetworkThread()
            thread.idnumber = box.get(i).toString()

            thread.start()
            thread.join(5000)
        }

        //동적할당 부분
        //스크롤 뷰에 추가한다.
        scrolllayout = layout.findViewById(R.id.favorite_scroll) as LinearLayout
        for (i in 0 until getcount)
        {
            //레이아웃을 먼저 추가
            dynamic_layout.add(LinearLayout(context))

            //그림 6 , 글씨 5 , 버튼 2
            //사이즈 설정
            dynamic_layout[i].weightSum = 13f

            //이미지 추가
            dynamic_image.add(ImageView(context))

            //이미지 예외 처리용 22.03.23
            try{
                dynamic_image[i].setImageBitmap(Detail_bitmap[i])
            }catch (e : Exception){
                //dynamic_image[i].setImageResource(R.drawable.noimage)
            }
            var tmpimage = LinearLayout.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT)
            tmpimage.weight = 6f
            tmpimage.height = 500
            //tmpimage.gravity = Gravity.LEFT

            dynamic_image[i].layoutParams = tmpimage
            //scrolllayout!!.addView(dynamic_image[i])

            // # 초기화 # 22.03.16 no1
            //할당이 되어있다면 먼저 제거를 한다.
            if(dynamic_image[i].parent != null){
                (dynamic_image[i].parent as ViewGroup).removeView(dynamic_image[i])
            }
            dynamic_layout[i]!!.addView(dynamic_image[i])

            //텍스트 추가
            dynamic_text.add(TextView(context))

            //텍스트를 원하는대로 편집하는 부분
            //단위 변경
            var kmunit = ExchangeFormat_1000();
            var priceunit = ExchangeFormat();
            var distance_unit = kmunit.unit_change(distance[i])
            var price_unit = priceunit.unit_change(price[i])

            var tmptext = ""
            tmptext = tmptext + "■" + car_title[i] + "\n"
            tmptext = tmptext + "■" + "연식 :" + caryear[i] + "\n"
            tmptext = tmptext + "■" + "KM수 :" + distance_unit + "KM\n"
            tmptext = tmptext + "■" + "가격 :" + price_unit + "원\n"

            dynamic_text[i].setText(tmptext)
            //볼드체 처리
            dynamic_text[i].setTypeface(null, Typeface.BOLD)

            var tmplayout = LinearLayout.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT)
            tmplayout.weight = 5f
            tmplayout.gravity = Gravity.CENTER
            dynamic_text[i].layoutParams = tmplayout
            //scrolllayout!!.addView(dynamic_text[i])

            // # 초기화 # 22.03.16 no2
            //할당이 되어있다면 먼저 제거를 한다.
            if(dynamic_text[i].parent != null){
                (dynamic_text[i].parent as ViewGroup).removeView(dynamic_text[i])
            }
            dynamic_layout[i]!!.addView(dynamic_text[i])


            //버튼 추가
            dynamic_button.add(Button(context))
            var tmpbutton = LinearLayout.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT)
            tmpbutton.weight = 2f
            tmpbutton.gravity = Gravity.CENTER
            dynamic_button[i].layoutParams = tmpbutton
            dynamic_button[i].text = "삭제"
            //dynamic_button[i].text = "삭제" + car_id[i]

            // # 초기화 # 22.03.16 no3
            //할당이 되어있다면 먼저 제거를 한다.
            if(dynamic_button[i].parent != null){
                (dynamic_button[i].parent as ViewGroup).removeView(dynamic_button[i])
            }
            dynamic_button[i].setBackgroundResource(R.drawable.btn_normal)
            dynamic_layout[i]!!.addView(dynamic_button[i])

            // # 초기화 # 22.03.16 no4
            //할당이 되어있다면 먼저 제거를 한다.
            if(dynamic_layout[i].parent != null){
                (dynamic_layout[i].parent as ViewGroup).removeView(dynamic_layout[i])
            }
            scrolllayout!!.addView(dynamic_layout[i])

            /* -------------------------------------------- */
            dynamic_button[i].setOnClickListener {
                Toast.makeText(context, "찜 목록에서 제거했습니다.", Toast.LENGTH_SHORT).show()
                //찜목록에 제거
                var caridArray: ArrayList<Any> = getStringArrayPref(context, "favorite")
                caridArray.remove(car_id[i])
                setStringArrayPref(context, "favorite", caridArray)

                scrolllayout!!.removeView(dynamic_layout[i])
            }

            //표 만드는 부분
            dynamic_layout[i].setBackgroundResource(R.drawable.border_bottom)
            //클릭 부분
            dynamic_layout[i].setOnClickListener {

                //검색기록에 추가
                var caridArray : ArrayList<Any> = getStringArrayPref(context, "history")

                if (caridArray.contains(car_id[i])) {
                    caridArray.remove(car_id[i])
                }
                if (caridArray.size >= 20) {
                    caridArray.removeAt(caridArray.size - 1)
                }
                caridArray.add(0, car_id[i])
                setStringArrayPref(context, "history", caridArray)

                var nextIntent = Intent(activity,Search_Result_InfomationActivity::class.java)
                nextIntent.putExtra("url_address", url_address[i]) //0
                nextIntent.putExtra("sitetype", sitetype[i]) //1
                nextIntent.putExtra("car_title", car_title[i]) //2
                nextIntent.putExtra("carnumber", carnumber[i]) //3
                nextIntent.putExtra("cartype", cartype[i]) //4
                nextIntent.putExtra("manufacturer", manufacturer[i]) //5
                nextIntent.putExtra("model", model[i]) //6
                nextIntent.putExtra("model_detail", model_detail[i]) //7
                nextIntent.putExtra("price", price[i]) //8
                nextIntent.putExtra("distance", distance[i]) //9
                nextIntent.putExtra("caryear", caryear[i]) //10
                nextIntent.putExtra("carcolor", carcolor[i]) //11
                nextIntent.putExtra("carfuel", carfuel[i]) //12
                nextIntent.putExtra("imglink", imglink[i]) //13
                nextIntent.putExtra("car_id",car_id[i]) //14
                startActivity(nextIntent)
            }
        }
    }

    //인터넷 연결 실패시 보여줄 화면
    //참조 : https://webnautes.tistory.com/1094
    fun show() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("연결 실패")
        builder.setMessage("인터넷 연결이 되어있지 않거나\n 서버와 연결을 할 수 없습니다.\n 앱을 종료합니다.")
        builder.setPositiveButton(
            "확인"
        ) { dialog, which -> //앱 자체를 종료 (이제 종료기능은 사용하지 않음)
//            finishAffinity()
//            System.runFinalization()
//            System.exit(0)
        }
//            builder.setNegativeButton(
//                "아니오"
//            ) { dialog, which ->
//                Toast.makeText(
//                    applicationContext,
//                    "아니오를 선택했습니다.",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
        builder.setCancelable(false)
        builder.show()
    }

    //그림 가져오기
    inner class uThread : Thread() {
        var str = ""
        override fun run() {
            val tmp = "http://" + netip + "/" + str
            Log.d("주소",tmp)
            try {
                val url = URL(tmp)
                var conn: HttpURLConnection? = null
                conn = url.openConnection() as HttpURLConnection

                //접속오류시 연결안함
                conn.connectTimeout = 10000
                conn!!.connect()
                try {
                    //연결 성공, 이미지인경우
                    val aaa = conn.inputStream //inputStream 값 가져오기
                    bitmap = BitmapFactory.decodeStream(aaa) // Bitmap으로 반환
                } catch (e: Exception) {
                    //연결은 성공했지만, 이미지가 아닌경우입니다.
                }
            } catch (SocketTimeoutException: Exception) {
            }
        }
    }

    // 자동차 ID
    private var car_id : MutableList<String> = mutableListOf<String>()
    // URL 주소
    private var url_address : MutableList<String> = mutableListOf<String>()
    // 사이트 타입
    private var sitetype : MutableList<String> = mutableListOf<String>()
    // title = 자동차 전체 이름 ex(현대 LF 쏘나타 LPI 프리미엄)
    private var car_title : MutableList<String> = mutableListOf<String>()
    // 자동차 번호
    private var carnumber : MutableList<String> = mutableListOf<String>()
    // 자동차 타입 ex(중형차)
    private var cartype : MutableList<String> = mutableListOf<String>()
    // 제조사
    private var manufacturer : MutableList<String> = mutableListOf<String>()
    // 모델명 ex(쏘나타)
    private var model : MutableList<String> = mutableListOf<String>()
    // 모델명_자세히 ex(LF 쏘나타)
    private var model_detail : MutableList<String> = mutableListOf<String>()
    // 가격
    private var price : MutableList<String> = mutableListOf<String>()
    // 주행거리
    private var distance : MutableList<String> = mutableListOf<String>()
    // 연식
    private var caryear : MutableList<String> = mutableListOf<String>()
    // 색깔
    private var carcolor : MutableList<String> = mutableListOf<String>()
    // 연료타입
    private var carfuel : MutableList<String> = mutableListOf<String>()
    // 이미지링크
    private var imglink : MutableList<String> = mutableListOf<String>()

    //서버통신
    inner class NetworkThread: Thread() {

        var idnumber = ""

        override fun run() {
            // 접속할 페이지 주소: Site

            var site = "http://"+ netip +"/car_search_by_carid.php?carid="+idnumber
            Log.d("테스트ppp",site)

            var url = URL(site)
            var conn = url.openConnection()
            var input = conn.getInputStream()
            var isr = InputStreamReader(input)
            // br: 라인 단위로 데이터를 읽어오기 위해서 만듦
            var br = BufferedReader(isr)

            // Json 문서는 일단 문자열로 데이터를 모두 읽어온 후, Json에 관련된 객체를 만들어서 데이터를 가져옴
            var str: String? = null
            var buf = StringBuffer()

            do {
                str = br.readLine()

                if (str != null) {
                    buf.append(str)
                }

            } while (str != null)
            var root = JSONObject(buf.toString())


            /* ---------------------------------------------- */
            var result = root.getJSONArray("result")
            var obj2 = result.getJSONObject(0)
            car_id.add(obj2.getString("carid"))
            url_address.add(obj2.getString("url"))
            sitetype.add(obj2.getString("site"))
            car_title.add(obj2.getString("title"))
            carnumber.add(obj2.getString("carnumber"))
            cartype.add(obj2.getString("cartype"))
            manufacturer.add(obj2.getString("manufacturer"))
            model.add(obj2.getString("model"))
            model_detail.add(obj2.getString("model_detail"))
            price.add(obj2.getString("price"))
            distance.add(obj2.getString("distance"))
            caryear.add(obj2.getString("caryear"))
            carcolor.add(obj2.getString("carcolor"))
            carfuel.add(obj2.getString("carfuel"))
            imglink.add(obj2.getString("imglink"))

            var thread1 = uThread()
            thread1.str = obj2.getString("imglink")
            thread1.start()
            thread1.join()
            Detail_bitmap.add(bitmap)
        }
    }

    private fun setStringArrayPref(context: Context, key: String, values: ArrayList<Any>) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until values.size) {
            a.put(values[i])
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString())
        } else {
            editor.putString(key, null)
        }
        editor.apply()
    }

    private fun getStringArrayPref(context: Context, key: String) : ArrayList<Any> {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val json = prefs.getString(key, null)
        val urls = ArrayList<Any>()
        if (json != null) {
            try {
                val a = JSONArray(json)
                for (i in 0 until a.length()) {
                    val url = a.optString(i)
                    urls.add(url)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return urls
    }

    override fun onResume() {
        super.onResume()

        Log.d("테스트테스트","123123123")

        //아이피주소받아오기
        netip = getString(R.string.net_ip)

        var customProgressDialog = ProgressDialog(mycontext)
        //로딩창을 투명하게
        customProgressDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT));
        customProgressDialog.show()

        box = getStringArrayPref(mycontext, "favorite")

        car_id.clear()
        url_address.clear()
        sitetype.clear()
        car_title.clear()
        carnumber.clear()
        cartype.clear()
        manufacturer.clear()
        model.clear()
        model_detail.clear()
        price.clear()
        distance.clear()
        caryear.clear()
        carcolor.clear()
        carfuel.clear()
        imglink.clear()

        dynamic_text.clear()
        dynamic_image.clear()
        dynamic_layout.clear()
        dynamic_button.clear()
        Detail_bitmap.clear()
        scrolllayout?.removeAllViews()
        //뷰 구성
        favorite_layout_make(mycontext, layout)

        customProgressDialog.cancel()
    }
}

