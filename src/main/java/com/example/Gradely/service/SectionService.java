package com.example.Gradely.service;

import com.example.Gradely.database.model.Sections;
import com.example.Gradely.database.repository.SectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public static class SectionAddRequest {
        public String name;
    }

    public static class SectionAddResponse {
        public String id;
        public String name;
        public List<Sections.Class> classes;

        public SectionAddResponse(String id, String name, List<Sections.Class> classes) {
            this.id = id;
            this.name = name;
            this.classes = classes;
        }
    }

    @Transactional
    public SectionAddResponse add(SectionAddRequest body) {

        Sections sections = new Sections();
        sections.setName(body.name);
        sections.setClasses(new ArrayList<>());

        sectionRepository.save(sections);

        return new SectionAddResponse(
                sections.getId(),
                sections.getName(),
                sections.getClasses()
        );
    }
}