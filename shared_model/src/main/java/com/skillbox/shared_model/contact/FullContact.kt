package com.skillbox.shared_model.contact


class FullContact(
    id: Long,
    name: String,
    numbers: List<String>,
    val emails: List<String>,
    avatar: String
) : BaseContact(
    id = id,
    name = name,
    numbers = numbers,
    avatar = avatar
)