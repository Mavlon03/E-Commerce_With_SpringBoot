package uz.pdp.e_commerce_with_springboot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.e_commerce_with_springboot.entity.AttachmentContent;

public interface AttachmentContentRepository extends JpaRepository<AttachmentContent, Integer> {
    AttachmentContent findByAttachmentId(Integer id);

}