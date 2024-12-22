package uz.pdp.e_commerce_with_springboot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.e_commerce_with_springboot.entity.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
}