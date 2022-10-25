package com.mycomp.firebasemvvm.domain.model

import android.net.Uri
import java.util.UUID

data class Media(
     val id:UUID,
     val uri:Uri,
     val size:String,
     //val url:String,
     val name:String
)
