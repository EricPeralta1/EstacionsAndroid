package com.example.estacionsandroid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView

class AvatarAdapter (val activity: Context, val layout: Int, val data: List<Avatar>):
    ArrayAdapter<Avatar>(activity,layout,data)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView

        if (view == null){
            view = LayoutInflater.from(activity).inflate(layout, parent, false)
        }


        val avatar = view?.findViewById<ImageView>(R.id.ImgAvatar)
        val id = activity.resources.getIdentifier(data[position].name, "drawable", activity.packageName)

        avatar?.setImageResource(id)

        return view!!
    }
}