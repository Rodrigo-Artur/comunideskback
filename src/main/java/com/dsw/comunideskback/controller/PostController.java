package com.dsw.comunideskback.controller;

import com.dsw.comunideskback.model.Post;
import com.dsw.comunideskback.model.Usuario;
// MUDANÇA: Import corrigido para o novo local (sem .enums)
import com.dsw.comunideskback.model.PostType;
import com.dsw.comunideskback.repository.PostRepository;
import com.dsw.comunideskback.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findByAtivoTrue();
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Post post, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String login = userDetails.getUsername();
            Optional<Usuario> userOpt = usuarioRepository.findByLogin(login);
            
            if (userOpt.isPresent()) {
                post.setUsuario(userOpt.get());
            } else {
                return ResponseEntity.status(403).body("Erro: Usuário do token não encontrado no banco.");
            }

            post.setDataCriacao(LocalDateTime.now());
            post.setDataAtualizacao(LocalDateTime.now());
            post.setAtivo(true);
            
            if (post.getExpirationDate() == null) {
                post.setExpirationDate(LocalDateTime.now().plusDays(30));
            }

            Post savedPost = postRepository.save(post);
            return ResponseEntity.ok(savedPost);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao criar post: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable String id, @RequestBody Post postAtualizado, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Post> postOpt = postRepository.findById(id);

        if (postOpt.isPresent()) {
            Post postExistente = postOpt.get();
            
            // VERIFICAÇÃO DE PERMISSÃO: Dono OU Admin
            boolean isOwner = postExistente.getUsuario().getLogin().equals(userDetails.getUsername());
            boolean isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (!isOwner && !isAdmin) {
                return ResponseEntity.status(403).body("Acesso negado: Você não tem permissão para editar este post.");
            }

            postExistente.setTitle(postAtualizado.getTitle());
            postExistente.setDescription(postAtualizado.getDescription());
            postExistente.setCategory(postAtualizado.getCategory());
            postExistente.setTags(postAtualizado.getTags());
            postExistente.setDataAtualizacao(LocalDateTime.now());

            return ResponseEntity.ok(postRepository.save(postExistente));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Post> postOpt = postRepository.findById(id);

        if (postOpt.isPresent()) {
            Post post = postOpt.get();

            // VERIFICAÇÃO DE PERMISSÃO: Dono OU Admin
            boolean isOwner = post.getUsuario().getLogin().equals(userDetails.getUsername());
            boolean isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (!isOwner && !isAdmin) {
                return ResponseEntity.status(403).body("Acesso negado: Você não tem permissão para excluir este post.");
            }

            post.setAtivo(false);
            postRepository.save(post);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}