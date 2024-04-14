package utils

import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

fun <T> RequestBuilder<T>.cornerRadius(value: Int) =
    apply(RequestOptions().transform(RoundedCorners(value)))
