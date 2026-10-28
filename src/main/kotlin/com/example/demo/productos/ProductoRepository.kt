package com.example.demo.productos

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductoRepository : JpaRepository<Producto, Long> {

    fun findByNombreContainingIgnoreCase(nombre: String): List<Producto>

    fun existsBySku(sku: String): Boolean
}