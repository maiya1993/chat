package com.company.webChat2.controller;

import com.company.webChat2.domain.Message;
import com.company.webChat2.domain.User;
import com.company.webChat2.repos.MessageRepo;
import com.company.webChat2.service.MessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Controller
public class MainController {

    private final MessageRepo messageRepo;
    private final MessageService messageService;

    public MainController(MessageRepo messageRepo, MessageService messageService) {
        this.messageRepo = messageRepo;
        this.messageService = messageService;
    }

    @Value("${upload.path}")
    private String uploadPath;

    @SuppressWarnings("SameReturnValue")
    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    @SuppressWarnings("SameReturnValue")
    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        messageRepo.findAll();
        Iterable<Message> messages;

        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "main";
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "SameReturnValue"})
    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag, Map<String, Object> model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        Message message = new Message(text, tag, user);

        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                //noinspection ResultOfMethodCallIgnored
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            message.setFilename(resultFilename);
        }

        messageRepo.save(message);
        Iterable<Message> messages = messageRepo.findAll();
        model.put("messages", messages);

        return "main";
    }

    @SuppressWarnings("SameReturnValue")
    @PostMapping("del")
    public String delete(String id, Map<String, Object> model) {

        messageService.deleteMessage(id);
        Iterable<Message> messages = messageRepo.findAll();
        model.put("messages", messages);

        return "redirect:/main";
    }

}