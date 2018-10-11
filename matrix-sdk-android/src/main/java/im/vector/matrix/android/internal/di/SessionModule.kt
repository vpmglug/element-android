package im.vector.matrix.android.internal.di

import im.vector.matrix.android.internal.auth.data.SessionParams
import im.vector.matrix.android.internal.legacy.MXDataHandler
import im.vector.matrix.android.internal.legacy.MXSession
import im.vector.matrix.android.internal.legacy.data.store.MXFileStore
import im.vector.matrix.android.internal.session.DefaultSession
import org.koin.dsl.context.ModuleDefinition
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import retrofit2.Retrofit

class SessionModule(private val sessionParams: SessionParams) : Module {

    override fun invoke(): ModuleDefinition = module(override = true) {

        scope(DefaultSession.SCOPE) {
            val retrofitBuilder = get() as Retrofit.Builder
            retrofitBuilder
                    .baseUrl(sessionParams.homeServerConnectionConfig.homeServerUri.toString())
                    .build()
        }

        scope(DefaultSession.SCOPE) {
            val store = MXFileStore(sessionParams.credentials, false, get())
            val dataHandler = MXDataHandler(store, sessionParams.credentials)
            MXSession.Builder(sessionParams, dataHandler, get()).build()
            store.setDataHandler(dataHandler)
            dataHandler
        }

    }.invoke()


}