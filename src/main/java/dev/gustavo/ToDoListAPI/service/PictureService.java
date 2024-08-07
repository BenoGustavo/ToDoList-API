package dev.gustavo.ToDoListAPI.service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import dev.gustavo.ToDoListAPI.models.PictureModel;
import dev.gustavo.ToDoListAPI.models.UserModel;
import dev.gustavo.ToDoListAPI.repositories.interfaces.IPictureRepository;
import dev.gustavo.ToDoListAPI.repositories.interfaces.IUserRepository;
import dev.gustavo.ToDoListAPI.utils.JWT.JwtUtil;
import dev.gustavo.ToDoListAPI.utils.error.custom.NotFound404Exception;
import dev.gustavo.ToDoListAPI.utils.error.custom.Unauthorized401Exception;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class PictureService {

    @Autowired
    private IPictureRepository pictureRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private IUserRepository userRepository;

    @Transactional
    public PictureModel saveFile(MultipartFile file, HttpServletRequest request)
            throws IOException, NoSuchAlgorithmException {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader != null ? authHeader.substring(7) : null;

        if (token == null || token.isEmpty()) {
            throw new Unauthorized401Exception("Authorization token is missing");
        }

        String email = jwtUtil.extractEmail(token);
        UserModel user = userRepository.findByEmail(email);

        if (user == null) {
            throw new NotFound404Exception("User not found");
        }

        PictureModel fileEntity = new PictureModel();

        byte[] base64 = file.getBytes();

        String pictureHash = computePictureHash(base64);

        fileEntity.setPicture(base64);
        fileEntity.setPictureHash(pictureHash);
        fileEntity.setOwner(user);
        fileEntity.setName(email + "_" + file.getName() + "_" + LocalDateTime.now().toString());

        pictureRepository.save(fileEntity);
        return fileEntity;
    }

    public List<PictureModel> downloadPicture(UUID id, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader != null ? authHeader.substring(7) : null;

        if (token == null || token.isEmpty()) {
            throw new Unauthorized401Exception("Authorization token is missing");
        }

        String email = jwtUtil.extractEmail(token);
        UserModel requestUser = userRepository.findByEmail(email);
        UserModel owner = userRepository.findById(id)
                .orElseThrow(() -> new NotFound404Exception("Invalid id, user doesn't exist"));

        System.out.println("Got some infos like owner " + owner);

        if (!requestUser.equals(owner)) {
            throw new Unauthorized401Exception("You aren't the owner of this picture");
        }

        List<PictureModel> pictures = pictureRepository.findByOwner(owner);

        return pictures;
    }

    private String computePictureHash(byte[] picture) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(picture);
        return Base64.getEncoder().encodeToString(hash);
    }
}