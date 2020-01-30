package me.dmdev.rxpm.validation

import me.dmdev.rxpm.PresentationModel
import me.dmdev.rxpm.widget.InputControl

class FormValidator internal constructor() {

    private val itemValidators = mutableListOf<Validator>()

    fun addItemValidator(itemValidator: Validator) {
        itemValidators.add(itemValidator)
    }

    fun validate(): Boolean {
        var isFormValid = true
        itemValidators.forEach { itemValidator ->
            val isItemValid = itemValidator.validate()

            if (!isItemValid) {
                isFormValid = false
            }
        }
        return isFormValid
    }
}

@Suppress("unused")
fun PresentationModel.formValidator(init: FormValidator.() -> Unit): FormValidator {
    val formValidator = FormValidator()
    formValidator.init()
    return formValidator
}

fun FormValidator.input(inputControl: InputControl, init: InputValidator.() -> Unit) {
    val inputValidator = InputValidator(inputControl)
    inputValidator.init()
    addItemValidator(inputValidator)
}