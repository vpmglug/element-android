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

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import im.vector.riotx.R
import im.vector.riotx.features.home.room.detail.timeline.item.MessageInformationData

sealed class SimpleAction(@StringRes val titleRes: Int, @DrawableRes val iconResId: Int) {
    data class AddReaction(val eventId: String) : SimpleAction(R.string.message_add_reaction, R.drawable.ic_add_reaction)
    data class Copy(val content: String) : SimpleAction(R.string.copy, R.drawable.ic_copy)
    data class Edit(val eventId: String) : SimpleAction(R.string.edit, R.drawable.ic_edit)
    data class Quote(val eventId: String) : SimpleAction(R.string.quote, R.drawable.ic_quote)
    data class Reply(val eventId: String) : SimpleAction(R.string.reply, R.drawable.ic_reply)
    data class Share(val imageUrl: String) : SimpleAction(R.string.share, R.drawable.ic_share)
    data class Resend(val eventId: String) : SimpleAction(R.string.global_retry, R.drawable.ic_refresh_cw)
    data class Remove(val eventId: String) : SimpleAction(R.string.remove, R.drawable.ic_trash)
    data class Delete(val eventId: String) : SimpleAction(R.string.delete, R.drawable.ic_delete)
    data class Cancel(val eventId: String) : SimpleAction(R.string.cancel, R.drawable.ic_close_round)
    data class ViewSource(val content: String) : SimpleAction(R.string.view_source, R.drawable.ic_view_source)
    data class ViewDecryptedSource(val content: String) : SimpleAction(R.string.view_decrypted_source, R.drawable.ic_view_source)
    data class CopyPermalink(val eventId: String) : SimpleAction(R.string.permalink, R.drawable.ic_permalink)
    data class Flag(val eventId: String) : SimpleAction(R.string.report_content, R.drawable.ic_flag)
    data class QuickReact(val eventId: String, val clickedOn: String, val add: Boolean) : SimpleAction(0, 0)
    data class ViewReactions(val messageInformationData: MessageInformationData) : SimpleAction(R.string.message_view_reaction, R.drawable.ic_view_reactions)
    data class ViewEditHistory(val messageInformationData: MessageInformationData) :
            SimpleAction(R.string.message_view_edit_history, R.drawable.ic_view_edit_history)
}
