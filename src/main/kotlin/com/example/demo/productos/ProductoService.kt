package com.example.demo.productos

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
@Transactional
class ProductoService(private val repository: ProductoRepository) {

    fun guardar(producto: Producto): Producto = repository.save(producto)

    fun listar(): List<Producto> = repository.findAll()

    fun obtenerPorId(id: Long): Producto = repository.findById(id)
        .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado: $id") }

    fun buscarPorNombre(nombre: String): List<Producto> =
        repository.findByNombreContainingIgnoreCase(nombre)

    fun actualizar(id: Long, datos: Producto): Producto {
        val existente = obtenerPorId(id)
        val actualizado = existente.copy(
            nombre = datos.nombre,
            descripcion = datos.descripcion,
            precio = datos.precio,
            stock = datos.stock,
            categoria = datos.categoria,
            sku = datos.sku,
            activo = datos.activo
        )
        return repository.save(actualizado)
    }

    fun eliminar(id: Long) {
        repository.deleteById(id)
    }
}