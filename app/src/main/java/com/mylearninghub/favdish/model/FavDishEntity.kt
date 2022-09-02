package com.mylearninghub.favdish.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tbl_fav_dish")
data class FavDishEntity(
    @ColumnInfo(name = "Image_Url") val image:String,
    @ColumnInfo(name = "Image_Source") val imageSource:String,
    @ColumnInfo(name="Title") val title:String,
    @ColumnInfo(name="Type") val type:String,
    @ColumnInfo(name="Category") val category:String,
    @ColumnInfo(name="Ingredients") val ingredients:String,
    @ColumnInfo(name="Cooking_Time") val cookingTime:String,
    @ColumnInfo(name="Instructions") val instructions:String,
    @ColumnInfo(name="Favourite_Dish") val favouriteDish:Boolean,
    @ColumnInfo(name = "Id")@PrimaryKey(autoGenerate = true) val id:Int=0,

    )
