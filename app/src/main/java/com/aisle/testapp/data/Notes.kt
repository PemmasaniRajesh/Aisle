package com.aisle.testapp.data

data class Profile(var first_name: String? = null, var avatar: String? = null)

data class Like(
    var can_see_profile: Boolean = false,
    var likes_received_count: Int = 0,
    var profiles: List<Profile>? = emptyList()
)

