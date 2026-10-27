package com.example.demo.productos

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/api/productos")
class ProductoController(private val service: ProductoService) {

    @PostMapping
    fun crear(@RequestBody producto: Producto): ResponseEntity<Producto> {
        if (producto.sku?.isNotBlank() == true && service.existeSku(producto.sku!!)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
        val guardado = service.guardar(producto)
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado)
    }

    @GetMapping
    fun listar(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "id") sortBy: String,
        @RequestParam(defaultValue = "asc") direction: String
    ): ResponseEntity<Page<Producto>> {
        val sort = if (direction.equals("desc", ignoreCase = true))
            Sort.by(sortBy).descending() else Sort.by(sortBy).ascending()
        val pageable = PageRequest.of(page, size, sort)
        return ResponseEntity.ok(service.obtenerPaginados(pageable))
    }

    @GetMapping("/activos")
    fun listarActivos(): List<Producto> = service.obtenerActivos()

    @GetMapping("/buscar")
    fun buscarPorNombre(@RequestParam nombre: String): List<Producto> =
        service.buscarPorNombre(nombre)

    @GetMapping("/categoria/{categoria}")
    fun listarPorCategoria(@PathVariable categoria: String): List<Producto> =
        service.obtenerPorCategoria(categoria)

    @GetMapping("/precio")
    fun listarPorRangoPrecio(
        @RequestParam min: BigDecimal,
        @RequestParam max: BigDecimal
    ): List<Producto> = service.obtenerPorRangoPrecio(min, max)

    @GetMapping("/{id}")
    fun obtener(@PathVariable id: Long): ResponseEntity<Producto> =
        ResponseEntity.ok(service.obtenerPorId(id))

    @GetMapping("/sku/{sku}")
    fun obtenerPorSku(@PathVariable sku: String): ResponseEntity<Producto> =
        service.obtenerPorSku(sku)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @PutMapping("/{id}")
    fun actualizar(@PathVariable id: Long, @RequestBody datos: Producto): ResponseEntity<Producto> =
        ResponseEntity.ok(service.actualizar(id, datos))

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Long): ResponseEntity<Void> {
        service.eliminar(id)
        return ResponseEntity.noContent().build()
    }
}