package com.example.demo.productos

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
@Transactional
class ProductoService(private val repository: ProductoRepository) {

    fun guardar(producto: Producto): Producto = repository.save(producto)

    fun obtenerTodos(): List<Producto> = repository.findAll()

    fun obtenerPorId(id: Long): Producto? = repository.findById(id).orElse(null)

    fun obtenerPorNombre(nombre: String): List<Producto> = 
        repository.findByNombreContainingIgnoreCase(nombre)

    fun obtenerPorCategoria(categoria: String): List<Producto> = 
        repository.findByCategoria(categoria)

    fun obtenerActivos(): List<Producto> = repository.findByActivoTrue()

    fun obtenerPorSku(sku: String): Producto? = repository.findBySku(sku)

    fun obtenerPorRangoPrecio(min: BigDecimal, max: BigDecimal): List<Producto> = 
        repository.findByPrecioBetween(min, max)

    fun obtenerPaginados(pageable: Pageable): Page<Producto> = repository.findAll(pageable)

    fun actualizar(id: Long, datos: Producto): Producto? {
        return repository.findById(id).map { existente ->
            val actualizado = existente.copy(
                nombre = datos.nombre,
                descripcion = datos.descripcion,
                precio = datos.precio,
                stock = datos.stock,
                categoria = datos.categoria,
                sku = datos.sku,
                activo = datos.activo,
                fechaActualizacion = java.time.LocalDateTime.now()
            )
            repository.save(actualizado)
        }.orElse(null)
    }

    fun eliminar(id: Long): Boolean {
        return repository.findById(id).map {
            repository.delete(it)
            true
        }.orElse(false)
    }

    fun existeSku(sku: String): Boolean = repository.existsBySku(sku)
}