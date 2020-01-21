package cqupt.sl.wanandroidmk.texthelper

object TextHelper {
    fun replaceStr(str:String):String{
        var temp = str.replace("&ldquo;","“")
        temp = temp.replace("&rdquo;","”")
        return temp
    }
}