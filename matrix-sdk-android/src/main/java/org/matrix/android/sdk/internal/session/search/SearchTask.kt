/*
 * Copyright 2020 New Vector Ltd
 * Copyright 2020 The Matrix.org Foundation C.I.C.
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

package org.matrix.android.sdk.internal.session.search

import org.greenrobot.eventbus.EventBus
import org.matrix.android.sdk.internal.network.executeRequest
import org.matrix.android.sdk.internal.session.room.RoomAPI
import org.matrix.android.sdk.internal.session.search.request.SearchRequestBody
import org.matrix.android.sdk.internal.session.search.request.SearchRequestCategories
import org.matrix.android.sdk.internal.session.search.request.SearchRequestEventContext
import org.matrix.android.sdk.internal.session.search.request.SearchRequestFilter
import org.matrix.android.sdk.internal.session.search.request.SearchRequestOrder
import org.matrix.android.sdk.internal.session.search.request.SearchRequestRoomEvents
import org.matrix.android.sdk.internal.session.search.response.SearchResponse
import org.matrix.android.sdk.internal.task.Task
import javax.inject.Inject

internal interface SearchTask : Task<SearchTask.Params, SearchResponse> {

    data class Params(
            val searchTerm: String,
            val roomId: String,
            val nextBatch: String? = null,
            val orderByRecent: Boolean,
            val limit: Int,
            val beforeLimit: Int,
            val afterLimit: Int,
            val includeProfile: Boolean
    )
}

internal class DefaultSearchTask @Inject constructor(
        private val roomAPI: RoomAPI,
        private val eventBus: EventBus
) : SearchTask {

    override suspend fun execute(params: SearchTask.Params): SearchResponse {
        return executeRequest(eventBus) {
            val searchRequestBody = SearchRequestBody(
                    searchCategories = SearchRequestCategories(
                            roomEvents = SearchRequestRoomEvents(
                                    searchTerm = params.searchTerm,
                                    orderBy = if (params.orderByRecent) SearchRequestOrder.RECENT.value else SearchRequestOrder.RANK.value,
                                    filter = SearchRequestFilter(
                                            limit = params.limit,
                                            rooms = listOf(params.roomId)
                                    ),
                                    eventContext = SearchRequestEventContext(
                                            beforeLimit = params.beforeLimit,
                                            afterLimit = params.afterLimit,
                                            includeProfile = params.includeProfile
                                    )
                            )
                    )
            )
            apiCall = roomAPI.search(params.nextBatch, searchRequestBody)
        }
    }
}