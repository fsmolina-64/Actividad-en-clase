package com.example.demo.productos

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@Controller
class ProductoController {
    @Autowired
    lateinit var productoService: ProductoService

    @GetMapping ("/productos")
    fun obtenerProductos(@PathVariable id: Long): Product{
        return productoService
    }
}

