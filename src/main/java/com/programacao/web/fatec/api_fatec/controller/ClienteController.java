package com.programacao.web.fatec.api_fatec.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.programacao.web.fatec.api_fatec.domain.cliente.ClienteRepository;
import com.programacao.web.fatec.api_fatec.entities.Cliente;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    // Criar Cliente (POST)
    @PostMapping
    public Cliente createCliente(@RequestBody Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // Listar todos (GET)
    @GetMapping
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    // Buscar por ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .map(cliente -> ResponseEntity.ok(cliente))
                .orElse(ResponseEntity.notFound().build());
    }

    // Atualizar (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        return clienteRepository.findById(id)
                .map(c -> {
                    c.setNome(cliente.getNome());
                    c.setEndereco(cliente.getEndereco());
                    Cliente atualizado = clienteRepository.save(c);
                    return ResponseEntity.ok(atualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Deletar (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarCliente(@PathVariable Long id) {
        try {
            if (!clienteRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            clienteRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // 204
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Caso o cliente esteja referenciado por outra tabela
            return ResponseEntity.status(409).body("Não foi possível excluir: registro referenciado.");
        }
    }
}
