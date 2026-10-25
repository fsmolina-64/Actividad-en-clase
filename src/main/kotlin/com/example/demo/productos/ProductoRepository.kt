package com.example.demo.productos

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDateTime

@Repository
interface ProductoRepository : JpaRepository<Producto, Long> {

    // ===== BÚSQUEDAS SIMPLES (derivadas del nombre) =====
    fun findByNombre(nombre: String): List<Producto>
    fun findByNombreContainingIgnoreCase(nombre: String): List<Producto>
    fun findByNombreStartingWith(nombre: String): List<Producto>
    fun findByNombreEndingWith(nombre: String): List<Producto>

    // ===== POR CATEGORÍA =====
    fun findByCategoria(categoria: String): List<Producto>
    fun findByCategoriaContainingIgnoreCase(categoria: String): List<Producto>
    fun findByCategoriaIsNull(): List<Producto>
    fun findByCategoriaIsNotNull(): List<Producto>

    // ===== POR ESTADO ACTIVO =====
    fun findByActivoTrue(): List<Producto>
    fun findByActivoFalse(): List<Producto>
    fun findByActivo(activo: Boolean): List<Producto>

    // ===== POR SKU (único) =====
    fun findBySku(sku: String): Producto?
    fun existsBySku(sku: String): Boolean

    // ===== POR PRECIO =====
    fun findByPrecio(precio: BigDecimal): List<Producto>
    fun findByPrecioLessThan(precio: BigDecimal): List<Producto>
    fun findByPrecioLessThanEqual(precio: BigDecimal): List<Producto>
    fun findByPrecioGreaterThan(precio: BigDecimal): List<Producto>
    fun findByPrecioGreaterThanEqual(precio: BigDecimal): List<Producto>
    fun findByPrecioBetween(min: BigDecimal, max: BigDecimal): List<Producto>

    // ===== POR STOCK =====
    fun findByStock(stock: Int): List<Producto>
    fun findByStockLessThan(stock: Int): List<Producto>
    fun findByStockLessThanEqual(stock: Int): List<Producto>
    fun findByStockGreaterThan(stock: Int): List<Producto>
    fun findByStockGreaterThanEqual(stock: Int): List<Producto>
    fun findByStockBetween(min: Int, max: Int): List<Producto>
    fun findByStockIsNull(): List<Producto>

    // ===== COMBINADOS (AND / OR) =====
    fun findByCategoriaAndActivoTrue(categoria: String): List<Producto>
    fun findByCategoriaAndActivoFalse(categoria: String): List<Producto>
    fun findByNombreContainingIgnoreCaseAndActivoTrue(nombre: String): List<Producto>
    fun findByPrecioBetweenAndActivoTrue(min: BigDecimal, max: BigDecimal): List<Producto>

    // ===== CON ORDENAMIENTO =====
    fun findByActivoTrueOrderByNombreAsc(): List<Producto>
    fun findByActivoTrueOrderByPrecioDesc(): List<Producto>
    fun findByCategoriaOrderByNombreAsc(categoria: String): List<Producto>
    fun findByStockLessThanOrderByStockAsc(stock: Int): List<Producto>

    // ===== CON PAGINACIÓN (Pageable) =====
    fun findByNombreContainingIgnoreCase(nombre: String, pageable: Pageable): Page<Producto>
    fun findByCategoria(categoria: String, pageable: Pageable): Page<Producto>
    fun findByActivoTrue(pageable: Pageable): Page<Producto>
    fun findByPrecioBetween(min: BigDecimal, max: BigDecimal, pageable: Pageable): Page<Producto>
    fun findByStockLessThan(stock: Int, pageable: Pageable): Page<Producto>

    // ===== CONTEO Y EXISTENCIA =====
    fun countByActivoTrue(): Long
    fun countByCategoria(categoria: String): Long
    fun countByPrecioGreaterThan(precio: BigDecimal): Long
    fun existsByNombre(nombre: String): Boolean
    fun existsByCategoriaAndNombre(categoria: String, nombre: String): Boolean

    // ===== POR FECHAS =====
    fun findByFechaCreacionAfter(fecha: LocalDateTime): List<Producto>
    fun findByFechaCreacionBefore(fecha: LocalDateTime): List<Producto>
    fun findByFechaCreacionBetween(inicio: LocalDateTime, fin: LocalDateTime): List<Producto>
    fun findByFechaActualizacionAfter(fecha: LocalDateTime): List<Producto>

    // ===== TOP / LIMIT =====
    fun findTop5ByActivoTrueOrderByPrecioDesc(): List<Producto>
    fun findTop10ByOrderByFechaCreacionDesc(): List<Producto>
    fun findFirstByOrderByPrecioDesc(): Producto?

    // ===== QUERIES PERSONALIZADAS (@Query) =====
    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :term, '%')) OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :term, '%'))")
    fun buscarPorTermino(@Param("term") term: String, pageable: Pageable): Page<Producto>

    @Query("SELECT p FROM Producto p WHERE p.stock < :stockMin")
    fun findByStockBajo(@Param("stockMin") stockMin: Int): List<Producto>

    // ===== SQL NATIVO =====
    @Query(value = "SELECT * FROM productos WHERE precio > :precio ORDER BY precio DESC", nativeQuery = true)
    fun findByPrecioMayorQueNativo(@Param("precio") precio: BigDecimal): List<Producto>

    // ===== UPDATES/DELETES MASIVOS (@Modifying) =====
    @Modifying
    @Query("UPDATE Producto p SET p.activo = :activo WHERE p.categoria = :categoria")
    fun actualizarActivoPorCategoria(@Param("categoria") categoria: String, @Param("activo") activo: Boolean): Int

    @Modifying
    @Query("UPDATE Producto p SET p.stock = p.stock - :cantidad WHERE p.id = :id AND p.stock >= :cantidad")
    fun reducirStock(@Param("id") id: Long, @Param("cantidad") cantidad: Int): Int

    @Modifying
    @Query("DELETE FROM Producto p WHERE p.activo = false AND p.fechaCreacion < :fecha")
    fun eliminarInactivosAntiguos(@Param("fecha") fecha: LocalDateTime): Int
}