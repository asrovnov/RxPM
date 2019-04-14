package me.dmdev.rxpm.delegate

import android.app.*
import android.os.*
import me.dmdev.rxpm.*
import me.dmdev.rxpm.base.*
import me.dmdev.rxpm.navigation.*

/**
 * Delegate for the [Activity] that helps with creation and binding of
 * a [presentation model][PresentationModel] and a [view][PmView].
 *
 * Use this class only if you can't subclass the [PmActivity].
 *
 * Users of this class must forward all the life cycle methods from the containing Activity
 * to the corresponding ones in this class.
 * todo doc
 */
class PmActivityDelegate<PM, A>(
    private val pmActivity: A,
    private val retainMode: RetainMode
)
        where PM : PresentationModel,
              A : Activity, A : PmView<PM> {

    // todo doc
    enum class RetainMode { FINISHING, CONFIGURATION_CHANGES }

    private val commonDelegate = CommonDelegate<PM, A>(pmActivity, ActivityNavigationMessageDispatcher(pmActivity))

    val presentationModel: PM get() = commonDelegate.presentationModel

    /**
     * You must call this method from the containing [Activity]'s corresponding method.
     */
    fun onCreate(savedInstanceState: Bundle?) {
        commonDelegate.onCreate(savedInstanceState)
    }

    /**
     * You must call this method from the containing [Activity]'s corresponding method.
     */
    fun onPostCreate() {
        commonDelegate.onBind()
    }

    /**
     * You must call this method from the containing [Activity]'s corresponding method.
     */
    fun onStart() {
        // For symmetry, may be used in the future
    }

    /**
     * You must call this method from the containing [Activity]'s corresponding method.
     */
    fun onResume() {
        commonDelegate.onResume()
    }

    /**
     * You must call this method from the containing [Activity]'s corresponding method.
     */
    fun onSaveInstanceState(outState: Bundle) {
        commonDelegate.onSaveInstanceState(outState)
        commonDelegate.onPause()
    }

    /**
     * You must call this method from the containing [Activity]'s corresponding method.
     */
    fun onPause() {
        commonDelegate.onPause()
    }

    /**
     * You must call this method from the containing [Activity]'s corresponding method.
     */
    fun onStop() {
        // For symmetry, may be used in the future
    }

    /**
     * You must call this method from the containing [Activity]'s corresponding method.
     */
    fun onDestroy() {
        commonDelegate.onUnbind()

        when (retainMode) {
            RetainMode.FINISHING -> {
                if (pmActivity.isFinishing) {
                    commonDelegate.onDestroy()
                }
            }

            RetainMode.CONFIGURATION_CHANGES -> {
                if (!pmActivity.isChangingConfigurations) {
                    commonDelegate.onDestroy()
                }
            }
        }
    }
}