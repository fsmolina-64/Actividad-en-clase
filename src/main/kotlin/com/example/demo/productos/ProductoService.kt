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

    // Obtener todos los productos
    fun obtenerTodos(): List<Producto> = productoRepository.findAll()

    // Obtener producto paginado
    fun obtenerPaginados(pageable: Pageable): Page<Producto> = productoRepository.findAll(pageable)

    // Buscar producto por ID (lanza 404 si no existe)
    fun obtenerPorId(id: Long): Producto = productoRepository.findById(id)
        .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el producto con id $id") }

    // Buscar producto por SKU
    fun obtenerPorSku(sku: String): Producto? = productoRepository.findBySku(sku)

    // Buscar productos por nombre (contiene, ignores case)
    fun buscarPorNombre(nombre: String): List<Producto> =
        productoRepository.findByNombreContainingIgnoreCase(nombre)

    // Crear / guardar producto
    fun guardar(producto: Producto): Producto = productoRepository.save(producto)

    // Actualizar producto existente
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

    // Eliminar producto por ID
    fun eliminar(id: Long) {
        productoRepository.deleteById(id)
    }

    // Buscar productos activos
    fun obtenerActivos(): List<Producto> = productoRepository.findByActivoTrue()

    // Buscar por categoría
    fun obtenerPorCategoria(categoria: String): List<Producto> =
        productoRepository.findByCategoria(categoria)

    // Buscar por rango de precio
    fun obtenerPorRangoPrecio(min: BigDecimal, max: BigDecimal): List<Producto> =
        productoRepository.findByPrecioBetween(min, max)

    // Verificar si existe SKU
    fun existeSku(sku: String): Boolean = productoRepository.existsBySku(sku)
}