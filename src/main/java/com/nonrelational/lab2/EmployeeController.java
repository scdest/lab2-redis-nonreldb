package com.nonrelational.lab2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @PostMapping
    public void addEmployee(@RequestBody Employee employee) throws JsonProcessingException {
        redisTemplate.opsForValue().set(employee.getId(), objectMapper.writeValueAsString(employee));
    }

    @PutMapping("/{id}")
    public void updateEmployee(@PathVariable("id") String id, @RequestBody Employee employee) throws JsonProcessingException {
        redisTemplate.opsForValue().set(id, objectMapper.writeValueAsString(employee));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable("id") String id) throws JsonProcessingException {
        String res = redisTemplate.opsForValue().get(id);
        if (null == res) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(objectMapper.readValue(res, Employee.class));
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable("id") String id) {
        redisTemplate.delete(id);
    }
}
