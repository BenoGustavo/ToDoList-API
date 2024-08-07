package dev.gustavo.ToDoListAPI.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.gustavo.ToDoListAPI.models.PictureModel;
import dev.gustavo.ToDoListAPI.service.PictureService;
import dev.gustavo.ToDoListAPI.utils.responses.builder.ResponseBuilder;
import dev.gustavo.ToDoListAPI.utils.responses.generic.Response;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/storage")
public class PictureController {
    @Autowired
    PictureService pictureService;

    @PostMapping("/upload")
    public ResponseEntity<Response<PictureModel>> uploadPicture(@RequestParam("file") MultipartFile file,
            HttpServletRequest request)
            throws IOException, SQLException, NoSuchAlgorithmException {
        ResponseBuilder<PictureModel> responseBuilder = new ResponseBuilder<>();
        PictureModel picture = pictureService.saveFile(file, request);

        return ResponseEntity
                .ok(responseBuilder.data(picture).status(200).result("File uploaded successfully").build());
    }

    @GetMapping("/download/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<Response<List<PictureModel>>> downloadPicture(@PathVariable("id") UUID id,
            HttpServletRequest request) {
        ResponseBuilder<List<PictureModel>> responseBuilder = new ResponseBuilder<>();

        System.out.println("Inicio do controller. " + id.toString());
        List<PictureModel> data = pictureService.downloadPicture(id, request);

        responseBuilder = responseBuilder.data(data).result("images successfully gotten").status(200);

        return ResponseEntity.ok(responseBuilder.build());
    }
}
