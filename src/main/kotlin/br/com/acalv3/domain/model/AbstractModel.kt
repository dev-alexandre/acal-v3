package br.com.acalv3.domain.model

import java.time.LocalDateTime

interface AbstractModel {

    var id: Long?
    val name: String?
    var createdAt: LocalDateTime?
    var lastModifiedAt: LocalDateTime?

}

