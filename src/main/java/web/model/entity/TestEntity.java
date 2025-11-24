package web.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "test")
@Getter@Setter@AllArgsConstructor@NoArgsConstructor
@Builder
public class TestEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int id;

    @Column
    private String content;

}
