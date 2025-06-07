package com.example.Gradely.service;

import com.example.Gradely.database.model.Section;
import com.example.Gradely.database.repository.SectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        public Section.Class classes;

        public SectionAddResponse(String id, String name, Section.Class classes) {
            this.id = id;
            this.name = name;
            this.classes = classes;
        }
    }

    @Transactional
    public SectionAddResponse add(SectionAddRequest body) {

        Section section = new Section();
        section.setName(body.name);
        section.setClasses(new Section.Class());

        sectionRepository.save(section);

        return new SectionAddResponse(
                section.getId(),
                section.getName(),
                section.getClasses()
        );
    }
}