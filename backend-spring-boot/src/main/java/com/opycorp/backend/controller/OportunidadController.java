package com.opycorp.backend.controller;

import com.opycorp.backend.model.Oportunidad;
import com.opycorp.backend.repository.OportunidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/oportunidades")
@CrossOrigin(origins = "*")
public class OportunidadController {

    @Autowired
    private OportunidadRepository repository;

    @GetMapping
    public List<Oportunidad> getAllOportunidades() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Oportunidad> getOportunidadById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Oportunidad createOportunidad(@RequestBody Oportunidad oportunidad) {
        return repository.save(oportunidad);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Oportunidad> updateOportunidad(@PathVariable Long id, @RequestBody Oportunidad oportunidadDetails) {
        return repository.findById(id)
                .map(oportunidad -> {
                    oportunidad.setNombre(oportunidadDetails.getNombre());
                    oportunidad.setTipo(oportunidadDetails.getTipo());
                    oportunidad.setDescripcion(oportunidadDetails.getDescripcion());
                    oportunidad.setFuente(oportunidadDetails.getFuente());
                    oportunidad.setPrioridad(oportunidadDetails.getPrioridad());
                    oportunidad.setPotencial(oportunidadDetails.getPotencial());
                    oportunidad.setEstado(oportunidadDetails.getEstado());
                    oportunidad.setObservaciones(oportunidadDetails.getObservaciones());
                    oportunidad.setUbicacion(oportunidadDetails.getUbicacion());
                    oportunidad.setFotoPath(oportunidadDetails.getFotoPath());
                    oportunidad.setOrganizacion(oportunidadDetails.getOrganizacion());
                    oportunidad.setContactoNombre(oportunidadDetails.getContactoNombre());
                    oportunidad.setContactoTelefono(oportunidadDetails.getContactoTelefono());
                    oportunidad.setPublicoEsperado(oportunidadDetails.getPublicoEsperado());
                    oportunidad.setResponsable(oportunidadDetails.getResponsable());
                    oportunidad.setCondicionesEspacio(oportunidadDetails.getCondicionesEspacio());
                    oportunidad.setCondicionesCosto(oportunidadDetails.getCondicionesCosto());
                    oportunidad.setCondicionesRequisitos(oportunidadDetails.getCondicionesRequisitos());
                    oportunidad.setResultadoActivacion(oportunidadDetails.getResultadoActivacion());
                    oportunidad.setImpactoEvaluacion(oportunidadDetails.getImpactoEvaluacion());
                    oportunidad.setProximaAccion(oportunidadDetails.getProximaAccion());
                    return ResponseEntity.ok(repository.save(oportunidad));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOportunidad(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
