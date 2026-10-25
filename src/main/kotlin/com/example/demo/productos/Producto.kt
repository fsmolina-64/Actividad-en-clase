package com.example.demo.productos

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "productos")
data class Producto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 200)
    val nombre: String,

    @Column(columnDefinition = "TEXT")
    val descripcion: String? = null,

    @Column(nullable = false, precision = 12, scale = 2)
    val precio: BigDecimal,

    @Column(nullable = false)
    val stock: Int = 0,

    @Column(length = 100)
    val categoria: String? = null,

    @Column(length = 50, unique = true)
    val sku: String? = null,

    @Column(nullable = false)
    val activo: Boolean = true,

    @Column(name = "fecha_creacion", updatable = false)
    val fechaCreacion: LocalDateTime? = null,

    @Column(name = "fecha_actualizacion")
    val fechaActualizacion: LocalDateTime? = null
)