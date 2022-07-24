package ltotj.minecraft.man10cooking.cooking.data.recipe.action

abstract class BaseAction {

    var actionType:ActionType
    val isFinished=false
    var rating=3

    constructor(actionData:Map<String,*>){
        actionType= ActionType.valueOf(actionData["action"] as String)
    }


}