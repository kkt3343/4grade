package com.example.capstone

import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.preference.PreferenceManager
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import java.lang.Math.abs
import java.lang.Math.round
import java.net.*
import kotlin.math.roundToInt
import org.json.JSONException

import org.json.JSONArray
import javax.annotation.Nullable
import android.text.TextUtils

import androidx.core.graphics.drawable.toBitmap
import kotlin.Exception


class Search_ResultActivity : Fragment() {

    //맨위 상단 텍스트
    private lateinit var scrollview : ScrollView

    private var Detail_bitmap : MutableList<Bitmap> = mutableListOf<Bitmap>()

    //그림을 저장할 공간
    private lateinit var bitmap: Bitmap

    //동적할당부분 코드
    //스크롤레이아웃
    private var scrolllayout : LinearLayout? = null
    //동적 배열 (텍스트 및 이미지 및 레이아웃)

    //연식, 주행거리, 가격
    private var dynamic_text_1 : MutableList<TextView> = mutableListOf<TextView>()
    private var dynamic_text_2 : MutableList<TextView> = mutableListOf<TextView>()
    private var dynamic_text_3 : MutableList<TextView> = mutableListOf<TextView>()
    private var dynamic_text_4 : MutableList<TextView> = mutableListOf<TextView>()

    private var dynamic_layout : MutableList<LinearLayout> = mutableListOf<LinearLayout>()

    private var dynamic_image_background : MutableList<LinearLayout> = mutableListOf<LinearLayout>()
    var dynamic_favorite_button : MutableList<Button> = mutableListOf<Button>()

    //얼마나 동적으로 늘릴지 정하는 코드
    private var getcount : Int = 0


    //평균정보 받아올 변수
    private lateinit var avg_count : String
    private lateinit var avg_price : String
    private lateinit var avg_distance : String

    private var avgpriceINT : Int = 0
    private var avgdistanceINT : Int = 0

    private lateinit var netip : String

    //몇개씩 보기 (Ex 만약에 5면 한 페이지에 5개씩 매물이 나온다.)
    private var view_number : Int = 3
    private var page_number : Int = 1
    private var page_count  : Int = 0

    private lateinit var prev_btn : Button
    private lateinit var next_btn : Button
    private var search_result_button : MutableList<Button> = mutableListOf<Button>()


    lateinit var caridArray_history : ArrayList<Any>
    lateinit var caridArray_favorite : ArrayList<Any>

    private var refresh_btn : Button? = null

    //결과없음
    var no_result = false

    //정렬
    var ordertype : String = "0"

