package com.example.capstone

import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class Detail: AppCompatActivity()  {
    lateinit var model :TextView
    lateinit var car_info :TextView
    lateinit var price :TextView

    lateinit var image : ImageView

    //배열과 사진 저장할 공간
    private var Detail_bitmap : MutableList<Bitmap> = mutableListOf<Bitmap>()

    //그림을 저장할 공간
    private lateinit var bitmap : Bitmap

    //필수정보
    private lateinit var use_manufacturer : String
    private lateinit var use_model : String
    private lateinit var use_model_detail : String

    private var count : Int = 0

    // 자동차 ID
    private var Detail_car_id : MutableList<String> = mutableListOf<String>()
    // URL 주소
    private var Detail_url_address : MutableList<String> = mutableListOf<String>()
    // 사이트 타입
    private var Detail_sitetype : MutableList<String> = mutableListOf<String>()
    // title = 자동차 전체 이름 ex(현대 LF 쏘나타 LPI 프리미엄)
    private var Detail_car_title : MutableList<String> = mutableListOf<String>()
    // 자동차 번호
    private var Detail_carnumber : MutableList<String> = mutableListOf<String>()
    // 자동차 타입 ex(중형차)
    private var Detail_cartype : MutableList<String> = mutableListOf<String>()
    // 제조사
    private var Detail_manufacturer : MutableList<String> = mutableListOf<String>()
    // 모델명 ex(쏘나타)
    private var Detail_model : MutableList<String> = mutableListOf<String>()
    // 모델명_자세히 ex(LF 쏘나타)
    private var Detail_model_detail : MutableList<String> = mutableListOf<String>()
    // 가격
    private var Detail_price : MutableList<String> = mutableListOf<String>()
    // 주행거리
    private var Detail_distance : MutableList<String> = mutableListOf<String>()
    // 연식
    private var Detail_caryear : MutableList<String> = mutableListOf<String>()
    // 색깔
    private var Detail_carcolor : MutableList<String> = mutableListOf<String>()
    // 연료타입
    private var Detail_carfuel : MutableList<String> = mutableListOf<String>()
    // 이미지링크
    private var Detail_imglink : MutableList<String> = mutableListOf<String>()
    // 배기량
    private var Detail_displacement : MutableList<String> = mutableListOf<String>()


    //동적할당부분 코드
    //스크롤레이아웃
    private var scrolllayout : LinearLayout? = null
    //동적 배열 (텍스트 및 이미지 및 레이아웃)
    private var dynamic_text : MutableList<TextView> = mutableListOf<TextView>()
    private var dynamic_image : MutableList<ImageView> = mutableListOf<ImageView>()
    private var dynamic_layout : MutableList<LinearLayout> = mutableListOf<LinearLayout>()

    private var dynamic_image_background : MutableList<LinearLayout> = mutableListOf<LinearLayout>()
    private var dynamic_favorite_button : MutableList<Button> = mutableListOf<Button>()

    //얼마나 동적으로 늘릴지 정하는 코드
    private var getcount : Int = 0

    //매물이 하나라도 있나 체크 코드
    private var issaletrue : Boolean = false

    //구 매물
    private var old_sale : ArrayList<String> = ArrayList()
    private var new_sale : ArrayList<String> = ArrayList()


    private lateinit var tmpimg : ImageView

    private lateinit var netip : String

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "매물 비교하기"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail)

        //툴바
        val toolbar = findViewById<View>(R.id.toolbar_detail) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        //아이피주소받아오기
        netip = getString(R.string.net_ip)

        model = findViewById(R.id.detail_model)
        car_info = findViewById(R.id.detail_model_info)
        price = findViewById(R.id.detail_price)
        
        //이미지
        var imglinks : String

        image = findViewById(R.id.detail_image)
        try {
            /*  리스트 표
            * 0. url 주소
            * 1. 사이트 타입 (ex 엔카)
            * 2. 자동차 타이틀 (자동차 전체 이름)
            * 3. 자동차 번호
            * 4. 자동차 타입 (ex 중형차)
            * 5. 제조사
            * 6. 모델
            * 7. 모델_자세히
            * 8. 가격
            * 9. 주행거리
            * 10.연식
            * 11.연료타입 (ex 휘발유)
            * 12.자동차 색
            * 13.이미지링크
            * 14.자동차ID
            * 15.연료
            */

            old_sale = intent.getStringArrayListExtra("old_sale") as ArrayList<String>

            var exchangeformat1000 = ExchangeFormat_1000()
            var exchangeFormat = ExchangeFormat()

            //자동차 타이틀
            model.setTextSize(18f)
            model.setTextColor(Color.BLACK)
            car_info.setTextSize(15f)

            price.setTextSize(20f)
            price.setTextColor(Color.RED)
            price.setTypeface(Typeface.DEFAULT_BOLD)

            //값 변환
            old_sale[9] = exchangeformat1000.unit_change(old_sale[9]) // KM단위 변환
            old_sale[8] = exchangeFormat.unit_change(old_sale[8]) // 가격단위 변환


            model.text = old_sale[2]
            //연식 KM 연료 차량번호
            car_info.text = old_sale[10] + " / " + old_sale[9] + "KM / " + old_sale[11] + " / " + old_sale[3]
            //가격
            price.text = old_sale[8] + "원"

            use_manufacturer = old_sale[5]
            use_model = old_sale[6]
            use_model_detail = old_sale[7]

            try {
                imglinks = old_sale[13]

                var thread1 = uThread()
                thread1.str = imglinks
                thread1.start()
                thread1.join()

                //맨위 이미지에 넣기
                image.setImageBitmap(bitmap)
            } catch (e : Exception){
            }
        } catch (e : Exception){

        }

        //쓰레드를 하고 마칠 때 까지 기다린다.
        var thread = NetworkThreadsss()
        thread.manu_net = use_manufacturer
        thread.model_net = use_model
        thread.model_detail_net = use_model_detail

        thread.start()
        thread.join(5000)


        //찜목록 체크 불러오기
        var caridArray : ArrayList<Any> = getStringArrayPref(applicationContext, "favorite")

        //스크롤 뷰에 추가한다.
        scrolllayout = findViewById(R.id.scroll_inside_linearlayout) as LinearLayout

        for (i in 0 until getcount)
        {
            issaletrue = true
            //레이아웃을 먼저 추가
            dynamic_layout.add(LinearLayout(this))

            //그림 3 : 글씨 2
            //사이즈 설정
            dynamic_layout[i].weightSum = 5f

            //이미지 추가
            dynamic_image_background.add(LinearLayout(this))
            dynamic_image_background[i].weightSum = 3f
            var ob = BitmapDrawable(Detail_bitmap[i])
            dynamic_image_background[i].background = ob

            //이미지레이아웃
            var tmplinear = LinearLayout.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT)
            tmplinear.weight = 3f
            tmplinear.height = 400
            tmplinear.gravity = Gravity.CENTER
            dynamic_image_background[i].layoutParams = tmplinear

            //버튼 추가
            dynamic_favorite_button.add(Button(this))
            var tmpbutton = LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT)
            tmpbutton.width = 100
            tmpbutton.height = 100
            tmpbutton.gravity = Gravity.RIGHT

            dynamic_favorite_button[i].layoutParams = tmpbutton

            //만약에 찜목록에 있다면 채워진하트, 아니면 빈하트
            var is_favorite : Boolean

            is_favorite = caridArray.contains(Detail_car_id[i])
            if (is_favorite){
                dynamic_favorite_button[i].background = getDrawable(R.drawable.heart6)
            }
            else{
                dynamic_favorite_button[i].background = getDrawable(R.drawable.heart5)
            }
            //dynamic_favorite_button[i].text = "버튼"
            dynamic_image_background[i]!!.addView(dynamic_favorite_button[i])

            dynamic_layout[i]!!.addView(dynamic_image_background[i])

            dynamic_favorite_button[i].setOnClickListener {

                var caridArray : ArrayList<Any> = getStringArrayPref(applicationContext, "favorite")

                if(caridArray.contains(Detail_car_id[i])){
                    Toast.makeText(applicationContext,"찜 목록에서 제거했습니다.",Toast.LENGTH_SHORT).show()
                    dynamic_favorite_button[i].background = getDrawable(R.drawable.heart5)
                    //찜목록에 제거
                    caridArray.remove(Detail_car_id[i])
                    setStringArrayPref(applicationContext, "favorite", caridArray)

                }
                else{
                    Toast.makeText(applicationContext,"찜 목록에 추가했습니다.",Toast.LENGTH_SHORT).show()
                    dynamic_favorite_button[i].background = getDrawable(R.drawable.heart6)
                    //찜목록에 추가
                    caridArray.add(0, Detail_car_id[i])
                    setStringArrayPref(applicationContext, "favorite", caridArray)
                }
            }

            //텍스트 추가
            dynamic_text.add(TextView(this))

            //텍스트를 원하는대로 편집하는 부분

            var tmptext = ""
            tmptext = tmptext + "■" + Detail_car_title[i] + "\n"
            tmptext = tmptext + "■" + "연식 :" + Detail_caryear[i] + "\n"
            tmptext = tmptext + "■" + "KM수 :" + Detail_distance[i] + "KM\n"
            tmptext = tmptext + "■" + "가격 :" + Detail_price[i] + "원\n"

            dynamic_text[i].setText(tmptext)
            //볼드체 처리
            dynamic_text[i].setTypeface(null, Typeface.BOLD)

            var tmplayout = LinearLayout.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT)
            tmplayout.weight = 2f
            tmplayout.gravity = Gravity.CENTER
            dynamic_text[i].layoutParams = tmplayout
            //scrolllayout!!.addView(dynamic_text[i])
            dynamic_layout[i]!!.addView(dynamic_text[i])

            scrolllayout!!.addView(dynamic_layout[i])


            //표 만드는 부분
            dynamic_layout[i].setBackgroundResource(R.drawable.border_bottom)
            //클릭 부분
            dynamic_layout[i].setOnClickListener {

                var caridArray : ArrayList<Any> = getStringArrayPref(applicationContext, "history")

                if (caridArray.contains(Detail_car_id[i])) {
                    caridArray.remove(Detail_car_id[i])
                }
                if (caridArray.size >= 20) {
                    caridArray.removeAt(caridArray.size - 1)
                }
                caridArray.add(0, Detail_car_id[i])
                setStringArrayPref(applicationContext, "history", caridArray)

                new_sale.clear()

                new_sale.add(Detail_url_address[i]) //0
                new_sale.add(Detail_sitetype[i]) //1
                new_sale.add(Detail_car_title[i]) //2
                new_sale.add(Detail_carnumber[i]) //3
                new_sale.add(Detail_cartype[i]) //4
                new_sale.add(Detail_manufacturer[i]) //5
                new_sale.add(Detail_model[i]) //6
                new_sale.add(Detail_model_detail[i]) //7
                new_sale.add(Detail_price[i]) //8
                new_sale.add(Detail_distance[i]) //9
                new_sale.add(Detail_caryear[i]) //10
                new_sale.add(Detail_carfuel[i]) //11
                new_sale.add(Detail_carcolor[i]) //12
                new_sale.add(Detail_imglink[i])  //13
                new_sale.add(Detail_car_id[i]) //14
                new_sale.add(Detail_displacement[i])//15

                var nextIntent = Intent(this,Compare::class.java)
                nextIntent.putStringArrayListExtra("new_sale",new_sale)
                nextIntent.putStringArrayListExtra("old_sale",old_sale)
                startActivity(nextIntent)

            }
        }

        if (getcount == 0){
            //텍스트 추가
            var tmp_text = TextView(this)

            //텍스트를 원하는대로 편집하는 부분
            var tmptext = "일치하는 매물이 없습니다."

            tmp_text.text = tmptext

            var tmplayout = LinearLayout.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT)
            tmplayout.weight = 2f
            tmplayout.gravity = Gravity.CENTER
            tmp_text.layoutParams = tmplayout

            var tmp_layout = LinearLayout(this);

            tmp_layout!!.addView(tmp_text)
            scrolllayout!!.addView(tmp_layout)
        }
    }
    //서버통신
    inner class NetworkThreadsss: Thread() {

        var manu_net = ""
        var model_net = ""
        var model_detail_net = ""

        override fun run() {
            // 접속할 페이지 주소: Site
            var site = "http://"+ netip +"/car_search.php?manufacturer="+manu_net+"&model="+model_net+"&model_detail="+model_detail_net

            var url = URL(site)
            var conn = url.openConnection()
            var input = conn.getInputStream()
            var isr = InputStreamReader(input)
            // br: 라인 단위로 데이터를 읽어오기 위해서 만듦
            var br = BufferedReader(isr)

            // Json 문서는 일단 문자열로 데이터를 모두 읽어온 후, Json에 관련된 객체를 만들어서 데이터를 가져옴
            var str: String? = null
            var buf = StringBuffer()

            try{
                do {
                    str = br.readLine()

                    if (str != null) {
                        buf.append(str)
                    }

                } while (str != null)
                //Log.d("태그", "3")
                // 전체가 객체로 묶여있기 때문에 객체형태로 가져옴
                var root = JSONObject(buf.toString())

                //            var userName: String = root.getString("site")
                //var userPhoneNumber: String = root.getString("userPhoneNumber")

                // 객체 안에 있는 stores라는 이름의 리스트를 가져옴
                var result = root.getJSONArray("result")

                //받아오는 데이터의 개수
                var lenofresult = result.length()

                count = lenofresult

                var exchangeformat1000 = ExchangeFormat_1000()
                var exchangeformat = ExchangeFormat()

                for (i in 0 until lenofresult){
                    var obj2 = result.getJSONObject(i)
                    if(old_sale.get(14) != obj2.getString("carid"))
                    {
                        Detail_car_id.add(obj2.getString("carid"))
                        Detail_url_address.add(obj2.getString("url"))
                        Detail_sitetype.add(obj2.getString("site"))
                        Detail_car_title.add(obj2.getString("title"))
                        Detail_carnumber.add(obj2.getString("carnumber"))
                        Detail_cartype.add(obj2.getString("cartype"))
                        Detail_manufacturer.add(obj2.getString("manufacturer"))
                        Detail_model.add(obj2.getString("model"))
                        Detail_model_detail.add(obj2.getString("model_detail"))
                        Detail_price.add(exchangeformat.unit_change(obj2.getString("price")))
                        Detail_distance.add(exchangeformat1000.unit_change(obj2.getString("distance")))
                        Detail_caryear.add(obj2.getString("caryear"))
                        Detail_carcolor.add(obj2.getString("carcolor"))
                        Detail_carfuel.add(obj2.getString("carfuel"))
                        Detail_imglink.add(obj2.getString("imglink"))
                        Detail_displacement.add(obj2.getString("displacement"))
                    }
                }

                for (i in 0 until lenofresult-1){
                    //이미지 처리 부분
                    var thread1 = uThread()
                    thread1.str = Detail_imglink[i]
                    thread1.start()
                    thread1.join()
                    Detail_bitmap.add(bitmap)
                }

                // 밖에 Json길이를 전달해줘야함
                getcount = lenofresult - 1


            }catch(e : Exception){
                Log.d("테스트ppp",e.toString())
            }
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

    //그림 가져오기
    inner class uThread : Thread() {
        var str = ""
        override fun run() {
            val tmp = "http://"+netip+"/" + str
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

    //Toolbar 뒤로가기
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}