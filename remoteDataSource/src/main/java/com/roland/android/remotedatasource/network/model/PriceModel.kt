package com.roland.android.remotedatasource.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// Timbu API contains a bug in the `current_price` field. The field holds an array of objects
// which is `PriceModel` is this case. In it is `values` which is a list of two doubles and another array.
// There is where the error lies. Since an array cannot hold two different types of values, it returns an error each time.
// But the `@Transient` annotation is used to avoid the error as it tells `Moshi` to ignore unrecognized fields.
// In doing so, `values` returns null.
@JsonClass(generateAdapter = true)
internal data class PriceModel(
	@Transient
	@Json(name = "EUR")
	val values: List<Double> = emptyList()
)

// Below is the response from `current_price`. If only the the last field in the array
// which is also an array can be removed, life will be smoother for us all.
//
//"current_price": [
//  {
//  	"EUR": [
//  	    124.48,
//  	    null,
//  	    []
//  	]
//  }
//]