    @Nullable
    @Override
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, saveInstanceState: Bundle?): View? {

        //뷰를 가져온다. (findviewmyid 사용)
        var layout = inflater.inflate(R.layout.search_result, container, false)
        //getApplicationContext대신해서 사용
        var context = container!!.context

        //아이피주소받아오기
        netip = getString(R.string.net_ip)

        //쓰레드 할 때 사용 변수
        var manu_out = ""
        var model_out = ""
        var model_detail_out = ""

        //값 전달받는부분 (맨 위 상단)
        try{
            //변수전달
            manu_out = arguments?.getString("manufacturer").toString()
            model_out = arguments?.getString("model").toString()
            model_detail_out = arguments?.getString("model_detail").toString()
        } catch (e : Exception){
        }

        var title = ""
        if(model_out == "NULL"){
            title = manu_out
        }
        else{
            if(model_detail_out == "NULL"){
                title = manu_out + " " + model_out
            }
            else{
                title = manu_out + " " + model_out + "/" + model_detail_out
            }
        }

        //툴바 22.03.17
        (activity as MainActivity?)!!.title = title
        (activity as MainActivity?)!!.search_result_activity_name = title
        (activity as MainActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김


        /* 평균 정보 받아오는 부분 */
        var car_count = layout.findViewById<TextView>(R.id.search_result_activty_show_car_count)

        //스피너 설정
        val view_number_spinner_arraylist = java.util.ArrayList<String>()
        view_number_spinner_arraylist.add("5개 씩 보기")
        view_number_spinner_arraylist.add("10개 씩 보기")
        view_number_spinner_arraylist.add("15개 씩 보기")
        view_number_spinner_arraylist.add("20개 씩 보기")

        var view_number_spinner = layout.findViewById<Spinner>(R.id.view_number_spinner)
        var view_number_spinner_adapter : ArrayAdapter<String> = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, view_number_spinner_arraylist)
        view_number_spinner_adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        view_number_spinner.setAdapter(view_number_spinner_adapter)

        view_number_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position==0){
                    page_number = 1
                    view_number = 5
                    spinner_accept(layout, context, manu_out, model_out, model_detail_out, car_count)
                }
                else if(position==1){
                    page_number = 1
                    view_number = 10
                    spinner_accept(layout, context, manu_out, model_out, model_detail_out, car_count)
                }
                else if(position==2){
                    page_number = 1
                    view_number = 15
                    spinner_accept(layout, context, manu_out, model_out, model_detail_out, car_count)
                }
                else if(position==3){
                    page_number = 1
                    view_number = 20
                    spinner_accept(layout, context, manu_out, model_out, model_detail_out, car_count)
                }
                else{
                }
                //나중에 하기
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        //스피너 설정 (정렬)
        val view_sort_spinner_array_list = java.util.ArrayList<String>()
        view_sort_spinner_array_list.add("최근 등록순")
        view_sort_spinner_array_list.add("높은 가격순")
        view_sort_spinner_array_list.add("낮은 가격순")
        view_sort_spinner_array_list.add("높은 주행거리순")
        view_sort_spinner_array_list.add("낮은 주행거리순")

        var view_sort_spinner = layout.findViewById<Spinner>(R.id.view_sort_spinner)
        var view_sort_spinner_adapter : ArrayAdapter<String> = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, view_sort_spinner_array_list)
        view_sort_spinner_adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        view_sort_spinner.setAdapter(view_sort_spinner_adapter)

        view_sort_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                page_number = 1
                if(position==0){ //최근등록순
                    ordertype = "0"
                    spinner_accept(layout, context, manu_out, model_out, model_detail_out, car_count)
                }
                else if(position==1){ //높은가격순
                    ordertype = "1"
                    spinner_accept(layout, context, manu_out, model_out, model_detail_out, car_count)
                }
                else if(position==2){ //낮은가격순
                    ordertype = "2"
                    spinner_accept(layout, context, manu_out, model_out, model_detail_out, car_count)
                }
                else if(position==3){ //높은주행거리순
                    ordertype = "3"
                    spinner_accept(layout, context, manu_out, model_out, model_detail_out, car_count)
                }
                else if(position==4){ //낮은주행거리순
                    ordertype = "4"
                    spinner_accept(layout, context, manu_out, model_out, model_detail_out, car_count)
                }
                else{
                }
                //나중에 하기
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        return layout
    }

    fun item_load(manu_out : String, model_out : String, model_detail_out : String){
        //쓰레드를 하고 마칠 때 까지 기다린다.
        var thread = NetworkThread()
        thread.manu_net = manu_out
        thread.model_net = model_out
            thread.model_detail_net = model_detail_out

            thread.start()
            thread.join(5000)

            if (avg_count == "0"){
                avgpriceINT = 0
                avgdistanceINT = 0
            }
            else{
                avgpriceINT = avg_price.toDouble().roundToInt()
            avgdistanceINT = avg_distance.toDouble().roundToInt()
        }
    }

    fun layout_load(context: Context, layout : View, unitnumber : ExchangeFormat, unitnumber2 : ExchangeFormat_1000){
        //검색기록과 찜목록 데이터 불러오기
        caridArray_favorite = getStringArrayPref(context, "favorite")

        //동적할당 부분
        //스크롤 뷰에 추가한다.
        scrolllayout?.removeAllViews() // 필요함 (없으면 스크롤 넘어갈 때 제거가 안됨)
        scrolllayout = layout.findViewById(R.id.search_result_linearlayout) as LinearLayout

        for (i in 0 until getcount)
        {
            //레이아웃을 먼저 추가
            dynamic_layout.add(LinearLayout(context))
            //세로로 설정
            dynamic_layout[i].orientation = LinearLayout.VERTICAL

            var dynamic_layout_params = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 0)
            dynamic_layout_params.height = 1000
            dynamic_layout_params.gravity = Gravity.CENTER
            dynamic_layout[i].layoutParams = dynamic_layout_params

            //비율설정 그림 6 , 연식 1, 주행거리 1, 가격 2
            dynamic_layout[i].weightSum = 10f

            //이미지 리니어 레아아웃 추가 (찜 버튼과 함께 존재)
            dynamic_image_background.add(LinearLayout(context))
            dynamic_image_background[i].background = BitmapDrawable(Detail_bitmap[i])
            dynamic_image_background[i].orientation = LinearLayout.VERTICAL

            //이미지레이아웃
            var tmplinear = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 0)
            tmplinear.width = ActionBar.LayoutParams.MATCH_PARENT
            tmplinear.weight = 6f
            tmplinear.gravity = Gravity.CENTER
            dynamic_image_background[i].layoutParams = tmplinear

            //버튼 추가 ----- 찜목록 버튼 -----
            dynamic_favorite_button.add(Button(context))
            var tmpbutton = LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT)
            tmpbutton.width = 100
            tmpbutton.height = 100
            tmpbutton.gravity = Gravity.LEFT

            dynamic_favorite_button[i].layoutParams = tmpbutton

            //만약에 찜목록에 있다면 채워진하트, 아니면 빈하트
            var is_favorite = caridArray_favorite.contains(car_id[i])
            if (is_favorite){
                dynamic_favorite_button[i].background = resources.getDrawable(R.drawable.heart6)
            }
            else{
                dynamic_favorite_button[i].background = resources.getDrawable(R.drawable.heart5)
            }
            // # 초기화 # 22.03.19 no3
            //할당이 되어있다면 먼저 제거를 한다.
            if(dynamic_favorite_button[i].parent != null){
                (dynamic_favorite_button[i].parent as ViewGroup).removeView(dynamic_favorite_button[i])
            }
            dynamic_image_background[i]!!.addView(dynamic_favorite_button[i])

            // # 초기화 # 22.03.19 no4
            //할당이 되어있다면 먼저 제거를 한다.
            if(dynamic_image_background[i].parent != null){
                (dynamic_image_background[i].parent as ViewGroup).removeView(dynamic_image_background[i])
            }
            dynamic_layout[i]!!.addView(dynamic_image_background[i])

            dynamic_favorite_button[i].setOnClickListener {
                if(caridArray_favorite.contains(car_id[i])){
                    Toast.makeText(context,"찜 목록에서 제거했습니다.",Toast.LENGTH_SHORT).show()
                    dynamic_favorite_button[i].background = resources.getDrawable(R.drawable.heart5)
                    //찜목록에 제거
                    caridArray_favorite.remove(car_id[i])
                    setStringArrayPref(context, "favorite", caridArray_favorite)

                    accept_favorite()
                }
                else{
                    Toast.makeText(context,"찜 목록에 추가했습니다.",Toast.LENGTH_SHORT).show()
                    dynamic_favorite_button[i].background = resources.getDrawable(R.drawable.heart6)
                    //찜목록에 추가
                    caridArray_favorite.add(0, car_id[i])
                    setStringArrayPref(context, "favorite", caridArray_favorite)

                    accept_favorite()
                }
            }

            //텍스트 부분 설정
            //텍스트 추가
            //이름, 연식, 주행거리, 가격
            dynamic_text_1.add(TextView(context))
            dynamic_text_2.add(TextView(context))
            dynamic_text_3.add(TextView(context))
            dynamic_text_4.add(TextView(context))

            //폰트설정
            var assets = resources.assets
            var typeface = Typeface.createFromAsset(assets, "nexon1.otf" )
            dynamic_text_1[i].setTypeface(typeface)
            dynamic_text_2[i].setTypeface(typeface)
            dynamic_text_3[i].setTypeface(typeface)
            dynamic_text_4[i].setTypeface(typeface)
            dynamic_text_1[i].textSize = 20f
            dynamic_text_1[i].setTextColor(Color.BLACK)
            dynamic_text_1[i].setTypeface(Typeface.DEFAULT_BOLD)

            //한줄로 출력하게 설정
            dynamic_text_1[i].setSingleLine(true); //한줄로 나오게 하기.
            dynamic_text_1[i].setEllipsize(TextUtils.TruncateAt.MARQUEE);//Ellipsize의 MARQUEE 속성 주기
            dynamic_text_1[i].setSelected(true); //해당 텍스트뷰에 포커스가 없어도 문자 흐르게 하기

            dynamic_text_2[i].setSingleLine(true); //한줄로 나오게 하기.
            dynamic_text_2[i].setEllipsize(TextUtils.TruncateAt.END);//Ellipsize의 END 속성 주기
            dynamic_text_2[i].setSelected(true); //해당 텍스트뷰에 포커스가 없어도 문자 흐르게 하기

            dynamic_text_3[i].setSingleLine(true); //한줄로 나오게 하기.
            dynamic_text_3[i].setEllipsize(TextUtils.TruncateAt.END);//Ellipsize의 END 속성 주기
            dynamic_text_3[i].setSelected(true); //해당 텍스트뷰에 포커스가 없어도 문자 흐르게 하기

            dynamic_text_4[i].setSingleLine(true); //한줄로 나오게 하기.
            dynamic_text_4[i].setEllipsize(TextUtils.TruncateAt.MARQUEE);//Ellipsize의 END 속성 주기
            dynamic_text_4[i].setSelected(true); //해당 텍스트뷰에 포커스가 없어도 문자 흐르게 하기

            // ***** 이름 *****
            var text1 = "\t" + car_title[i] + "\n"
            dynamic_text_1[i].setText(text1)

            if(dynamic_text_1[i].parent != null){
                (dynamic_text_1[i].parent as ViewGroup).removeView(dynamic_text_1[i])
            }
            var tmplayout = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 0)
            tmplayout.weight = 1f
            tmplayout.gravity = Gravity.CENTER
            dynamic_text_1[i].layoutParams = tmplayout
            dynamic_layout[i]!!.addView(dynamic_text_1[i])

            // ***** 연식 ******
            var text2 = "\t" + "" + caryear[i] + "\n"
            dynamic_text_2[i].setText(text2)

            if(dynamic_text_2[i].parent != null){
                (dynamic_text_2[i].parent as ViewGroup).removeView(dynamic_text_2[i])
            }
            tmplayout = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 0)
            tmplayout.weight = 1f
            tmplayout.gravity = Gravity.CENTER
            dynamic_text_2[i].layoutParams = tmplayout
            dynamic_layout[i]!!.addView(dynamic_text_2[i])

            // ***** 주행거리 ****** ------------------------------------------------------------------
            var text3 = ""

            var avg_distanceINT = avg_distance.toFloat().toInt()
            var disgap = (distance[i].toInt() - avg_distanceINT)

            var disper = 0f
            disper = round(((disgap / (avg_distanceINT).toFloat()) * 100) * 100)/100.toFloat()

            //거리데이터를 ###,###으로 바꾼다.
            var showdistance = unitnumber2.unit_change(distance[i])
            var showdisgap =  unitnumber2.unit_change(disgap.toString())

            var KM_string1 = ""
            var KM_string2 = ""

            KM_string1 = showdistance + "KM "
            text3 += "\t" + "" + KM_string1


            var text3_span : SpannableString
            text3_span = textcolorchange(SpannableString(text3), KM_string1, "#000000", 1.3f)

