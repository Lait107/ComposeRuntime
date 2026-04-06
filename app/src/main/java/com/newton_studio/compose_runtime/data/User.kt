package com.newton_studio.compose_runtime.data

import com.newton_studio.compose_runtime.domain.model.User as DomainUSer

data class User(
    val login: String,
    val password: String,
)

fun User.toDomainUser() = DomainUSer(
    login = login,
    password = password,
)
