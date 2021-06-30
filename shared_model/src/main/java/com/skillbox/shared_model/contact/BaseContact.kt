package com.skillbox.shared_model.contact

abstract class BaseContact(open val id: Long, open val name: String, open val numbers: List<String>, open val avatar: String)