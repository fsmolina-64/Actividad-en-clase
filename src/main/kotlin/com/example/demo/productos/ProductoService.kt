package com.example.demo.productos

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal

@Service
@Transactional
class ProductoService(private val productoRepository: ProductoRepository) {

    fun obtenerTodos(): List<Producto> = productoRepository.findAll()

    fun obtenerPaginados(pageable: Pageable): Page<Producto> = productoRepository.findAll(pageable)

    fun obtenerPorId(id: Long): Producto = productoRepository.findById(id)
        .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el producto con id $id") }

    fun obtenerPorSku(sku: String): Producto? = productoRepository.findBySku(sku)

    fun buscarPorNombre(nombre: String): List<Producto> =
        productoRepository.findByNombreContainingIgnoreCase(nombre)

    fun guardar(producto: Producto): Producto = productoRepository.save(producto)

    fun actualizar(id: Long, datos: Producto): Producto {
        val existente = obtenerPorId(id)
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
        return productoRepository.save(actualizado)
    }

    fun eliminar(id: Long) {
        productoRepository.deleteById(id)
    }

    fun obtenerActivos(): List<Producto> = productoRepository.findByActivoTrue()

    fun obtenerPorCategoria(categoria: String): List<Producto> =
        productoRepository.findByCategoria(categoria)

    fun obtenerPorRangoPrecio(min: BigDecimal, max: BigDecimal): List<Producto> =
        productoRepository.findByPrecioBetween(min, max)

    fun existeSku(sku: String): Boolean = productoRepository.existsBySku(sku)
}