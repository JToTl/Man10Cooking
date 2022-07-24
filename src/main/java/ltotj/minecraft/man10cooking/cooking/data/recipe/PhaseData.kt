package ltotj.minecraft.man10cooking.cooking.data.recipe

import ltotj.minecraft.man10cooking.cooking.data.recipe.action.BaseAction
import java.util.*
import kotlin.collections.ArrayList

class PhaseData {

    val startTime= Date().time/1000L
    val actions= ArrayList<BaseAction>()

}