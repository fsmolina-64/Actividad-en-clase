package com.example.demo.productos

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/productos")
class ProductoController(private val service: ProductoService) {

    @PostMapping
    fun crear(@RequestBody producto: Producto): ResponseEntity<Producto> =
        ResponseEntity.ok(service.guardar(producto))

    @GetMapping
    fun listar(): List<Producto> = service.listar()

    @GetMapping("/{id}")
    fun obtener(@PathVariable id: Long): ResponseEntity<Producto> =
        ResponseEntity.ok(service.obtenerPorId(id))
}