//            if(avg_distanceINT < distance[i].toInt()){
//                KM_string2 = "(+" + showdisgap + "KM) (" + abs(disper) + "% 높음)\n"
//                text3 += KM_string2
//
//                text3_span = textcolorchange(SpannableString(text3), KM_string1, "#000000", 1.3f)
//                text3_span = textcolorchange(SpannableString(text3_span), KM_string2, "#79ABFF", 1.1f)
//
//            }
//            else{
//                KM_string2 = "(" + showdisgap + "KM) (" + abs(disper) + "% 낮음)\n"
//                text3 += KM_string2
//
//                text3_span = textcolorchange(SpannableString(text3), KM_string1, "#000000", 1.3f)
//                text3_span = textcolorchange(SpannableString(text3_span), KM_string2, "#47C83E", 1.1f)
//
//            }

            dynamic_text_3[i].setText(text3_span)

            if(dynamic_text_3[i].parent != null){
                (dynamic_text_3[i].parent as ViewGroup).removeView(dynamic_text_3[i])
            }
            tmplayout = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 0)
            tmplayout.weight = 1f
            tmplayout.gravity = Gravity.CENTER
            dynamic_text_3[i].layoutParams = tmplayout
            dynamic_layout[i]!!.addView(dynamic_text_3[i])

            // ***** 가격 ****** ------------------------------------------------------------------
            var text4 = ""

            //var avg_priceINT = avg_price.toFloat().toInt()

            // 예상가격
            var expected_price = 0
            var text4_span : SpannableString


            //가격데이터를 xx만xxxx원으로 변환한다.
            var showprice = unitnumber.unit_change(price[i]) // 가격변환
            var Price_string1 = ""
            var Price_string2 = ""
            Price_string1 = showprice + "원 "
            text4 += "\t" + "" + Price_string1 + "\n"

            if(coef[i] == "" || intercept[i] == ""){
                text4_span = textcolorchange(SpannableString(text4), Price_string1, "#000000", 1.3f)
            }
            else{
                expected_price = (distance[i].toInt() * coef[i].toDouble() + intercept[i].toDouble()).roundToInt()

                var prigap = (price[i].toInt() - expected_price)
                var priper = 0f
                priper = round(((prigap / (expected_price).toFloat()) * 100) * 100)/100.toFloat()
                var showprigap =  unitnumber.unit_change(prigap.toString())

                if(expected_price < price[i].toInt()){
                    Price_string2 = "(+" + showprigap + "원) (" + abs(priper) + "% 높음)\n"
                    text4 += Price_string2

                    text4_span = textcolorchange(SpannableString(text4), Price_string1, "#000000", 1.3f)
                    text4_span = textcolorchange(SpannableString(text4_span), Price_string2, "#F15F5F", 1.1f)

                }
                else{
                    Price_string2 = "(" + showprigap + "원) (" + abs(priper) + "% 낮음)\n"
                    text4 += Price_string2

                    //초록색 : #47C83E 파란색 : #79ABFF 빨간색 : #F15F5F
                    text4_span = textcolorchange(SpannableString(text4), Price_string1, "#000000", 1.3f)
                    text4_span = textcolorchange(SpannableString(text4_span), Price_string2, "#79ABFF", 1.1f)
                }
            }
            dynamic_text_4[i].setText(text4_span)

            if(dynamic_text_4[i].parent != null){
                (dynamic_text_4[i].parent as ViewGroup).removeView(dynamic_text_4[i])
            }
            tmplayout = LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 0)
            tmplayout.weight = 1f
            tmplayout.gravity = Gravity.CENTER
            dynamic_text_4[i].layoutParams = tmplayout
            dynamic_layout[i]!!.addView(dynamic_text_4[i])


            // ***** 스크롤뷰에 추가 ***** ------------------------------------------------------------------
            if(dynamic_layout[i].parent != null){
                (dynamic_layout[i].parent as ViewGroup).removeView(dynamic_layout[i])
            }
            scrolllayout!!.addView(dynamic_layout[i])

            //표 만드는 부분
            dynamic_layout[i].setBackgroundResource(R.drawable.border_bottom)
            //클릭 부분 (검색기록)
            dynamic_layout[i].setOnClickListener {
                //조회수 전달하기 22.04.09
                var update_views = _okhttp_connect()
                update_views.update_views(car_id[i], "http://" + getString(R.string.net_ip) + "/update_views.php")


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
                nextIntent.putExtra("displacement",displacement[i]) //15
                startActivity(nextIntent)
            }
        }
    }

    //바꿀 글자의 전체값, 바꾸고 싶은 구간, 색, 폰트사이즈
    fun textcolorchange(fulltext : SpannableString, change_text : String, color : String, size : Float) : SpannableString{
        var colortext_start = fulltext.indexOf(change_text)
        var colortext_end = colortext_start + change_text.length
        /*
            setSpan (속성, 시작 위치, 끝 위치, 플래그)
            ForegroundColorSpan : 글자 색상 지정. (BackgroundColorSpan : 배경색 지정)
            StyleSpan : 글자의 스타일 지정 (BOLD, ITALIC 등)
            RelativeSizeSpan : 글자의 상대적 크기 지정 (1.3f는 1.3배)
        */
        fulltext.setSpan(ForegroundColorSpan(Color.parseColor(color)), colortext_start, colortext_end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        fulltext.setSpan(StyleSpan(Typeface.BOLD), colortext_start, colortext_end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        fulltext.setSpan(RelativeSizeSpan(size), colortext_start, colortext_end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        return fulltext
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

    // 기울기
    private var coef : MutableList<String> = mutableListOf<String>()
    // y절편
    private var intercept : MutableList<String> = mutableListOf<String>()

    //배기량
    private var displacement : MutableList<String> = mutableListOf<String>()

    //서버통신
    inner class NetworkThread: Thread() {

        var manu_net = ""
        var model_net = ""
        var model_detail_net = ""

        override fun run() {
            // 접속할 페이지 주소: Site

            //var site = "http://"+ netip +"/car_search.php?manufacturer="+manu_net+"&model="+model_net+"&model_detail="+model_detail_net
            var site = "http://"+ netip +"/car_page_search.php?manufacturer="+manu_net+"&model="+model_net+"&model_detail="+model_detail_net+
                    "&view_number="+view_number+"&page_number="+page_number+"&order_type="+ordertype
            Log.d("테스트site",site)

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
            //Log.d("태그", "3")
            // 전체가 객체로 묶여있기 때문에 객체형태로 가져옴
            var root = JSONObject(buf.toString())


            /* 차량 개수 먼저 받아오기 */

            var result2 = root.getJSONArray("result2")
            var obj2 = result2.getJSONObject(0)
            avg_count = obj2.getString("count")
            avg_price = obj2.getString("priceAVG")
            avg_distance = obj2.getString("distAVG")

            /* ---------------------------------------------- */

            if (avg_count.toInt() != 0) {
                try {
                    // 객체 안에 있는 stores라는 이름의 리스트를 가져옴
                    var result = root.getJSONArray("result")

                    //받아오는 데이터의 개수
                    var lenofresult = result.length()

                    //너무 많으면 속도저하가 있기 때문에, 최초에 view_number 개만 통신한다.
                    if (lenofresult > view_number){
                        lenofresult = view_number
                    }
                    //THIS
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
                    Detail_bitmap.clear()
                    displacement.clear()

                    coef.clear()
                    intercept.clear()

                    Log.d("태그",car_id.size.toString())

                    for (i in 0 until lenofresult){
                        var obj2 = result.getJSONObject(i)
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

                        //배기량
                        displacement.add(obj2.getString("displacement"))

                        coef.add(obj2.getString("coef"))
                        intercept.add(obj2.getString("intercept"))

                    }

                    for (i in 0 until lenofresult){
                        //이미지 처리 부분
                        var thread1 = uThread()
                        thread1.str = imglink[i]
                        thread1.start()
                        try{
                            thread1.join()
                            Detail_bitmap.add(bitmap)
                            //val t = resources.getDrawable(R.drawable.a)
                            //Detail_bitmap.add(t.toBitmap())
                        } catch ( e : Exception){
                            val t = resources.getDrawable(R.drawable.a)
                            Detail_bitmap.add(t.toBitmap())
                        }
                    }
                    // 밖에 Json길이를 전달해줘야함
                    getcount = lenofresult
                } catch (e : Exception){
                    //개수 바깥으로 나감
                    getcount = 0
                    no_result = true
                }
            }
            else{
                //결과없음
                getcount = 0
                no_result = true
            }
        }
    }

    //찜기능 구현사용
    fun setStringArrayPref(context: Context, key: String, values: ArrayList<Any>) {
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

    fun getStringArrayPref(context: Context, key: String) : ArrayList<Any> {
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
    
    // n개씩 보기
    fun spinner_accept(layout: View, context: Context, manu_out : String, model_out : String, model_detail_out : String, car_count : TextView){
        //쓰레드를 하고 마칠 때 까지 기다린다.
        item_load(manu_out, model_out, model_detail_out)

        // ----------------------------------------//

        //거리 & 가격 변수
        var unitnumber = ExchangeFormat();
        var unitnumber2 = ExchangeFormat_1000();

        if(no_result){
            car_count.text = "결과가 없습니다."
            car_count.setTextColor(Color.parseColor("#F15F5F"))
        }else{
            car_count.text = "검색 결과 총 " + avg_count + "대 존재합니다."
            car_count.setTextColor(Color.parseColor("#6B9900"))
        }

        // --------------------------------------------------------------- //
        //하단 레이아웃 로드
        layout_load(context, layout, unitnumber , unitnumber2)

        // 페이지 버튼 설정
        set_pageButton(layout, context, manu_out, model_out, model_detail_out, car_count)
    }
    fun set_pageButton(layout: View, context: Context, manu_out : String, model_out : String, model_detail_out : String, car_count : TextView) {

        //스크롤뷰 최상단으로 이동하기 위한 코드 22.03.20
        scrollview = layout.findViewById<ScrollView>(R.id.search_result_scrollview)
        scrollview.fullScroll(ScrollView.FOCUS_UP)

        var item_num = avg_count.toInt()

        if (item_num % view_number == 0 && item_num > 0) {
            page_count = item_num / view_number
        } else {
            page_count = item_num / view_number + 1
        }

        // 버튼 1,2,3,4,5 비우기
        search_result_button.clear()

        //하단 페이지 버튼 설정
        prev_btn = layout.findViewById(R.id.search_result_bottom1)
        search_result_button.add(layout.findViewById(R.id.search_result_bottom2)) //1
        search_result_button.add(layout.findViewById(R.id.search_result_bottom3)) //2
        search_result_button.add(layout.findViewById(R.id.search_result_bottom4)) //3
        search_result_button.add(layout.findViewById(R.id.search_result_bottom5)) //4
        search_result_button.add(layout.findViewById(R.id.search_result_bottom6)) //5
        next_btn = layout.findViewById(R.id.search_result_bottom7)

        for (i in 0 until 5) {
            search_result_button[i].setVisibility(View.VISIBLE)
        }
        prev_btn.setVisibility(View.VISIBLE)
        next_btn.setVisibility(View.VISIBLE)

        refresh_btn = search_result_button[(page_number-1) % 5]

        for (i in 0 until 5) {
            var button_num = page_number - (page_number - 1) % 5 + i
            search_result_button[i].text = (button_num).toString()
            if (button_num > page_count) {
                search_result_button[i].setVisibility(View.GONE)
            }
        }
        if (page_number <= 5) {
            prev_btn.setVisibility(View.GONE)
        }
        if ((page_number - (page_number - 1) % 5 + 5) > page_count) {
            next_btn.setVisibility(View.GONE)
        }

        search_result_button[(page_number - 1) % 5].setBackgroundResource(R.drawable.btn_normal_click)
        for (i in 0 until 5) {
            if (i != (page_number - 1) % 5) {
                search_result_button[i].setBackgroundResource(R.drawable.btn_normal)
            }
        }

        var unitnumber = ExchangeFormat();
        var unitnumber2 = ExchangeFormat_1000();

        for (i in 0 until 5) {
            search_result_button[i].setOnClickListener {
                page_number = search_result_button[i].text.toString().toInt()
                item_load(manu_out, model_out, model_detail_out)
                layout_load(context, layout, unitnumber, unitnumber2)
                set_pageButton(layout, context, manu_out, model_out, model_detail_out, car_count)
            }
        }

        prev_btn.setOnClickListener {
            page_number = page_number - (page_number - 1) % 5 - 5
            item_load(manu_out, model_out, model_detail_out)
            layout_load(context, layout, unitnumber, unitnumber2)
            set_pageButton(layout, context, manu_out, model_out, model_detail_out, car_count)
        }
        //다음
        next_btn.setOnClickListener {
            page_number = page_number - (page_number - 1) % 5 + 5
            item_load(manu_out, model_out, model_detail_out)
            layout_load(context, layout, unitnumber, unitnumber2)
            set_pageButton(layout, context, manu_out, model_out, model_detail_out, car_count)
        }
    }

    var mainactivity: MainActivity? = null

    //찜목록 등록시 찜목록 프래그먼트 새로고침 해야함
    fun accept_favorite() {
        mainactivity?.bottom3 = null
    }

    //검색기록 초기화
    fun accept_history() {
        mainactivity?.bottom2 = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainactivity = activity as MainActivity?
    }

    override fun onDetach() {
        super.onDetach()
        mainactivity = null
    }

    //프래그먼트 상태가 바뀔 때마다 호출
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        //프래그먼트 하이드 상태일때 동작
        if(isHidden) {
        }
        else{
            refresh_btn?.callOnClick()
        }
    }

    override fun onResume() {
        super.onResume()
        if(refresh_btn==null){

        }else{
            refresh_btn!!.callOnClick()
        }
    }
}

