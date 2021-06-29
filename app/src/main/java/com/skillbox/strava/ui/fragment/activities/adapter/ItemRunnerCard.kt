package com.skillbox.strava.ui.fragment.activities.adapter

import android.os.Build
import com.skillbox.shared_model.СreateActivity
import com.skillbox.strava.R
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.skillbox.core_db.pref.Pref
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.item_runner.view.*

fun itemRunnerCard(share:() -> Unit) =
        adapterDelegateLayoutContainer<СreateActivity, Any>(R.layout.item_runner) {

            bind {
                val profileImageUrl = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    Pref(itemView.context).photoprofile
                else
                    ""
                val nameProfile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    Pref(itemView.context).nameProfile
                else
                    ""
                if(profileImageUrl.isNotEmpty()) {
                    val transformation = RoundedCornersTransformation(
                            200,
                            0,
                            RoundedCornersTransformation.CornerType.ALL
                    )
                    Picasso.get().load(profileImageUrl)
                            .resize(200, 200)
                            .transform(transformation)
                            .into(containerView.runner_ivPhoto)
                }
                if(nameProfile.isNotEmpty())
                    containerView.runner_tvName.text = nameProfile
                containerView.runner_tvTitle.text = item.name
                containerView.runner_tvDistanceValue.text = "${item.distance} m"
                containerView.runner_tvTimeValue.text = "${item.elapsed_time} h"
                containerView.runner_tvElevationValue.text = "${item.total_elevation_gain} m"
                containerView.runner_share.setOnClickListener {
                    share.invoke()
                }
            }

        }