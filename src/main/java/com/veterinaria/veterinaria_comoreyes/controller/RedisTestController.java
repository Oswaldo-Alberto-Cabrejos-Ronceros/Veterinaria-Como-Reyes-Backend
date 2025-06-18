package com.veterinaria.veterinaria_comoreyes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis")
public class RedisTestController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/guardar/{clave}/{valor}")
    public String guardar(@PathVariable String clave, @PathVariable String valor) {
        redisTemplate.opsForValue().set(clave, valor);
        return "Guardado en Redis";
    }

    @GetMapping("/obtener/{clave}")
    public String obtener(@PathVariable String clave) {
        return redisTemplate.opsForValue().get(clave);
    }
}