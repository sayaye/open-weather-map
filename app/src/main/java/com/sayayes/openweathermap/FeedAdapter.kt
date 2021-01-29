package com.sayayes.openweathermap

import android.support.v7.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Context
import kotlinx.android.synthetic.main.weather_item_main.view.*
import java.text.SimpleDateFormat
import java.util.*



class FeedAdapter(val feedModelItems: List<Daily>,c: Context, val itemClick: (Daily) -> Unit)
    : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bindFeedModel( feedModelItems[position] )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_item_main, parent, false)
        return ViewHolder(view, itemClick)
    }//ViewHolder

    override fun getItemCount() = feedModelItems.size // Size

    inner class ViewHolder(itemView: View, val itemClick: (Daily) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bindFeedModel(feedModel: Daily) {

            with(feedModel) {


                itemView.date.text = feedModel.date


                //---------------------------------------
                //get weather picture
                val weather=feedModel.weather[0].icon

                //01d,clear sky
                //02d,few clouds
                //03d,scattered clouds
                //04d,overcast clouds ,broken clouds

                //10d,light rain
                //13d,light snow

                if(weather.equals("01d")) {
                    itemView.weather_image.setBackgroundResource(R.drawable.clear_sky)
                }
                else if(weather.equals("02d")) {
                    itemView.weather_image.setBackgroundResource(R.drawable.few_clouds)
                }
                else if(weather.equals("03d")) {
                    itemView.weather_image.setBackgroundResource(R.drawable.scattered_clouds)
                }
                else if(weather.equals("04d")) {
                    itemView.weather_image.setBackgroundResource(R.drawable.overcast_clouds)
                }
                else if(weather.equals("10d")) {
                    itemView.weather_image.setBackgroundResource(R.drawable.light_rain)
                }
                else if(weather.equals("13d")) {
                    itemView.weather_image.setBackgroundResource(R.drawable.light_snow)
                }

                //-------------------------------------
                //get max / min temperature


                val temp_max= feedModel.temp.max_string
                itemView.temp_max.text = temp_max

                val temp_min= feedModel.temp.min_string
                itemView.temp_min.text = temp_min

            }
        }
    }


}