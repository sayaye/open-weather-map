package com.sayayes.openweathermap

import android.app.ProgressDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONObject
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_main.view.current_temp
import kotlinx.android.synthetic.main.fragment_main.view.location
import kotlinx.android.synthetic.main.fragment_main.view.max_temp
import kotlinx.android.synthetic.main.fragment_main.view.min_temp
import kotlinx.android.synthetic.main.fragment_main.view.weather
import kotlinx.android.synthetic.main.weather_main.*
import com.android.volley.VolleyError
import android.content.SharedPreferences
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.weather_item_main.view.*
import org.json.JSONException
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList



class MainActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    //list of cities , lat, lon
    val sydney:CityModel=CityModel("Sydney","-33.868","151.207")
    val perth:CityModel=CityModel("Perth","-31.933","115.833")
    val hobart:CityModel=CityModel("Hobart","-42.879","147.329")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_main)
        MainActivity.appContext = applicationContext

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

    }

    fun cityToUrl(cityModel:CityModel):String {

        val city=cityModel.city
        val lat=cityModel.lat
        val lon=cityModel.lon
        val url: String = "https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=6ee4fe3756b94dc8328595d892b12b4e&units=metric"

        return url

    }

    fun cityToForecastUrl(cityModel:CityModel):String {

        val city=cityModel.city
        val lat=cityModel.lat
        val lon=cityModel.lon

        val url: String = "https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&exclude=hourly&appid=6ee4fe3756b94dc8328595d892b12b4e&units=metric"

        return url
    }


    fun getDate(int: Int):String{
        val calendar = Calendar.getInstance()
        val today = calendar.time

        //get i number
        calendar.add(Calendar.DAY_OF_YEAR, 1+int)
        val date = calendar.time

        //get which day of week, date
        val date_formate = SimpleDateFormat("dd")
        val date_name = date_formate.format(date)

        val cal = Calendar.getInstance()
        val month_formate = SimpleDateFormat("MMM")
        val month_name = month_formate.format(date)

        val weekday_name = SimpleDateFormat("EEEE", Locale.ENGLISH).format(date)


        val string= weekday_name +", "+ date_name + " "+month_name

        return string
    }



    //temp to int,add degree icon
    fun temperatureProcessing(string:String):String {

        //string to decimal
        //decimal to int
        //int to string, add degree icon
        val parsedInt = string.toDouble().toInt()
        val processedString =parsedInt.toString()+"°"
        return processedString
    }

    var dialog: ProgressDialog? = null


    fun showDialog() {

        weather_main.setVisibility(View.INVISIBLE);

        if (dialog == null) {
            dialog = ProgressDialog(this)
        }
        dialog!!.setMessage("Searching")
        dialog!!.show()
    }

    fun hideDialog() {
        weather_main.setVisibility(View.VISIBLE);


        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }

    companion object {
        lateinit  var appContext: Context
    }










    //----------------------------------------------
    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {

        var loadedData = false


        override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.fragment_main, container, false)

            //get shared preference array list
            var mainActivity: MainActivity = activity as MainActivity



            if(loadedData==false) {
                mainActivity.showDialog()
            }

            val i =getString(R.string.section_format, arguments?.getInt(ARG_SECTION_NUMBER))
            if(i.equals("1")){

                getData(mainActivity.sydney)
                getForecastWeatherData(mainActivity.sydney)


            }
            if(i.equals("2")){
                getData(mainActivity.perth)
                getForecastWeatherData(mainActivity.perth)


            }
            if(i.equals("3")){
                getData(mainActivity.hobart)
                getForecastWeatherData(mainActivity.hobart)


            }


            //add list recycler view
                rootView.weather_list  as RecyclerView
                rootView.weather_list.layoutManager = LinearLayoutManager(getActivity()) // LayoutManager RecyclerView


            return rootView
        }



        fun PopCurrentDataToView(model:CurrentWeatherModel){
            var mainActivity: MainActivity = activity as MainActivity

            val model_temp = mainActivity.temperatureProcessing(model.main.temp)
            val model_temp_max = mainActivity.temperatureProcessing(model.main.temp_max)
            val model_temp_min = mainActivity.temperatureProcessing(model.main.temp_min)
            val model_description = model.weather[0].main

            location.text = model.cityName
            current_temp.text = model_temp
            max_temp.text = model_temp_max
            min_temp.text = model_temp_min
            weather.text = model_description
        }

        fun PopForecastDataToView(forecastWeatherModel:ForecastWeatherModel){
            val dailyArrayList = forecastWeatherModel.daily.filterIndexed { index, value -> index <= 2 }
            val context = MainActivity.appContext
            val adapter = FeedAdapter(dailyArrayList, context, { itemClick ->
                Unit
                Log.i("Selected", "1")
            })
            weather_list.adapter = adapter // set FeedAdapter

        }

        fun getData(cityModel: CityModel){
            var mainActivity: MainActivity = activity as MainActivity

            val url=mainActivity.cityToUrl(cityModel)
            val city =cityModel.city


            mainActivity.getCurrentWeather(url, object: VolleyStringResponse {

                override fun onSuccess(response: String?) {
                    var strResp = response.toString()
                    val jsonObj: JSONObject = JSONObject(strResp)

                    val gson = Gson()
                    val model=gson.fromJson(jsonObj.toString(), CurrentWeatherModel::class.java)
                    model.cityName=city


                    //load view
                    PopCurrentDataToView(model)



                }

                override fun onError(error: VolleyError?) {
                }

            })

        }



        fun getForecastWeatherData(cityModel: CityModel){
            var mainActivity: MainActivity = activity as MainActivity

            val url=mainActivity.cityToForecastUrl(cityModel)
            val city =cityModel.city


            mainActivity.getForecastWeather(url, object: VolleyStringResponse {

                override fun onSuccess(response: String?) {

                    var strResp = response.toString()


                    val jsonObj: JSONObject = JSONObject(strResp)

                    val gson = Gson()
                    val forecastWeatherModel=gson.fromJson(jsonObj.toString(), ForecastWeatherModel::class.java)


                    //add city name, date, convert temperature to degree
                    forecastWeatherModel.cityName=city

                    for(i in forecastWeatherModel.daily.indices) {
                        forecastWeatherModel.daily[i].date = mainActivity.getDate(i)
                        forecastWeatherModel.daily[i].temp.max_string=mainActivity.temperatureProcessing( forecastWeatherModel.daily[i].temp.max)
                        forecastWeatherModel.daily[i].temp.min_string=mainActivity.temperatureProcessing( forecastWeatherModel.daily[i].temp.min)

                    }

                    //load list
                    PopForecastDataToView(forecastWeatherModel)


                    mainActivity.hideDialog()
                    loadedData=true

                }

                override fun onError(error: VolleyError?) {
                }

            })

        }


        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

















    //-----------------------------------------------------
    //volley api call
    //interface
    interface VolleyStringResponse {
        fun onSuccess(response: String?)
        fun onError(error: VolleyError?)
    }

    fun getCurrentWeather(url: String?, volleyResponse: VolleyStringResponse) {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, url, object: Response.Listener<String?> {
            override fun onResponse(response: String?) {
                volleyResponse.onSuccess(response)
            }
        }, object: Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError?) {
                volleyResponse.onError(error)
            }
        })
        queue.add(stringRequest)
    }



    fun getForecastWeather(url: String?, volleyResponse: VolleyStringResponse) {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, url, object: Response.Listener<String?> {
            override fun onResponse(response: String?) {
                volleyResponse.onSuccess(response)
            }
        }, object: Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError?) {
                volleyResponse.onError(error)
            }
        })
        queue.add(stringRequest)
    }






}
