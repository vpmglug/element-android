/*
 * Copyright 2019 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package im.vector.riotx.features.home.room.detail.timeline.action

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import im.vector.riotx.R
import im.vector.riotx.core.epoxy.VectorEpoxyHolder
import im.vector.riotx.core.epoxy.VectorEpoxyModel

/**
 * A action for bottom sheet.
 */
@EpoxyModelClass(layout = R.layout.item_bottom_sheet_action)
abstract class BottomSheetItemAction : VectorEpoxyModel<BottomSheetItemAction.Holder>() {

    @EpoxyAttribute
    @DrawableRes
    var iconRes: Int = 0
    @EpoxyAttribute
    var textRes: Int = 0
    @EpoxyAttribute
    lateinit var listener: View.OnClickListener

    override fun bind(holder: Holder) {
        holder.view.setOnClickListener {
            listener.onClick(it)
        }

        holder.icon.setImageResource(iconRes)
        holder.text.setText(textRes)
    }

    class Holder : VectorEpoxyHolder() {
        val icon by bind<ImageView>(R.id.action_icon)
        val text by bind<TextView>(R.id.action_title)
    }
}
