package cloud.opensky.coupons.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
public class Campaign {
    public enum Style {
        UniqueUsage,
        SharedUsage
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(columnDefinition = "varchar(200)", nullable = false)
    private String name;

    @Column(columnDefinition = "varchar(500)")
    private String description;

    @Column(name = "expires_at")
    private Timestamp expiresAt;

    @Column(name = "starts_at", nullable = false)
    private Timestamp startsAt;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private Boolean cumulative;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Style style;

    @OneToOne
    @JoinColumn(name = "campaign_rule_id", nullable = true)
    private CampaignRule campaignRule;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Timestamp(System.currentTimeMillis());
    }
}
