package com.opycorp.backend.controller;

import com.opycorp.backend.model.EventoExterno;
import com.opycorp.backend.repository.EventoExternoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos-externos")
@CrossOrigin(origins = "*")
public class EventoExternoController {

    @Autowired
    private EventoExternoRepository repository;

    @PostConstruct
    public void initDemoData() {
        if (repository.count() == 0) {
            repository.save(new EventoExterno(
                    "Congreso de Bienestar Laboral 2026",
                    "2026-04-12",
                    "Santiago Centro",
                    "Asociación Chilena de Seguridad (ACHS)",
                    "Encuentro masivo de prevencionistas y recursos humanos para convenios institucionales.",
                    "Congreso corporativo",
                    "1500 profesionales"
            ));
            repository.save(new EventoExterno(
                    "Expo Salud & Deporte Providencia",
                    "2026-04-18",
                    "Providencia",
                    "Corporación de Desarrollo Social Providencia",
                    "Jornada al aire libre enfocada en operativos de salud preventiva, dental y kinesiológica.",
                    "Actividad municipal",
                    "3000 personas"
            ));
            repository.save(new EventoExterno(
                    "Feria de Beneficios Colaboradores Enel",
                    "2026-04-25",
                    "Las Condes",
                    "Enel Distribución Chile",
                    "Feria interna de prestadores de servicios de salud, automotriz y bienestar para personal corporativo.",
                    "Jornada de beneficios",
                    "1000 colaboradores"
            ));
        }
    }

    @GetMapping
    public List<EventoExterno> getAllEventosExternos() {
        return repository.findAll();
    }
}
