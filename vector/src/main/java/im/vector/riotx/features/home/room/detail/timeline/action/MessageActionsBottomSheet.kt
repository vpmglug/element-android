/*
 * Copyright 2019 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package im.vector.riotx.features.home.room.detail.timeline.action

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProviders
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import im.vector.riotx.EmojiCompatFontProvider
import im.vector.riotx.R
import im.vector.riotx.core.di.ScreenComponent
import im.vector.riotx.core.platform.VectorBaseBottomSheetDialogFragment
import im.vector.riotx.features.home.AvatarRenderer
import im.vector.riotx.features.home.room.detail.timeline.item.MessageInformationData
import kotlinx.android.synthetic.main.bottom_sheet_generic_recycler_epoxy.*
import javax.inject.Inject

/**
 * Bottom sheet fragment that shows a message preview with list of contextual actions
 */
class MessageActionsBottomSheet : VectorBaseBottomSheetDialogFragment(), MessageActionsEpoxyController.MessageActionsEpoxyControllerListener {
    @Inject lateinit var messageActionViewModelFactory: MessageActionsViewModel.Factory
    @Inject lateinit var avatarRenderer: AvatarRenderer
    @Inject lateinit var fontProvider: EmojiCompatFontProvider

    private val viewModel: MessageActionsViewModel by fragmentViewModel(MessageActionsViewModel::class)

    private lateinit var messageActionsEpoxyController: MessageActionsEpoxyController
    private lateinit var actionHandlerModel: ActionsHandler

    override fun injectWith(screenComponent: ScreenComponent) {
        screenComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_generic_recycler_epoxy, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        actionHandlerModel = ViewModelProviders.of(requireActivity()).get(ActionsHandler::class.java)

        messageActionsEpoxyController = MessageActionsEpoxyController(requireContext(), avatarRenderer, fontProvider)
        bottomSheetEpoxyRecyclerView.setController(messageActionsEpoxyController)
        messageActionsEpoxyController.listener = this
    }

    override fun didSelectMenuAction(simpleAction: SimpleAction) {
        actionHandlerModel.fireAction(simpleAction)
        dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        // We want to force the bottom sheet initial state to expanded
        (dialog as? BottomSheetDialog)?.let { bottomSheetDialog ->
            bottomSheetDialog.setOnShowListener { dialog ->
                val d = dialog as BottomSheetDialog
                (d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as? FrameLayout)?.let {
                    BottomSheetBehavior.from(it).state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        }
        return dialog
    }

    override fun invalidate() = withState(viewModel) {
        messageActionsEpoxyController.setData(it)
    }

    companion object {
        fun newInstance(roomId: String, informationData: MessageInformationData): MessageActionsBottomSheet {
            return MessageActionsBottomSheet().apply {
                setArguments(
                        TimelineEventFragmentArgs(
                                informationData.eventId,
                                roomId,
                                informationData
                        )
                )
            }
        }
    }
}
