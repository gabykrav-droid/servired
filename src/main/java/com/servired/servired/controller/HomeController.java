package com.servired.servired.controller;

import com.servired.servired.model.Profesional;
import com.servired.servired.repository.ProfesionalRepository;
import org.springframework.web.bind.annotation.PathVariable;
import com.servired.servired.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.servired.servired.model.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;


@Controller

public class HomeController {

    @Autowired
    private ProfesionalRepository profesionalRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // métodos abajo...


    @GetMapping("/")
    public String index(
            @RequestParam(required = false) String buscar,
            Model model) {

        List<Profesional> profesionales;

        if (buscar != null && !buscar.isEmpty()) {

            List<Profesional> porNombre =
                    profesionalRepository.findByEstadoAndNombreContainingIgnoreCase("APROBADO", buscar);

            List<Profesional> porProfesion =
                    profesionalRepository.findByEstadoAndProfesionContainingIgnoreCase("APROBADO", buscar);

            porNombre.addAll(porProfesion);

            profesionales = porNombre;

        } else {
            profesionales = profesionalRepository.findByEstado("APROBADO");
        }

        model.addAttribute("profesionales", profesionales);
        model.addAttribute("profesional", new Profesional());
        model.addAttribute("buscar", buscar);

        return "index";
    }
    @GetMapping("/redirigir")
    public String redirigirSegunRol(Authentication authentication) {

        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/pendientes";
        }

        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            return "redirect:/panel";
        }

        return "redirect:/";
    }
    @GetMapping("/panel")
    public String panel(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Usuario usuario = usuarioRepository.findByEmail(email);

        Profesional profesional = profesionalRepository.findByUsuario(usuario);

        model.addAttribute("profesional", profesional);

        return "panel";
    }
    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        Profesional profesional = profesionalRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido"));

        model.addAttribute("profesional", profesional);

        return "editar";
    }


    @PostMapping("/guardar")
    public String guardar(Profesional profesional) {

        profesional.setEstado("PENDIENTE");

        profesionalRepository.save(profesional);
        return "redirect:/";
    }
    @PostMapping("/panel/editar-perfil")
    public String editarPerfil(
            @RequestParam Long id,
            @RequestParam String nombre,
            @RequestParam String profesion,
            @RequestParam String descripcion) {

        Profesional profesional = profesionalRepository.findById(id).orElse(null);

        if (profesional != null) {
            profesional.setNombre(nombre);
            profesional.setProfesion(profesion);
            profesional.setDescripcion(descripcion);
            profesional.setEstado("PENDIENTE");

            profesionalRepository.save(profesional);
        }

        return "redirect:/panel";
    }
    @PostMapping("/panel/crear-perfil")
    public String crearPerfil(
            @RequestParam String nombre,
            @RequestParam String profesion,
            @RequestParam String descripcion) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Usuario usuario = usuarioRepository.findByEmail(email);

        Profesional profesional = new Profesional();
        profesional.setNombre(nombre);
        profesional.setProfesion(profesion);
        profesional.setDescripcion(descripcion);
        profesional.setEstado("PENDIENTE");
        profesional.setUsuario(usuario);

        profesionalRepository.save(profesional);

        return "redirect:/panel";
    }
    @PostMapping("/registro")
    public String registrarUsuario(
            @RequestParam String email,
            @RequestParam String password,
            Model model) {

        if (usuarioRepository.findByEmail(email) != null) {
            model.addAttribute("error", "El email ya está registrado");
            return "registro";
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setRol("USER");

        usuarioRepository.save(usuario);

        return "redirect:/login";
    }
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        profesionalRepository.deleteById(id);
        return "redirect:/";
    }
    @GetMapping("/admin/pendientes")
    public String verPendientes(Model model) {
        model.addAttribute("pendientes",
                profesionalRepository.findByEstado("PENDIENTE"));
        return "pendientes";
    }
    @GetMapping("/admin/rechazar/{id}")
    public String rechazar(@PathVariable Long id) {

        Profesional profesional = profesionalRepository.findById(id).orElse(null);

        if (profesional != null) {
            profesional.setEstado("RECHAZADO");
            profesionalRepository.save(profesional);
        }

        return "redirect:/admin/pendientes";
    }
    @GetMapping("/profesional/{id}")
    public String verProfesional(@PathVariable Long id, Model model) {

        Profesional profesional =
                profesionalRepository.findById(id).orElse(null);

        if (profesional == null ||
                !profesional.getEstado().equals("APROBADO")) {
            return "redirect:/";
        }

        model.addAttribute("p", profesional);

        return "detalle";
    }
    @GetMapping("/admin/aprobar/{id}")
    public String aprobar(@PathVariable Long id) {

        Profesional profesional = profesionalRepository.findById(id).orElseThrow();
        profesional.setEstado("APROBADO");

        profesionalRepository.save(profesional);

        return "redirect:/admin/pendientes";
    }
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Profesional profesional) {

        System.out.println("ID recibido: " + profesional.getId());

        profesionalRepository.save(profesional);
        return "redirect:/";
    }


}