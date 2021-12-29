package br.com.acalv3.domain.service

import br.com.acalv3.domain.dto.FilterDTO
import br.com.acalv3.domain.exception.DuplicatedFieldException
import br.com.acalv3.domain.exception.RequiredFieldException
import br.com.acalv3.domain.model.AbstractModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

abstract class AppService<U: AbstractModel>(
    private val appRepository: JpaRepository<U, Long>
) {
    private var logger: Logger = LoggerFactory.getLogger(AppService::class.java)

    fun delete(id: Long) {
        logger.info("delete by id $id")

        if(!appRepository.existsById(id)){
            throw NoSuchElementException("Entity not found")
        }
        appRepository.deleteById(id)
    }

    fun update(u: U) : U =
        save(u)

    fun save(u: U) : U {

        valid(u)
        prepareForSave(u)

        try{

            return appRepository.save(u)
        } catch (ex: DataIntegrityViolationException){
            logger.info("Campo nulo", ex)
            throw RequiredFieldException("Campo nulo")
        } catch (ex: Exception){

            logger.info("Duplicated", ex)
            throw DuplicatedFieldException("Duplicated")
        }
    }

    fun get(id: Long): U =
        appRepository.findById(id).orElseThrow {
            NoSuchElementException("Entity not found")
        }

    fun getAll(): List<U> =
        appRepository.findAll()

    open fun pageable(filter: FilterDTO<U>): Page<U> {
        throw RuntimeException("")
    }

    fun count(): Long =
        appRepository.count()

    abstract fun findByName(name: String): U

    open fun valid(u: U) = Unit

    open fun prepareForSave(u: U) {

        if (u.createdAt == null || u.id == null) {
            u.createdAt = LocalDateTime.now()
        }

        u.lastModifiedAt = LocalDateTime.now()

    }
}

